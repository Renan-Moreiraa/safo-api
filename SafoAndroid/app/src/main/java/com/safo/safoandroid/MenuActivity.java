package com.safo.safoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MaterialCardView cardColaboradores = findViewById(R.id.cardColaboradores);
        MaterialCardView cardQuadras = findViewById(R.id.cardQuadras);
        MaterialCardView cardAgendamentos = findViewById(R.id.cardAgendamentos);

        // Abre o módulo de Colaboradores (MainActivity)
        cardColaboradores.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Abre o módulo de Quadras (QuadrasActivity)
        cardQuadras.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, QuadrasActivity.class);
            startActivity(intent);
        });

        // Módulo de Agendamentos (em breve)
        cardAgendamentos.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, AgendamentosActivity.class);
            startActivity(intent);
        });

        Button btnSair = findViewById(R.id.btnSairAdmin);
        btnSair.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}