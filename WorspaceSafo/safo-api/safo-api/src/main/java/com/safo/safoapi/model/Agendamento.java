package com.safo.safoapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AGENDAMENTOS")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Long idAgendamento;

    @ManyToOne
    @JoinColumn(name = "id_quadra", nullable = false)
    private Quadra quadra;

    @Column(name = "nome_cliente", nullable = false)
    private String nomeCliente;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(name = "status", nullable = false)
    private String status; // ex: CONFIRMADO, CANCELADO, PENDENTE

    public Agendamento() {}

    public Long getIdAgendamento() { return idAgendamento; }
    public void setIdAgendamento(Long idAgendamento) { this.idAgendamento = idAgendamento; }

    public Quadra getQuadra() { return quadra; }
    public void setQuadra(Quadra quadra) { this.quadra = quadra; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) { this.dataHoraInicio = dataHoraInicio; }

    public LocalDateTime getDataHoraFim() { return dataHoraFim; }
    public void setDataHoraFim(LocalDateTime dataHoraFim) { this.dataHoraFim = dataHoraFim; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}