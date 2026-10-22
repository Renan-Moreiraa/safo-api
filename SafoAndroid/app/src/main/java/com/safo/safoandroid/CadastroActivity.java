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
    private Long idColaboradorEdicao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editNome = findViewById(R.id.editNome);
        editCpf = findViewById(R.id.editCpf);
        editEspecialidade = findViewById(R.id.editEspecialidade);
        editHorario = findViewById(R.id.editHorario);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Verifica se vieram dados para edição
        if (getIntent().hasExtra("id")) {
            idColaboradorEdicao = getIntent().getLongExtra("id", -1);
            editNome.setText(getIntent().getStringExtra("nome"));
            editCpf.setText(getIntent().getStringExtra("cpf"));
            editEspecialidade.setText(getIntent().getStringExtra("especialidade"));
            editHorario.setText(getIntent().getStringExtra("horario"));
            btnSalvar.setText("Atualizar Colaborador");
        }

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

        Colaborador colaborador = new Colaborador();
        colaborador.setNome(nome);
        colaborador.setCpf(cpf);
        colaborador.setEspecialidade(especialidade);
        colaborador.setHorarioTrabalho(horario);
        colaborador.setStatus("ATIVO");

        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);

        if (idColaboradorEdicao != null && idColaboradorEdicao != -1) {
            // Executa o PUT para atualizar
            colaborador.setIdColaborador(idColaboradorEdicao);
            apiService.atualizarColaborador(idColaboradorEdicao, colaborador).enqueue(new Callback<Colaborador>() {
                @Override
                public void onResponse(Call<Colaborador> call, Response<Colaborador> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CadastroActivity.this, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CadastroActivity.this, "Erro HTTP: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Colaborador> call, Throwable t) {
                    Toast.makeText(CadastroActivity.this, "Falha ao atualizar", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Executa o POST para cadastrar novo
            apiService.salvarColaborador(colaborador).enqueue(new Callback<Colaborador>() {
                @Override
                public void onResponse(Call<Colaborador> call, Response<Colaborador> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CadastroActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CadastroActivity.this, "Erro HTTP: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Colaborador> call, Throwable t) {
                    Toast.makeText(CadastroActivity.this, "Falha ao salvar", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}