package com.safo.safoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AtendimentoRequestDTO {

    @NotNull(message = "A data é obrigatória.")
    private LocalDate data;

    @NotBlank(message = "O status é obrigatório.")
    private String status;

    @Size(max = 200)
    private String justificativaCancelamento;

    @NotNull(message = "O ID do colaborador é obrigatório.")
    private Long idColaborador;

    @NotNull(message = "O ID do agendamento é obrigatório.")
    private Long idAgendamento;

    public AtendimentoRequestDTO() {}

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getJustificativaCancelamento() { return justificativaCancelamento; }
    public void setJustificativaCancelamento(String justificativaCancelamento) { this.justificativaCancelamento = justificativaCancelamento; }

    public Long getIdColaborador() { return idColaborador; }
    public void setIdColaborador(Long idColaborador) { this.idColaborador = idColaborador; }

    public Long getIdAgendamento() { return idAgendamento; }
    public void setIdAgendamento(Long idAgendamento) { this.idAgendamento = idAgendamento; }
}