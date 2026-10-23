package com.safo.safoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.safo.safoandroid.model.Usuario;
import com.safo.safoandroid.network.RetrofitClient;
import com.safo.safoandroid.network.SafoApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private Button btnLogin, btnIrParaCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmailLogin);
        editSenha = findViewById(R.id.editSenhaLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnIrParaCadastro = findViewById(R.id.btnIrParaCadastro);

        btnLogin.setOnClickListener(v -> realizarLogin());

        btnIrParaCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
            startActivity(intent);
        });
    }

    private void realizarLogin() {
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);

        Usuario requestUsuario = new Usuario();
        requestUsuario.setEmail(email);
        requestUsuario.setSenha(senha);

        apiService.login(requestUsuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();
                    Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();

                    if ("ADMIN".equalsIgnoreCase(usuario.getPerfil())) {
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, MenuClienteActivity.class);
                        intent.putExtra("nomeCliente", usuario.getNome());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciais inválidas!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erro de conexão com o servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}