package com.safo.safoapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "COLABORADORES")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colaborador")
    private Long idColaborador;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 dígitos.")
    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @Size(max = 30, message = "A especialidade deve ter no máximo 30 caracteres.")
    @Column(length = 30)
    private String especialidade;

    @Size(max = 50, message = "O horário de trabalho deve ter no máximo 50 caracteres.")
    @Column(name = "horario_trabalho", length = 50)
    private String horarioTrabalho;

    @NotBlank(message = "O status é obrigatório.")
    @Size(max = 10, message = "O status deve ter no máximo 10 caracteres.")
    @Column(nullable = false, length = 10)
    private String status;

    public Colaborador() {}

    public Long getIdColaborador() { return idColaborador; }
    public void setIdColaborador(Long idColaborador) { this.idColaborador = idColaborador; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getHorarioTrabalho() { return horarioTrabalho; }
    public void setHorarioTrabalho(String horarioTrabalho) { this.horarioTrabalho = horarioTrabalho; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}