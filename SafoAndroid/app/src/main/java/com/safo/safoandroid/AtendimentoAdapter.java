package com.safo.safoandroid;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safo.safoandroid.model.Atendimento;

import java.util.List;

public class AtendimentoAdapter extends RecyclerView.Adapter<AtendimentoAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Atendimento atendimento);
    }

    private List<Atendimento> lista;
    private OnItemClickListener listener;

    public AtendimentoAdapter(List<Atendimento> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_atendimento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Atendimento a = lista.get(position);

        // Exibe Agendamento em vez de Reserva
        if (a.getAgendamento() != null && a.getAgendamento().getNomeCliente() != null) {
            holder.tvReserva.setText("Agendamento: " + a.getAgendamento().getNomeCliente());
        } else if (a.getAgendamento() != null && a.getAgendamento().getIdAgendamento() != null) {
            holder.tvReserva.setText("Agendamento #" + a.getAgendamento().getIdAgendamento());
        } else {
            holder.tvReserva.setText("Agendamento #" + a.getIdAtendimento());
        }

        if (a.getColaborador() != null) {
            holder.tvColaborador.setText("Atendido por: " + a.getColaborador().getNome());
        } else {
            holder.tvColaborador.setText("Atendido por: Equipe SAFÓ");
        }

        holder.tvData.setText("Data: " + (a.getData() != null ? a.getData() : "N/A"));
        holder.tvStatus.setText("Status: " + a.getStatus());

        if ("REALIZADO".equalsIgnoreCase(a.getStatus()) || "CONCLUIDO".equalsIgnoreCase(a.getStatus()) || "RESPONDIDO".equalsIgnoreCase(a.getStatus())) {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800"));
        }

        // Lógica da caixinha (RESPONDIDA)
        if (a.getResposta() != null && !a.getResposta().trim().isEmpty()) {
            holder.tvRespondida.setVisibility(View.VISIBLE);
        } else {
            holder.tvRespondida.setVisibility(View.GONE);
        }

        // Clique no item para abrir a janela de resposta
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReserva, tvColaborador, tvData, tvStatus, tvRespondida;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReserva = itemView.findViewById(R.id.tvAtendimentoReserva);
            tvColaborador = itemView.findViewById(R.id.tvAtendimentoColaborador);
            tvData = itemView.findViewById(R.id.tvAtendimentoData);
            tvStatus = itemView.findViewById(R.id.tvAtendimentoStatus);
            tvRespondida = itemView.findViewById(R.id.tagRespondida); // Mapeamento da caixinha
        }
    }
}