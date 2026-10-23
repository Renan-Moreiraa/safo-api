package com.safo.safoapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "ATENDIMENTOS")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atendimento")
    private Long idAtendimento;

    @NotNull(message = "A data do atendimento é obrigatória.")
    @Column(nullable = false)
    private LocalDate data;

    @NotBlank(message = "O status do atendimento é obrigatório.")
    @Size(max = 20, message = "O status deve ter no máximo 20 caracteres.")
    @Column(nullable = false, length = 20)
    private String status;

    @Size(max = 200, message = "A justificativa de cancelamento deve ter no máximo 200 caracteres.")
    @Column(name = "justificativa_cancelamento", length = 200)
    private String justificativaCancelamento;

    @NotNull(message = "O colaborador é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "id_colaborador", nullable = false)
    private Colaborador colaborador;

    @NotNull(message = "O agendamento é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "id_agendamento", nullable = false)
    private Agendamento agendamento;

    public Atendimento() {}

    public Long getIdAtendimento() { return idAtendimento; }
    public void setIdAtendimento(Long idAtendimento) { this.idAtendimento = idAtendimento; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getJustificativaCancelamento() { return justificativaCancelamento; }
    public void setJustificativaCancelamento(String justificativaCancelamento) { this.justificativaCancelamento = justificativaCancelamento; }

    public Colaborador getColaborador() { return colaborador; }
    public void setColaborador(Colaborador colaborador) { this.colaborador = colaborador; }

    public Agendamento getAgendamento() { return agendamento; }
    public void setAgendamento(Agendamento agendamento) { this.agendamento = agendamento; }
}