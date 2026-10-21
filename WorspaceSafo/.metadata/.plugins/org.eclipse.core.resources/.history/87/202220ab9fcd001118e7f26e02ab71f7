package com.safo.safoapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaRequestDTO {

    @NotNull(message = "A data é obrigatória.")
    private LocalDate data;

    @NotNull(message = "O horário de início é obrigatório.")
    private LocalTime horarioInicio;

    @NotNull(message = "O horário de fim é obrigatório.")
    private LocalTime horarioFim;

    @NotBlank(message = "O status é obrigatório.")
    private String status;

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.0", message = "O valor não pode ser negativo.")
    private BigDecimal valor;

    @NotNull(message = "O ID do cliente é obrigatório.")
    private Long idCliente;

    @NotNull(message = "O ID do espaço é obrigatório.")
    private Long idEspaco;

    public ReservaRequestDTO() {}

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }

    public LocalTime getHorarioFim() { return horarioFim; }
    public void setHorarioFim(LocalTime horarioFim) { this.horarioFim = horarioFim; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public Long getIdEspaco() { return idEspaco; }
    public void setIdEspaco(Long idEspaco) { this.idEspaco = idEspaco; }
}