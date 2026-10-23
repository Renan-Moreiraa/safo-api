package com.safo.safoandroid;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safo.safoandroid.model.Agendamento;
import com.safo.safoandroid.network.RetrofitClient;
import com.safo.safoandroid.network.SafoApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.AgendamentoViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Agendamento agendamento);
    }

    private List<Agendamento> listaOriginal;
    private List<Agendamento> listaExibida;
    private OnItemClickListener listener;
    private Context context;

    public AgendamentoAdapter(List<Agendamento> agendamentos, OnItemClickListener listener) {
        this.listaOriginal = new ArrayList<>(agendamentos);
        this.listaExibida = new ArrayList<>(agendamentos);
        this.listener = listener;
    }

    @NonNull
    @Override
    public AgendamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_agendamento_admin, parent, false);
        return new AgendamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendamentoViewHolder holder, int position) {
        Agendamento agendamento = listaExibida.get(position);

        holder.tvClienteNome.setText("Cliente: " + agendamento.getNomeCliente());
        if (agendamento.getQuadra() != null) {
            holder.tvQuadraNome.setText("Quadra: " + agendamento.getQuadra().getNome());
        }
        holder.tvHorario.setText("Início: " + agendamento.getDataHoraInicio());
        holder.tvStatus.setText("Status: " + agendamento.getStatus());

        if ("CONFIRMADO".equalsIgnoreCase(agendamento.getStatus())) {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
        } else if ("CANCELADO".equalsIgnoreCase(agendamento.getStatus())) {
            holder.tvStatus.setTextColor(Color.parseColor("#F44336"));
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800"));
        }

        // Ações diretas nos botões Aprovar e Recusar
        holder.btnAprovar.setOnClickListener(v -> atualizarStatusAPI(agendamento, "CONFIRMADO", position));
        holder.btnRecusar.setOnClickListener(v -> atualizarStatusAPI(agendamento, "CANCELADO", position));

        // Clique no card abre as opções extras (ex: deletar)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(agendamento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaExibida != null ? listaExibida.size() : 0;
    }

    private void atualizarStatusAPI(Agendamento agendamento, String novoStatus, int position) {
        agendamento.setStatus(novoStatus);
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);

        apiService.atualizarAgendamento(agendamento.getIdAgendamento(), agendamento).enqueue(new Callback<Agendamento>() {
            @Override
            public void onResponse(Call<Agendamento> call, Response<Agendamento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Status atualizado para " + novoStatus, Toast.LENGTH_SHORT).show();
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(context, "Erro ao atualizar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Agendamento> call, Throwable t) {
                Toast.makeText(context, "Falha de conexão ao atualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void filtrar(String texto) {
        listaExibida.clear();
        if (texto == null || texto.trim().isEmpty()) {
            listaExibida.addAll(listaOriginal);
        } else {
            String busca = texto.toLowerCase().trim();
            for (Agendamento item : listaOriginal) {
                boolean bateCliente = item.getNomeCliente() != null && item.getNomeCliente().toLowerCase().contains(busca);
                boolean bateStatus = item.getStatus() != null && item.getStatus().toLowerCase().contains(busca);
                boolean bateQuadra = item.getQuadra() != null && item.getQuadra().getNome() != null && item.getQuadra().getNome().toLowerCase().contains(busca);

                if (bateCliente || bateStatus || bateQuadra) {
                    listaExibida.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class AgendamentoViewHolder extends RecyclerView.ViewHolder {
        TextView tvClienteNome, tvQuadraNome, tvHorario, tvStatus;
        Button btnAprovar, btnRecusar;

        public AgendamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClienteNome = itemView.findViewById(R.id.tvClienteNome);
            tvQuadraNome = itemView.findViewById(R.id.tvQuadraNome);
            tvHorario = itemView.findViewById(R.id.tvHorario);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnAprovar = itemView.findViewById(R.id.btnAprovar);
            btnRecusar = itemView.findViewById(R.id.btnRecusar);
        }
    }
}