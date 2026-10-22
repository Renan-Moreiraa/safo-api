package com.safo.safoandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safo.safoandroid.model.Quadra;

import java.util.ArrayList;
import java.util.List;

public class QuadraAdapter extends RecyclerView.Adapter<QuadraAdapter.QuadraViewHolder> {

    public interface OnQuadraClickListener {
        void OnQuadraClick(Quadra quadra);
        void OnQuadraLongClick(Quadra quadra);
    }

    private List<Quadra> quadras;
    private List<Quadra> quadrasOriginal;
    private OnQuadraClickListener listener;

    public QuadraAdapter(List<Quadra> quadras, OnQuadraClickListener listener) {
        this.quadras = quadras;
        this.quadrasOriginal = new ArrayList<>(quadras);
        this.listener = listener;
    }

    public void filtrar(String texto) {
        quadras.clear();
        if (texto.isEmpty()) {
            quadras.addAll(quadrasOriginal);
        } else {
            String busca = texto.toLowerCase().trim();
            for (Quadra item : quadrasOriginal) {
                if (item.getNome().toLowerCase().contains(busca) ||
                        item.getTipoEsporte().toLowerCase().contains(busca)) {
                    quadras.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuadraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quadra, parent, false);
        return new QuadraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuadraViewHolder holder, int position) {
        Quadra quadra = quadras.get(position);
        holder.textNomeQuadra.setText(quadra.getNome());
        holder.textTipoEsporte.setText("Esporte: " + quadra.getTipoEsporte());
        holder.textPrecoStatus.setText("R$ " + quadra.getPrecoHora() + "/h | Status: " + quadra.getStatus());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.OnQuadraClick(quadra);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.OnQuadraLongClick(quadra);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return quadras != null ? quadras.size() : 0;
    }

    public static class QuadraViewHolder extends RecyclerView.ViewHolder {
        TextView textNomeQuadra, textTipoEsporte, textPrecoStatus;

        public QuadraViewHolder(@NonNull View itemView) {
            super(itemView);
            textNomeQuadra = itemView.findViewById(R.id.textNomeQuadra);
            textTipoEsporte = itemView.findViewById(R.id.textTipoEsporte);
            textPrecoStatus = itemView.findViewById(R.id.textPrecoStatus);
        }
    }
}