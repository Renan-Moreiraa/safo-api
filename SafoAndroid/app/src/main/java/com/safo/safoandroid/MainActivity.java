package com.safo.safoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        // Configura o RecyclerView na tela
        recyclerView = findViewById(R.id.recyclerViewColaboradores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configura a ação de clique no botão flutuante (+)
        FloatingActionButton fab = findViewById(R.id.fabAdicionar);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega a lista do MySQL sempre que a tela principal voltar a ficar visível
        buscarColaboradores();
    }

    private void buscarColaboradores() {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);

        apiService.getColaboradores().enqueue(new Callback<List<Colaborador>>() {
            @Override
            public void onResponse(Call<List<Colaborador>> call, Response<List<Colaborador>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Colaborador> lista = response.body();
                    Log.d(TAG, "Sucesso! Recebidos: " + lista.size() + " colaboradores.");

                    // Alimenta o RecyclerView com os dados retornados
                    adapter = new ColaboradorAdapter(lista);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e(TAG, "Erro HTTP: " + response.code());
                    Toast.makeText(MainActivity.this, "Erro na API: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Colaborador>> call, Throwable t) {
                Log.e(TAG, "Falha de conexão: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}