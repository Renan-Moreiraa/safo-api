package com.safo.safoandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safo.safoandroid.model.Agendamento;

import java.util.ArrayList;
import java.util.List;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.AgendamentoViewHolder> {

    public interface OnAgendamentoClickListener {
        void OnAgendamentoLongClick(Agendamento agendamento);
    }

    private List<Agendamento> agendamentos;
    private List<Agendamento> agendamentosOriginal;
    private OnAgendamentoClickListener listener;

    public AgendamentoAdapter(List<Agendamento> agendamentos, OnAgendamentoClickListener listener) {
        this.agendamentos = agendamentos;
        this.agendamentosOriginal = new ArrayList<>(agendamentos);
        this.listener = listener;
    }

    public void filtrar(String texto) {
        agendamentos.clear();
        if (texto.isEmpty()) {
            agendamentos.addAll(agendamentosOriginal);
        } else {
            String busca = texto.toLowerCase().trim();
            for (Agendamento item : agendamentosOriginal) {
                String nomeQuadra = item.getQuadra() != null ? item.getQuadra().getNome().toLowerCase() : "";
                if (item.getNomeCliente().toLowerCase().contains(busca) ||
                        item.getStatus().toLowerCase().contains(busca) ||
                        nomeQuadra.contains(busca)) {
                    agendamentos.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AgendamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agendamento, parent, false);
        return new AgendamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendamentoViewHolder holder, int position) {
        Agendamento ag = agendamentos.get(position);

        String nomeQuadra = ag.getQuadra() != null ? ag.getQuadra().getNome() : "Quadra não definida";
        holder.textClienteQuadra.setText("Cliente: " + ag.getNomeCliente() + " (" + nomeQuadra + ")");
        holder.textHorario.setText("Início: " + ag.getDataHoraInicio() + " | Fim: " + ag.getDataHoraFim());
        holder.textStatusAgendamento.setText("Status: " + ag.getStatus());

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.OnAgendamentoLongClick(ag);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return agendamentos != null ? agendamentos.size() : 0;
    }

    public static class AgendamentoViewHolder extends RecyclerView.ViewHolder {
        TextView textClienteQuadra, textHorario, textStatusAgendamento;

        public AgendamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            textClienteQuadra = itemView.findViewById(R.id.textClienteQuadra);
            textHorario = itemView.findViewById(R.id.textHorario);
            textStatusAgendamento = itemView.findViewById(R.id.textStatusAgendamento);
        }
    }
}