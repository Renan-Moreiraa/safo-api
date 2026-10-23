package com.safo.safoandroid.model;

import com.google.gson.annotations.SerializedName;

public class AtendimentoRequestDTO {

    @SerializedName("data")
    private String data;

    @SerializedName("status")
    private String status;

    @SerializedName("justificativaCancelamento")
    private String justificativaCancelamento;

    @SerializedName("idColaborador")
    private Long idColaborador;

    @SerializedName("idAgendamento")
    private Long idAgendamento;

    public AtendimentoRequestDTO(String data, String status, String justificativaCancelamento, Long idColaborador, Long idAgendamento) {
        this.data = data;
        this.status = status;
        this.justificativaCancelamento = justificativaCancelamento;
        this.idColaborador = idColaborador;
        this.idAgendamento = idAgendamento;
    }

    // Getters e Setters
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getJustificativaCancelamento() { return justificativaCancelamento; }
    public void setJustificativaCancelamento(String justificativaCancelamento) { this.justificativaCancelamento = justificativaCancelamento; }
    public Long getIdColaborador() { return idColaborador; }
    public void setIdColaborador(Long idColaborador) { this.idColaborador = idColaborador; }
    public Long getIdAgendamento() { return idAgendamento; }
    public void setIdAgendamento(Long idAgendamento) { this.idAgendamento = idAgendamento; }
}