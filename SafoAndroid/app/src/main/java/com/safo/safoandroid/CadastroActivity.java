package com.safo.safoandroid;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.safo.safoandroid.model.Colaborador;
import com.safo.safoandroid.network.RetrofitClient;
import com.safo.safoandroid.network.SafoApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNome, editCpf, editEspecialidade, editHorario;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editNome = findViewById(R.id.editNome);
        editCpf = findViewById(R.id.editCpf);
        editEspecialidade = findViewById(R.id.editEspecialidade);
        editHorario = findViewById(R.id.editHorario);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(v -> salvarColaborador());
    }

    private void salvarColaborador() {
        String nome = editNome.getText().toString().trim();
        String cpf = editCpf.getText().toString().trim();
        String especialidade = editEspecialidade.getText().toString().trim();
        String horario = editHorario.getText().toString().trim();

        if (nome.isEmpty() || cpf.isEmpty() || especialidade.isEmpty() || horario.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        Colaborador novoColaborador = new Colaborador();
        novoColaborador.setNome(nome);
        novoColaborador.setCpf(cpf);
        novoColaborador.setEspecialidade(especialidade);
        novoColaborador.setHorarioTrabalho(horario);
        novoColaborador.setStatus("ATIVO");

        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.salvarColaborador(novoColaborador).enqueue(new Callback<Colaborador>() {
            @Override
            public void onResponse(Call<Colaborador> call, Response<Colaborador> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a tela de cadastro e retorna à lista
                } else {
                    Toast.makeText(CadastroActivity.this, "Erro HTTP: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Colaborador> call, Throwable t) {
                Log.e("API_TESTE", "Erro ao salvar: " + t.getMessage());
                Toast.makeText(CadastroActivity.this, "Falha na conexão ao salvar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}