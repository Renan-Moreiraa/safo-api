package com.safo.safoandroid.model;

import com.google.gson.annotations.SerializedName;

public class Atendimento {

    @SerializedName("idAtendimento")
    private Long idAtendimento;

    @SerializedName("data")
    private String data;

    @SerializedName("status")
    private String status;

    @SerializedName("justificativaCancelamento")
    private String justificativaCancelamento;

    @SerializedName("colaborador")
    private Colaborador colaborador;

    @SerializedName("agendamento")
    private Agendamento agendamento;

    @SerializedName("resposta")
    private String resposta;

    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }

    public Atendimento() {}

    public Long getIdAtendimento() { return idAtendimento; }
    public void setIdAtendimento(Long idAtendimento) { this.idAtendimento = idAtendimento; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getJustificativaCancelamento() { return justificativaCancelamento; }
    public void setJustificativaCancelamento(String justificativaCancelamento) { this.justificativaCancelamento = justificativaCancelamento; }

    public Colaborador getColaborador() { return colaborador; }
    public void setColaborador(Colaborador colaborador) { this.colaborador = colaborador; }

    public Agendamento getAgendamento() { return agendamento; }
    public void setAgendamento(Agendamento agendamento) { this.agendamento = agendamento; }
}