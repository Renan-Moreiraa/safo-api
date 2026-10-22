package com.safo.safoandroid.model;

public class Colaborador {
    private Long idColaborador;
    private String nome;
    private String cpf;
    private String especialidade;
    private String horarioTrabalho;
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