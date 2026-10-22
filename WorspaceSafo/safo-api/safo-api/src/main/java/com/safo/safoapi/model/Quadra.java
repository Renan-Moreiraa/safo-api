package com.safo.safoapi.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "quadras")
public class Quadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuadra;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tipoEsporte; // ex: Futsal, Society, Tênis, Volley

    @Column(nullable = false)
    private BigDecimal precoHora;

    @Column(nullable = false)
    private String status; // ex: DISPONIVEL, MANUTENCAO

    public Quadra() {}

    public Long getIdQuadra() { return idQuadra; }
    public void setIdQuadra(Long idQuadra) { this.idQuadra = idQuadra; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipoEsporte() { return tipoEsporte; }
    public void setTipoEsporte(String tipoEsporte) { this.tipoEsporte = tipoEsporte; }

    public BigDecimal getPrecoHora() { return precoHora; }
    public void setPrecoHora(BigDecimal precoHora) { this.precoHora = precoHora; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}