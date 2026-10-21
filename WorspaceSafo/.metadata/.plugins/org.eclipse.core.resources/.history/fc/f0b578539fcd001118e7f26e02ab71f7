package com.safo.safoapi.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class EspacoRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 50)
    private String nome;

    @NotNull(message = "A capacidade é obrigatória.")
    @Min(value = 1, message = "A capacidade deve ser de no mínimo 1 pessoa.")
    private Integer capacidade;

    @NotNull(message = "O valor base é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor base deve ser maior que zero.")
    private BigDecimal valorBase;

    @NotBlank(message = "O status é obrigatório.")
    private String status;

    public EspacoRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }

    public BigDecimal getValorBase() { return valorBase; }
    public void setValorBase(BigDecimal valorBase) { this.valorBase = valorBase; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}