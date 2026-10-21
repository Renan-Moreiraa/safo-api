package com.safo.safoapi.dto;

import com.safo.safoapi.model.Colaborador;

public class ColaboradorResponseDTO {

    private Long idColaborador;
    private String nome;
    private String cpf;
    private String especialidade;
    private String horarioTrabalho;
    private String status;

    public ColaboradorResponseDTO() {}

    public ColaboradorResponseDTO(Colaborador colaborador) {
        this.idColaborador = colaborador.getIdColaborador();
        this.nome = colaborador.getNome();
        this.cpf = colaborador.getCpf();
        this.especialidade = colaborador.getEspecialidade();
        this.horarioTrabalho = colaborador.getHorarioTrabalho();
        this.status = colaborador.getStatus();
    }

    public Long getIdColaborador() { return idColaborador; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEspecialidade() { return especialidade; }
    public String getHorarioTrabalho() { return horarioTrabalho; }
    public String getStatus() { return status; }
}