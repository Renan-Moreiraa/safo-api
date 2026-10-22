package com.safo.safoandroid.model;

import java.math.BigDecimal;

public class Quadra {

    private Long idQuadra;
    private String nome;
    private String tipoEsporte;
    private BigDecimal precoHora;
    private String status;

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