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
        Button btnSair = findViewById(R.id.btnSairCliente);

        // Recebe o nome do cliente vindo da LoginActivity
        nomeCliente = getIntent().getStringExtra("nomeCliente");
        if (nomeCliente != null && !nomeCliente.isEmpty()) {
            tvBoasVindas.setText("Bem-vindo(a), " + nomeCliente + "!");
        }

        // Abre a tela para criar um novo agendamento
        cardNovoAgendamento.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, CadastroAgendamentoActivity.class);
            intent.putExtra("nomeCliente", nomeCliente);
            startActivity(intent);
        });

        // Abre a AgendamentosActivity filtrando APENAS as reservas deste cliente
        cardMeusAgendamentos.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, AgendamentosActivity.class);
            intent.putExtra("nomeCliente", nomeCliente);
            startActivity(intent);
        });

        // Sair da conta
        btnSair.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClienteActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}