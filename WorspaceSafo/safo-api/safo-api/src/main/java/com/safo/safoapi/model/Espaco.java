package com.safo.safoapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "ESPACOS")
public class Espaco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_espaco")
    private Long idEspaco;

    @NotBlank(message = "O nome do espaço é obrigatório.")
    @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres.")
    @Column(nullable = false, length = 50)
    private String nome;

    @NotNull(message = "A capacidade é obrigatória.")
    @Min(value = 1, message = "A capacidade deve ser de no mínimo 1 pessoa.")
    @Column(nullable = false)
    private Integer capacidade;

    @NotNull(message = "O valor base é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor base deve ser maior que zero.")
    @Column(name = "valor_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorBase;

    @NotBlank(message = "O status é obrigatório.")
    @Size(max = 20, message = "O status deve ter no máximo 20 caracteres.")
    @Column(nullable = false, length = 20)
    private String status;

    public Espaco() {}

    public Long getIdEspaco() { return idEspaco; }
    public void setIdEspaco(Long idEspaco) { this.idEspaco = idEspaco; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }

    public BigDecimal getValorBase() { return valorBase; }
    public void setValorBase(BigDecimal valorBase) { this.valorBase = valorBase; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}