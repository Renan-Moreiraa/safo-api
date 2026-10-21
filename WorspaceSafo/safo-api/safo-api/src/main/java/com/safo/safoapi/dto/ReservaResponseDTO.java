package com.safo.safoapi.dto;

import com.safo.safoapi.model.Reserva;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaResponseDTO {

    private Long idReserva;
    private LocalDate data;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private String status;
    private BigDecimal valor;
    private Long idCliente;
    private String nomeCliente;
    private Long idEspaco;
    private String nomeEspaco;

    public ReservaResponseDTO() {}

    public ReservaResponseDTO(Reserva reserva) {
        this.idReserva = reserva.getIdReserva();
        this.data = reserva.getData();
        this.horarioInicio = reserva.getHorarioInicio();
        this.horarioFim = reserva.getHorarioFim();
        this.status = reserva.getStatus();
        this.valor = reserva.getValor();
        if (reserva.getCliente() != null) {
            this.idCliente = reserva.getCliente().getIdCliente();
            this.nomeCliente = reserva.getCliente().getNome();
        }
        if (reserva.getEspaco() != null) {
            this.idEspaco = reserva.getEspaco().getIdEspaco();
            this.nomeEspaco = reserva.getEspaco().getNome();
        }
    }

    public Long getIdReserva() { return idReserva; }
    public LocalDate getData() { return data; }
    public LocalTime getHorarioInicio() { return horarioInicio; }
    public LocalTime getHorarioFim() { return horarioFim; }
    public String getStatus() { return status; }
    public BigDecimal getValor() { return valor; }
    public Long getIdCliente() { return idCliente; }
    public String getNomeCliente() { return nomeCliente; }
    public Long getIdEspaco() { return idEspaco; }
    public String getNomeEspaco() { return nomeEspaco; }
}