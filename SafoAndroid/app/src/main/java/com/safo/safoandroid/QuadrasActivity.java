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
import com.safo.safoandroid.model.Quadra;
import com.safo.safoandroid.network.RetrofitClient;
import com.safo.safoandroid.network.SafoApiService;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuadrasActivity extends AppCompatActivity {

    private static final String TAG = "API_QUADRAS";
    private RecyclerView recyclerView;
    private QuadraAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quadras);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainQuadras), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewQuadras);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefresh = findViewById(R.id.swipeRefreshQuadras);
        swipeRefresh.setOnRefreshListener(this::buscarQuadras);

        searchView = findViewById(R.id.searchViewQuadras);
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

        FloatingActionButton fab = findViewById(R.id.fabAdicionarQuadra);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(QuadrasActivity.this, CadastroQuadraActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscarQuadras();
    }

    private void buscarQuadras() {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);

        apiService.getQuadras().enqueue(new Callback<List<Quadra>>() {
            @Override
            public void onResponse(Call<List<Quadra>> call, Response<List<Quadra>> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Quadra> lista = response.body();
                    Log.d(TAG, "Sucesso! Recebidas: " + lista.size() + " quadras.");

                    adapter = new QuadraAdapter(lista, new QuadraAdapter.OnQuadraClickListener() {
                        @Override
                        public void OnQuadraClick(Quadra quadra) {
                            Intent intent = new Intent(QuadrasActivity.this, CadastroQuadraActivity.class);
                            intent.putExtra("id", quadra.getIdQuadra());
                            intent.putExtra("nome", quadra.getNome());
                            intent.putExtra("tipoEsporte", quadra.getTipoEsporte());
                            intent.putExtra("precoHora", String.valueOf(quadra.getPrecoHora()));
                            intent.putExtra("status", quadra.getStatus());
                            startActivity(intent);
                        }

                        @Override
                        public void OnQuadraLongClick(Quadra quadra) {
                            confirmarExclusao(quadra);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e(TAG, "Erro HTTP: " + response.code());
                    Toast.makeText(QuadrasActivity.this, "Erro na API: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Quadra>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.e(TAG, "Falha de conexão: " + t.getMessage(), t);
                Toast.makeText(QuadrasActivity.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void confirmarExclusao(Quadra quadra) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Quadra")
                .setMessage("Deseja realmente excluir a quadra " + quadra.getNome() + "?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    deletarQuadraAPI(quadra.getIdQuadra());
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void deletarQuadraAPI(Long id) {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.deletarQuadra(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuadrasActivity.this, "Quadra removida!", Toast.LENGTH_SHORT).show();
                    buscarQuadras();
                } else {
                    Toast.makeText(QuadrasActivity.this, "Erro ao excluir: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(QuadrasActivity.this, "Falha de conexão ao excluir", Toast.LENGTH_SHORT).show();
            }
        });
    }
}