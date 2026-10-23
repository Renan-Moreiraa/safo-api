package com.safo.safoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.safo.safoandroid.model.Agendamento;
import com.safo.safoandroid.network.RetrofitClient;
import com.safo.safoandroid.network.SafoApiService;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendamentosActivity extends AppCompatActivity {

    private static final String TAG = "API_AGENDAMENTOS";
    private RecyclerView recyclerView;
    private AgendamentoAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agendamentos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainAgendamentos), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewAgendamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefresh = findViewById(R.id.swipeRefreshAgendamentos);
        swipeRefresh.setOnRefreshListener(this::buscarAgendamentos);

        searchView = findViewById(R.id.searchViewAgendamentos);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.filtrar(newText);
                }
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabAdicionarAgendamento);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(AgendamentosActivity.this, CadastroAgendamentoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscarAgendamentos();
    }

    private void buscarAgendamentos() {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);

        apiService.getAgendamentos().enqueue(new Callback<List<Agendamento>>() {
            @Override
            public void onResponse(Call<List<Agendamento>> call, Response<List<Agendamento>> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Agendamento> lista = response.body();
                    Log.d(TAG, "Sucesso! Recebidos: " + lista.size() + " agendamentos.");

                    adapter = new AgendamentoAdapter(lista, agendamento -> gerenciarAgendamento(agendamento));

                    // Verifica se foi aberto por um cliente para ajustar a exibição dos botões e o filtro
                    String clienteFiltro = getIntent().getStringExtra("nomeCliente");
                    if (clienteFiltro != null && !clienteFiltro.isEmpty()) {
                        adapter.setModoCliente(true);
                        adapter.filtrar(clienteFiltro);
                    }

                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e(TAG, "Erro HTTP: " + response.code());
                    Toast.makeText(AgendamentosActivity.this, "Erro na API: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Agendamento>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.e(TAG, "Falha de conexão: " + t.getMessage(), t);
                Toast.makeText(AgendamentosActivity.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void gerenciarAgendamento(Agendamento agendamento) {
        String clienteFiltro = getIntent().getStringExtra("nomeCliente");

        // Se for acesso do Cliente, ele só pode solicitar exclusão/cancelamento
        if (clienteFiltro != null && !clienteFiltro.isEmpty()) {
            confirmarExclusao(agendamento);
            return;
        }

        // Visão do Administrador: Gerenciar Status e Eliminar
        String[] opcoes = {"Aprovar Reserva (CONFIRMADO)", "Recusar Reserva (CANCELADO)", "Eliminar Agendamento"};

        new AlertDialog.Builder(this)
                .setTitle("Gerir Reserva de " + agendamento.getNomeCliente())
                .setItems(opcoes, (dialog, which) -> {
                    if (which == 0) {
                        atualizarStatusAPI(agendamento, "CONFIRMADO");
                    } else if (which == 1) {
                        atualizarStatusAPI(agendamento, "CANCELADO");
                    } else if (which == 2) {
                        confirmarExclusao(agendamento);
                    }
                })
                .show();
    }

    private void atualizarStatusAPI(Agendamento agendamento, String novoStatus) {
        agendamento.setStatus(novoStatus);
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);

        apiService.atualizarAgendamento(agendamento.getIdAgendamento(), agendamento).enqueue(new Callback<Agendamento>() {
            @Override
            public void onResponse(Call<Agendamento> call, Response<Agendamento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AgendamentosActivity.this, "Status atualizado para " + novoStatus, Toast.LENGTH_SHORT).show();
                    buscarAgendamentos();
                } else {
                    Toast.makeText(AgendamentosActivity.this, "Erro ao atualizar status: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Agendamento> call, Throwable t) {
                Toast.makeText(AgendamentosActivity.this, "Falha de conexão ao atualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmarExclusao(Agendamento agendamento) {
        new AlertDialog.Builder(this)
                .setTitle("Remover Agendamento")
                .setMessage("Deseja realmente remover o agendamento de " + agendamento.getNomeCliente() + "?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    deletarAgendamentoAPI(agendamento.getIdAgendamento());
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void deletarAgendamentoAPI(Long id) {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.deletarAgendamento(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AgendamentosActivity.this, "Agendamento removido!", Toast.LENGTH_SHORT).show();
                    buscarAgendamentos();
                } else {
                    Toast.makeText(AgendamentosActivity.this, "Erro ao excluir: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AgendamentosActivity.this, "Falha de conexão ao excluir", Toast.LENGTH_SHORT).show();
            }
        });
    }
}