package com.safo.safoandroid;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.safo.safoandroid.model.Agendamento;
import com.safo.safoandroid.model.Quadra;
import com.safo.safoandroid.network.RetrofitClient;
import com.safo.safoandroid.network.SafoApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroAgendamentoActivity extends AppCompatActivity {

    private Spinner spinnerQuadras;
    private EditText editNomeCliente, editDataHoraInicio, editDataHoraFim, editStatus;
    private Button btnSalvar;
    private List<Quadra> listaQuadras = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_agendamento);

        spinnerQuadras = findViewById(R.id.spinnerQuadras);
        editNomeCliente = findViewById(R.id.editNomeCliente);
        editDataHoraInicio = findViewById(R.id.editDataHoraInicio);
        editDataHoraFim = findViewById(R.id.editDataHoraFim);
        editStatus = findViewById(R.id.editStatusAgendamento);
        btnSalvar = findViewById(R.id.btnSalvarAgendamento);

        carregarQuadrasNoSpinner();

        btnSalvar.setOnClickListener(v -> salvarAgendamento());
    }

    private void carregarQuadrasNoSpinner() {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.getQuadras().enqueue(new Callback<List<Quadra>>() {
            @Override
            public void onResponse(Call<List<Quadra>> call, Response<List<Quadra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaQuadras = response.body();
                    List<String> nomesQuadras = new ArrayList<>();
                    for (Quadra q : listaQuadras) {
                        nomesQuadras.add(q.getNome() + " (" + q.getTipoEsporte() + ")");
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CadastroAgendamentoActivity.this,
                            android.R.layout.simple_spinner_item, nomesQuadras);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerQuadras.setAdapter(adapter);
                } else {
                    Toast.makeText(CadastroAgendamentoActivity.this, "Erro ao carregar quadras", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Quadra>> call, Throwable t) {
                Toast.makeText(CadastroAgendamentoActivity.this, "Falha de conexão com a API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salvarAgendamento() {
        if (listaQuadras.isEmpty()) {
            Toast.makeText(this, "Nenhuma quadra disponível para agendamento!", Toast.LENGTH_SHORT).show();
            return;
        }

        int posicaoSelecionada = spinnerQuadras.getSelectedItemPosition();
        Quadra quadraSelecionada = listaQuadras.get(posicaoSelecionada);

        String cliente = editNomeCliente.getText().toString().trim();
        String inicio = editDataHoraInicio.getText().toString().trim();
        String fim = editDataHoraFim.getText().toString().trim();
        String status = editStatus.getText().toString().trim();

        if (cliente.isEmpty() || inicio.isEmpty() || fim.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setQuadra(quadraSelecionada);
        agendamento.setNomeCliente(cliente);
        agendamento.setDataHoraInicio(inicio);
        agendamento.setDataHoraFim(fim);
        agendamento.setStatus(status.toUpperCase());

        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.salvarAgendamento(agendamento).enqueue(new Callback<Agendamento>() {
            @Override
            public void onResponse(Call<Agendamento> call, Response<Agendamento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroAgendamentoActivity.this, "Agendamento realizado!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CadastroAgendamentoActivity.this, "Erro HTTP: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Agendamento> call, Throwable t) {
                Toast.makeText(CadastroAgendamentoActivity.this, "Falha ao criar agendamento", Toast.LENGTH_SHORT).show();
            }
        });
    }
}