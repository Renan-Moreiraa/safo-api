package com.safo.safoandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroAgendamentoActivity extends AppCompatActivity {

    private Spinner spinnerQuadras;
    private EditText editNomeCliente, editDataHoraInicio, editDataHoraFim, editStatus;
    private Button btnSalvar;
    private List<Quadra> listaQuadras = new ArrayList<>();
    private String perfilUsuario = "CLIENTE";

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

        // Recebe os dados do usuário
        String nomeCliente = getIntent().getStringExtra("nomeCliente");
        if (getIntent().hasExtra("perfilUsuario")) {
            perfilUsuario = getIntent().getStringExtra("perfilUsuario");
        }

        // Se houver nome de cliente passado, preenche e desabilita edição
        if (nomeCliente != null && !nomeCliente.isEmpty()) {
            editNomeCliente.setText(nomeCliente);
            editNomeCliente.setEnabled(false);
        }

        // Se for Cliente, fixa o status como PENDENTE e desabilita alteração
        if ("CLIENTE".equalsIgnoreCase(perfilUsuario) || (nomeCliente != null && !nomeCliente.isEmpty())) {
            editStatus.setText("PENDENTE");
            editStatus.setEnabled(false);
        }

        // Configura seletores de data/hora ao clicar nos campos
        editDataHoraInicio.setOnClickListener(v -> abrirSeletorDataHora(editDataHoraInicio));
        editDataHoraFim.setOnClickListener(v -> abrirSeletorDataHora(editDataHoraFim));

        carregarQuadras();

        btnSalvar.setOnClickListener(v -> salvarAgendamento());
    }

    private void abrirSeletorDataHora(EditText campoTarget) {
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            int hora = c.get(Calendar.HOUR_OF_DAY);
            int minuto = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02dT%02d:%02d:00",
                        year, month + 1, dayOfMonth, hourOfDay, minute);
                campoTarget.setText(dataFormatada);
            }, hora, minuto, true);
            timePickerDialog.show();

        }, ano, mes, dia);
        datePickerDialog.show();
    }

    private void carregarQuadras() {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.getQuadras().enqueue(new Callback<List<Quadra>>() {
            @Override
            public void onResponse(Call<List<Quadra>> call, Response<List<Quadra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaQuadras = response.body();
                    List<String> nomesQuadras = new ArrayList<>();
                    for (Quadra q : listaQuadras) {
                        nomesQuadras.add(q.getNome());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CadastroAgendamentoActivity.this,
                            android.R.layout.simple_spinner_item, nomesQuadras);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerQuadras.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Quadra>> call, Throwable t) {
                Toast.makeText(CadastroAgendamentoActivity.this, "Erro ao carregar quadras", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salvarAgendamento() {
        if (spinnerQuadras.getSelectedItemPosition() < 0 || listaQuadras.isEmpty()) {
            Toast.makeText(this, "Selecione uma quadra!", Toast.LENGTH_SHORT).show();
            return;
        }

        Quadra quadraSelecionada = listaQuadras.get(spinnerQuadras.getSelectedItemPosition());
        String nomeCliente = editNomeCliente.getText().toString().trim();
        String inicio = editDataHoraInicio.getText().toString().trim();
        String fim = editDataHoraFim.getText().toString().trim();
        String status = editStatus.getText().toString().trim();

        if (nomeCliente.isEmpty() || inicio.isEmpty() || fim.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        Agendamento ag = new Agendamento();
        ag.setQuadra(quadraSelecionada);
        ag.setNomeCliente(nomeCliente);
        ag.setDataHoraInicio(inicio);
        ag.setDataHoraFim(fim);
        ag.setStatus(status.toUpperCase());

        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.salvarAgendamento(ag).enqueue(new Callback<Agendamento>() {
            @Override
            public void onResponse(Call<Agendamento> call, Response<Agendamento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroAgendamentoActivity.this, "Solicitação de reserva enviada!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CadastroAgendamentoActivity.this, "Erro ao solicitar reserva", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Agendamento> call, Throwable t) {
                Toast.makeText(CadastroAgendamentoActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}