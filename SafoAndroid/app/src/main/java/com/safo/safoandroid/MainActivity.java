package com.safo.safoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.safo.safoandroid.model.Colaborador;
import com.safo.safoandroid.network.RetrofitClient;
import com.safo.safoandroid.network.SafoApiService;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "API_TESTE";
    private RecyclerView recyclerView;
    private ColaboradorAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewColaboradores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this::buscarColaboradores);

        FloatingActionButton fab = findViewById(R.id.fabAdicionar);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscarColaboradores();
    }

    private void buscarColaboradores() {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);

        apiService.getColaboradores().enqueue(new Callback<List<Colaborador>>() {
            @Override
            public void onResponse(Call<List<Colaborador>> call, Response<List<Colaborador>> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Colaborador> lista = response.body();
                    Log.d(TAG, "Sucesso! Recebidos: " + lista.size() + " colaboradores.");

                    adapter = new ColaboradorAdapter(lista, colaborador -> confirmarExclusao(colaborador));
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e(TAG, "Erro HTTP: " + response.code());
                    Toast.makeText(MainActivity.this, "Erro na API: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Colaborador>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.e(TAG, "Falha de conexão: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void confirmarExclusao(Colaborador colaborador) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Colaborador")
                .setMessage("Deseja realmente excluir " + colaborador.getNome() + "?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    deletarColaboradorAPI(colaborador.getIdColaborador());
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void deletarColaboradorAPI(Long id) {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.deletarColaborador(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Colaborador removido!", Toast.LENGTH_SHORT).show();
                    buscarColaboradores();
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao excluir: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Falha de conexão ao excluir", Toast.LENGTH_SHORT).show();
            }
        });
    }
}