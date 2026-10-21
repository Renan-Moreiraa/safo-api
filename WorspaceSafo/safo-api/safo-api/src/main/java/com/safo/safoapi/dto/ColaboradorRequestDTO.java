package com.safo.safoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ColaboradorRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 11)
    private String cpf;

    @Size(max = 30)
    private String especialidade;

    @Size(max = 50)
    private String horarioTrabalho;

    @NotBlank(message = "O status é obrigatório.")
    private String status;

    public ColaboradorRequestDTO() {}

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