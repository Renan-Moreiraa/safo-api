package com.safo.safoandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safo.safoandroid.model.Colaborador;

import java.util.ArrayList;
import java.util.List;

public class ColaboradorAdapter extends RecyclerView.Adapter<ColaboradorAdapter.ColaboradorViewHolder> {

    public interface OnColaboradorClickListener {
        void OnColaboradorClick(Colaborador colaborador);
        void OnColaboradorLongClick(Colaborador colaborador);
    }

    private List<Colaborador> colaboradores;
    private List<Colaborador> colaboradoresOriginal;
    private OnColaboradorClickListener listener;

    public ColaboradorAdapter(List<Colaborador> colaboradores, OnColaboradorClickListener listener) {
        this.colaboradores = colaboradores;
        this.colaboradoresOriginal = new ArrayList<>(colaboradores);
        this.listener = listener;
    }

    public void filtrar(String texto) {
        colaboradores.clear();
        if (texto.isEmpty()) {
            colaboradores.addAll(colaboradoresOriginal);
        } else {
            String busca = texto.toLowerCase().trim();
            for (Colaborador item : colaboradoresOriginal) {
                if (item.getNome().toLowerCase().contains(busca) || item.getCpf().contains(busca)) {
                    colaboradores.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColaboradorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_colaborador, parent, false);
        return new ColaboradorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColaboradorViewHolder holder, int position) {
        Colaborador colab = colaboradores.get(position);
        holder.textNome.setText(colab.getNome());
        holder.textEspecialidade.setText("Cargo: " + colab.getEspecialidade());
        holder.textCpfHorario.setText("CPF: " + colab.getCpf() + " | " + colab.getHorarioTrabalho());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.OnColaboradorClick(colab);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.OnColaboradorLongClick(colab);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return colaboradores != null ? colaboradores.size() : 0;
    }

    public static class ColaboradorViewHolder extends RecyclerView.ViewHolder {
        TextView textNome, textEspecialidade, textCpfHorario;

        public ColaboradorViewHolder(@NonNull View itemView) {
            super(itemView);
            textNome = itemView.findViewById(R.id.textNome);
            textEspecialidade = itemView.findViewById(R.id.textEspecialidade);
            textCpfHorario = itemView.findViewById(R.id.textCpfHorario);
        }
    }
}