package com.safo.safoandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safo.safoandroid.model.Colaborador;

import java.util.List;

public class ColaboradorAdapter extends RecyclerView.Adapter<ColaboradorAdapter.ColaboradorViewHolder> {

    private List<Colaborador> colaboradores;

    // Construtor que recebe a lista
    public ColaboradorAdapter(List<Colaborador> colaboradores) {
        this.colaboradores = colaboradores;
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