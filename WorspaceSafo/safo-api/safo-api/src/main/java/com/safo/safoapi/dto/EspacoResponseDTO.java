package com.safo.safoapi.dto;

import com.safo.safoapi.model.Espaco;
import java.math.BigDecimal;

public class EspacoResponseDTO {

    private Long idEspaco;
    private String nome;
    private Integer capacidade;
    private BigDecimal valorBase;
    private String status;

    public EspacoResponseDTO() {}

    public EspacoResponseDTO(Espaco espaco) {
        this.idEspaco = espaco.getIdEspaco();
        this.nome = espaco.getNome();
        this.capacidade = espaco.getCapacidade();
        this.valorBase = espaco.getValorBase();
        this.status = espaco.getStatus();
    }

    public Long getIdEspaco() { return idEspaco; }
    public String getNome() { return nome; }
    public Integer getCapacidade() { return capacidade; }
    public BigDecimal getValorBase() { return valorBase; }
    public String getStatus() { return status; }
}