package com.safo.safoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class MenuClienteActivity extends AppCompatActivity {

    private String nomeCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cliente);

        TextView tvBoasVindas = findViewById(R.id.tvBoasVindasCliente);
        MaterialCardView cardNovoAgendamento = findViewById(R.id.cardNovoAgendamento);
        MaterialCardView cardMeusAgendamentos = findViewById(R.id.cardMeusAgendamentos);
        MaterialCardView cardAtendimentoCliente = findViewById(R.id.cardAtendimentoCliente);
        Button btnSair = findViewById(R.id.btnSairCliente);

        nomeCliente = getIntent().getStringExtra("nomeCliente");
        if (nomeCliente != null && !nomeCliente.isEmpty()) {
            tvBoasVindas.setText("Bem-vindo(a), " + nomeCliente + "!");
        }

        cardNovoAgendamento.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, CadastroAgendamentoActivity.class);
            intent.putExtra("nomeCliente", nomeCliente);
            startActivity(intent);
        });

        cardMeusAgendamentos.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, AgendamentosActivity.class);
            intent.putExtra("nomeCliente", nomeCliente);
            startActivity(intent);
        });

        cardAtendimentoCliente.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, AtendimentosActivity.class);
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