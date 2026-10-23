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

                    adapter = new AgendamentoAdapter(lista, agendamento -> confirmarExclusao(agendamento));
                    recyclerView.setAdapter(adapter);

                    // Se a tela foi aberta por um Cliente, filtra automaticamente as reservas dele
                    String clienteFiltro = getIntent().getStringExtra("nomeCliente");
                    if (clienteFiltro != null && !clienteFiltro.isEmpty()) {
                        adapter.filtrar(clienteFiltro);
                    }
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

    private void confirmarExclusao(Agendamento agendamento) {
        new AlertDialog.Builder(this)
                .setTitle("Cancelar Agendamento")
                .setMessage("Deseja realmente cancelar o agendamento de " + agendamento.getNomeCliente() + "?")
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