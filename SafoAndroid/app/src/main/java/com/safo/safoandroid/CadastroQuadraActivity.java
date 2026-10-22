package com.safo.safoandroid;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.safo.safoandroid.model.Quadra;
import com.safo.safoandroid.network.RetrofitClient;
import com.safo.safoandroid.network.SafoApiService;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroQuadraActivity extends AppCompatActivity {

    private EditText editNome, editPrecoHora, editStatus;
    private Spinner spinnerTipoEsporte;
    private Button btnSalvar;
    private TextView textTitulo;
    private Long idQuadraEdicao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_quadra);

        textTitulo = findViewById(R.id.textTituloQuadra);
        editNome = findViewById(R.id.editNomeQuadra);
        spinnerTipoEsporte = findViewById(R.id.spinnerTipoEsporte);
        editPrecoHora = findViewById(R.id.editPrecoHora);
        editStatus = findViewById(R.id.editStatusQuadra);
        btnSalvar = findViewById(R.id.btnSalvarQuadra);

        // Preenche as opções do Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.esportes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoEsporte.setAdapter(adapter);

        // Se vier ID no Intent, configura a tela para Edição
        if (getIntent().hasExtra("id")) {
            idQuadraEdicao = getIntent().getLongExtra("id", -1);
            textTitulo.setText("Editar Quadra");
            editNome.setText(getIntent().getStringExtra("nome"));

            String tipoEsporteIntent = getIntent().getStringExtra("tipoEsporte");
            if (tipoEsporteIntent != null) {
                int spinnerPosition = adapter.getPosition(tipoEsporteIntent);
                spinnerTipoEsporte.setSelection(spinnerPosition);
            }

            editPrecoHora.setText(getIntent().getStringExtra("precoHora"));
            editStatus.setText(getIntent().getStringExtra("status"));
            btnSalvar.setText("Atualizar Quadra");
        }

        btnSalvar.setOnClickListener(v -> salvarQuadra());
    }

    private void salvarQuadra() {
        String nome = editNome.getText().toString().trim();
        String tipoEsporte = spinnerTipoEsporte.getSelectedItem().toString();
        String precoStr = editPrecoHora.getText().toString().trim();
        String status = editStatus.getText().toString().trim();

        if (nome.isEmpty() || precoStr.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal precoHora;
        try {
            precoHora = new BigDecimal(precoStr);
        } catch (Exception e) {
            Toast.makeText(this, "Insira um preço válido!", Toast.LENGTH_SHORT).show();
            return;
        }

        Quadra quadra = new Quadra();
        quadra.setNome(nome);
        quadra.setTipoEsporte(tipoEsporte);
        quadra.setPrecoHora(precoHora);
        quadra.setStatus(status.toUpperCase());

        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);

        if (idQuadraEdicao != null && idQuadraEdicao != -1) {
            quadra.setIdQuadra(idQuadraEdicao);
            apiService.atualizarQuadra(idQuadraEdicao, quadra).enqueue(new Callback<Quadra>() {
                @Override
                public void onResponse(Call<Quadra> call, Response<Quadra> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CadastroQuadraActivity.this, "Quadra atualizada!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CadastroQuadraActivity.this, "Erro HTTP: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Quadra> call, Throwable t) {
                    Toast.makeText(CadastroQuadraActivity.this, "Falha ao atualizar quadra", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            apiService.salvarQuadra(quadra).enqueue(new Callback<Quadra>() {
                @Override
                public void onResponse(Call<Quadra> call, Response<Quadra> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CadastroQuadraActivity.this, "Quadra cadastrada!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CadastroQuadraActivity.this, "Erro HTTP: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Quadra> call, Throwable t) {
                    Toast.makeText(CadastroQuadraActivity.this, "Falha ao salvar quadra", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}