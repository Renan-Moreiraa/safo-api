package com.safo.safoapi.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "QUADRAS")
public class Quadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_quadra")
    private Long idQuadra;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "tipo_esporte", nullable = false)
    private String tipoEsporte;

    @Column(name = "preco_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoHora;

    @Column(name = "status", nullable = false)
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