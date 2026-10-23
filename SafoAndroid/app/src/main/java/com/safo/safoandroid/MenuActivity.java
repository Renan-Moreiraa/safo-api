package com.safo.safoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.safo.safoandroid.model.Agendamento;
import com.safo.safoandroid.model.Quadra;
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

        // Busca o número de agendamentos pendentes em tempo real
        apiService.getAgendamentos().enqueue(new Callback<List<Agendamento>>() {
            @Override
            public void onResponse(Call<List<Agendamento>> call, Response<List<Agendamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    long pendentes = 0;
                    for (Agendamento a : response.body()) {
                        if ("PENDENTE".equalsIgnoreCase(a.getStatus())) {
                            pendentes++;
                        }
                    }
                    tvPendentesCount.setText(String.valueOf(pendentes));
                }
            }

            @Override
            public void onFailure(Call<List<Agendamento>> call, Throwable t) {
                // Tratamento silencioso em caso de falha transitória de rede
            }
        });

        // Busca o número total de quadras em tempo real
        apiService.getQuadras().enqueue(new Callback<List<Quadra>>() {
            @Override
            public void onResponse(Call<List<Quadra>> call, Response<List<Quadra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvQuadrasCount.setText(String.valueOf(response.body().size()));
                }
            }

            @Override
            public void onFailure(Call<List<Quadra>> call, Throwable t) {
                // Tratamento silencioso em caso de falha transitória de rede
            }
        });
    }
}