package com.safo.safoapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "RESERVAS")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @NotNull(message = "A data da reserva é obrigatória.")
    @Column(nullable = false)
    private LocalDate data;

    @NotNull(message = "O horário de início é obrigatório.")
    @Column(name = "horario_inicio", nullable = false)
    private LocalTime horarioInicio;

    @NotNull(message = "O horário de fim é obrigatório.")
    @Column(name = "horario_fim", nullable = false)
    private LocalTime horarioFim;

    @NotBlank(message = "O status da reserva é obrigatório.")
    @Size(max = 20, message = "O status deve ter no máximo 20 caracteres.")
    @Column(nullable = false, length = 20)
    private String status;

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.0", message = "O valor não pode ser negativo.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "O cliente é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @NotNull(message = "O espaço é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "id_espaco", nullable = false)
    private Espaco espaco;

    public Reserva() {}

    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }

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

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Espaco getEspaco() { return espaco; }
    public void setEspaco(Espaco espaco) { this.espaco = espaco; }
}