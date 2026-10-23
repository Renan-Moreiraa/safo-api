package com.safo.safoandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.safo.safoandroid.model.Atendimento;
import com.safo.safoandroid.model.AtendimentoRequestDTO;
import com.safo.safoandroid.network.RetrofitClient;
import com.safo.safoandroid.network.SafoApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AtendimentosActivity extends AppCompatActivity {

    private RecyclerView rvAtendimentos;
    private Button btnNovoAtendimento;
    private AtendimentoAdapter adapter;
    private List<Atendimento> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atendimentos);

        rvAtendimentos = findViewById(R.id.rvAtendimentos);
        btnNovoAtendimento = findViewById(R.id.btnNovoAtendimento);

        rvAtendimentos.setLayoutManager(new LinearLayoutManager(this));

        // Passa a ação de clique para tratar o atendimento (responder ou visualizar)
        adapter = new AtendimentoAdapter(lista, atendimento -> tratarCliqueItem(atendimento));
        rvAtendimentos.setAdapter(adapter);

        btnNovoAtendimento.setOnClickListener(v -> abrirDialogNovoAtendimento());

        carregarAtendimentos();
    }

    private void carregarAtendimentos() {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.getAtendimentos().enqueue(new Callback<List<Atendimento>>() {
            @Override
            public void onResponse(Call<List<Atendimento>> call, Response<List<Atendimento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lista.clear();
                    lista.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AtendimentosActivity.this, "Erro ao carregar atendimentos: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Atendimento>> call, Throwable t) {
                Toast.makeText(AtendimentosActivity.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirDialogNovoAtendimento() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nova Pergunta / Suporte");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_novo_atendimento, null);
        EditText editJustificativa = view.findViewById(R.id.editJustificativaAtendimento);

        builder.setView(view);
        builder.setPositiveButton("Enviar", (dialog, which) -> {
            String mensagem = editJustificativa.getText().toString().trim();
            if (!mensagem.isEmpty()) {
                enviarAtendimento(mensagem);
            } else {
                Toast.makeText(this, "Escreva a sua dúvida antes de enviar!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void enviarAtendimento(String mensagem) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
        String dataFormatada = sdf.format(new java.util.Date());

        AtendimentoRequestDTO dto = new AtendimentoRequestDTO(
                dataFormatada,
                "PENDENTE",
                mensagem,
                1L, // idColaborador
                1L  // idAgendamento
        );

        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.criarAtendimento(dto).enqueue(new Callback<Atendimento>() {
            @Override
            public void onResponse(Call<Atendimento> call, Response<Atendimento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AtendimentosActivity.this, "Pergunta enviada com sucesso!", Toast.LENGTH_SHORT).show();
                    carregarAtendimentos();
                } else {
                    Toast.makeText(AtendimentosActivity.this, "Erro HTTP: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Atendimento> call, Throwable t) {
                Toast.makeText(AtendimentosActivity.this, "Falha de conexão ao enviar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tratarCliqueItem(Atendimento atendimento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Long idAgendamento = (atendimento.getAgendamento() != null && atendimento.getAgendamento().getIdAgendamento() != null)
                ? atendimento.getAgendamento().getIdAgendamento()
                : atendimento.getIdAtendimento();

        boolean isRespondido = "RESPONDIDO".equalsIgnoreCase(atendimento.getStatus()) || "CONCLUIDO".equalsIgnoreCase(atendimento.getStatus());

        if (isRespondido) {
            // MODO VISUALIZAÇÃO: Exibe a dúvida e o status sem permitir alteração
            builder.setTitle("Detalhes do Agendamento #" + idAgendamento);

            String detalhe = "Dúvida / Mensagem enviada:\n\""
                    + (atendimento.getJustificativaCancelamento() != null ? atendimento.getJustificativaCancelamento() : "Sem mensagem")
                    + "\"\n\nStatus: " + atendimento.getStatus();

            builder.setMessage(detalhe);
            builder.setPositiveButton("Fechar", (dialog, which) -> dialog.dismiss());
            builder.show();
        } else {
            // MODO RESPOSTA: Mostra a dúvida do cliente e a caixa para digitar a resposta
            builder.setTitle("Responder Agendamento #" + idAgendamento);

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 30, 50, 10);

            TextView tvPergunta = new TextView(this);
            tvPergunta.setText("Pergunta do cliente:\n\"" + (atendimento.getJustificativaCancelamento() != null ? atendimento.getJustificativaCancelamento() : "N/A") + "\"");
            tvPergunta.setTextSize(15);
            tvPergunta.setPadding(0, 0, 0, 30);
            layout.addView(tvPergunta);

            final EditText input = new EditText(this);
            input.setHint("Digite a resposta para o cliente...");
            layout.addView(input);

            builder.setView(layout);

            builder.setPositiveButton("Responder", (dialog, which) -> {
                String resposta = input.getText().toString().trim();
                if (!resposta.isEmpty()) {
                    responderAtendimento(atendimento, resposta);
                } else {
                    Toast.makeText(this, "A resposta não pode ser vazia", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    }

    private void responderAtendimento(Atendimento atendimento, String resposta) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
        String dataFormatada = sdf.format(new java.util.Date());

        Long idAgendamento = (atendimento.getAgendamento() != null && atendimento.getAgendamento().getIdAgendamento() != null)
                ? atendimento.getAgendamento().getIdAgendamento()
                : 1L;

        AtendimentoRequestDTO dto = new AtendimentoRequestDTO(
                dataFormatada,
                "RESPONDIDO",
                resposta,
                1L, // idColaborador
                idAgendamento
        );

        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.atualizarAtendimento(atendimento.getIdAtendimento(), dto).enqueue(new Callback<Atendimento>() {
            @Override
            public void onResponse(Call<Atendimento> call, Response<Atendimento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AtendimentosActivity.this, "Atendimento respondido com sucesso!", Toast.LENGTH_SHORT).show();
                    carregarAtendimentos();
                } else {
                    Toast.makeText(AtendimentosActivity.this, "Erro ao responder: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Atendimento> call, Throwable t) {
                Toast.makeText(AtendimentosActivity.this, "Falha de conexão ao responder", Toast.LENGTH_SHORT).show();
            }
        });
    }
}