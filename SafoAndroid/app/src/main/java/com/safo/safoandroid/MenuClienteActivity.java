package com.safo.safoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class MenuClienteActivity extends AppCompatActivity {

    private MaterialCardView cardVerQuadras, cardMinhasReservas;
    private Button btnSair;
    private TextView textBoasVindas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cliente);

        // Recebe o nome do cliente enviado pela LoginActivity
        String nomeCliente = getIntent().getStringExtra("nomeCliente");

        textBoasVindas = findViewById(R.id.textBoasVindasCliente);
        if (nomeCliente != null && !nomeCliente.isEmpty()) {
            textBoasVindas.setText("Olá, " + nomeCliente + "!");
        }

        cardVerQuadras = findViewById(R.id.cardVerQuadras);
        cardMinhasReservas = findViewById(R.id.cardMinhasReservas);
        btnSair = findViewById(R.id.btnSairCliente);

        // Abrir tela de novo agendamento com os dados do cliente
        cardVerQuadras.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, CadastroAgendamentoActivity.class);
            intent.putExtra("nomeCliente", nomeCliente);
            intent.putExtra("perfilUsuario", "CLIENTE");
            startActivity(intent);
        });

        // Abrir tela de agendamentos filtrando apenas as reservas do cliente
        cardMinhasReservas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, AgendamentosActivity.class);
            intent.putExtra("nomeCliente", nomeCliente);
            startActivity(intent);
        });

        // Botão de Sair (Logout)
        btnSair.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}