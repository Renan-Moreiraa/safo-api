package com.safo.safoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.safo.safoandroid.model.Agendamento;
import com.safo.safoandroid.network.RetrofitClient;
import com.safo.safoandroid.network.SafoApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    private TextView tvPendentesCount, tvQuadrasCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvPendentesCount = findViewById(R.id.tvPendentesCount);
        tvQuadrasCount = findViewById(R.id.tvQuadrasCount);

        MaterialCardView cardColaboradores = findViewById(R.id.cardColaboradores);
        MaterialCardView cardQuadras = findViewById(R.id.cardQuadras);
        MaterialCardView cardAgendamentos = findViewById(R.id.cardAgendamentos);
        MaterialCardView cardAtendimentos = findViewById(R.id.cardAtendimentos);
        Button btnSair = findViewById(R.id.btnSairAdmin);

        cardColaboradores.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
        });

        cardQuadras.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, QuadrasActivity.class);
            startActivity(intent);
        });

        cardAgendamentos.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, AgendamentosActivity.class);
            startActivity(intent);
        });

        cardAtendimentos.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, AtendimentosActivity.class);
            startActivity(intent);
        });

        btnSair.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarMetricas();
    }

    private void carregarMetricas() {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);

        // Busca a lista completa de agendamentos para atualizar o dashboard
        apiService.getAgendamentos().enqueue(new Callback<List<Agendamento>>() {
            @Override
            public void onResponse(Call<List<Agendamento>> call, Response<List<Agendamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Agendamento> lista = response.body();

                    long pendentes = 0;
                    for (Agendamento a : lista) {
                        if ("PENDENTE".equalsIgnoreCase(a.getStatus())) {
                            pendentes++;
                        }
                    }

                    // Contador 1: Quantos agendamentos estão PENDENTES para alerta
                    tvPendentesCount.setText(String.valueOf(pendentes));

                    // Contador 2: Total geral de agendamentos acumulados
                    tvQuadrasCount.setText(String.valueOf(lista.size()));
                }
            }

            @Override
            public void onFailure(Call<List<Agendamento>> call, Throwable t) {
                // Tratamento silencioso em caso de falha transitória de rede
            }
        });
    }
}