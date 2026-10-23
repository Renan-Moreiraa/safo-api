package com.safo.safoandroid.model;

public class Agendamento {

    private Long idAgendamento;
    private Quadra quadra;
    private String nomeCliente;
    private String dataHoraInicio;
    private String dataHoraFim;
    private String status;

    public Agendamento() {}

    public Long getIdAgendamento() { return idAgendamento; }
    public void setIdAgendamento(Long idAgendamento) { this.idAgendamento = idAgendamento; }

    public Quadra getQuadra() { return quadra; }
    public void setQuadra(Quadra quadra) { this.quadra = quadra; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public String getDataHoraInicio() { return dataHoraInicio; }
    public void setDataHoraInicio(String dataHoraInicio) { this.dataHoraInicio = dataHoraInicio; }

    public String getDataHoraFim() { return dataHoraFim; }
    public void setDataHoraFim(String dataHoraFim) { this.dataHoraFim = dataHoraFim; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}