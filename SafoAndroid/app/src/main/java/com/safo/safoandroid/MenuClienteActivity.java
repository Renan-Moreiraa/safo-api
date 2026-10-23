package com.safo.safoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class MenuClienteActivity extends AppCompatActivity {

    private MaterialCardView cardVerQuadras, cardMinhasReservas;
    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cliente);

        cardVerQuadras = findViewById(R.id.cardVerQuadras);
        cardMinhasReservas = findViewById(R.id.cardMinhasReservas);
        btnSair = findViewById(R.id.btnSairCliente);

        cardVerQuadras.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, QuadrasActivity.class);
            startActivity(intent);
        });

        cardMinhasReservas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, AgendamentosActivity.class);
            startActivity(intent);
        });

        btnSair.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}