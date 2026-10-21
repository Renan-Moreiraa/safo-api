package com.safo.safoapi.dto;

import com.safo.safoapi.model.Atendimento;
import java.time.LocalDate;

public class AtendimentoResponseDTO {

    private Long idAtendimento;
    private LocalDate data;
    private String status;
    private String justificativaCancelamento;
    private Long idColaborador;
    private String nomeColaborador;
    private Long idReserva;

    public AtendimentoResponseDTO() {}

    public AtendimentoResponseDTO(Atendimento atendimento) {
        this.idAtendimento = atendimento.getIdAtendimento();
        this.data = atendimento.getData();
        this.status = atendimento.getStatus();
        this.justificativaCancelamento = atendimento.getJustificativaCancelamento();
        if (atendimento.getColaborador() != null) {
            this.idColaborador = atendimento.getColaborador().getIdColaborador();
            this.nomeColaborador = atendimento.getColaborador().getNome();
        }
        if (atendimento.getReserva() != null) {
            this.idReserva = atendimento.getReserva().getIdReserva();
        }
    }

    public Long getIdAtendimento() { return idAtendimento; }
    public LocalDate getData() { return data; }
    public String getStatus() { return status; }
    public String getJustificativaCancelamento() { return justificativaCancelamento; }
    public Long getIdColaborador() { return idColaborador; }
    public String getNomeColaborador() { return nomeColaborador; }
    public Long getIdReserva() { return idReserva; }
}