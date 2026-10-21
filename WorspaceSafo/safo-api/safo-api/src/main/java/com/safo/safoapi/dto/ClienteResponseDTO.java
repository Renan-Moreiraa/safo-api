package com.safo.safoapi.dto;

import com.safo.safoapi.model.Cliente;

public class ClienteResponseDTO {

    private Long idCliente;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String status;

    public ClienteResponseDTO() {}

    public ClienteResponseDTO(Cliente cliente) {
        this.idCliente = cliente.getIdCliente();
        this.nome = cliente.getNome();
        this.cpf = cliente.getCpf();
        this.telefone = cliente.getTelefone();
        this.email = cliente.getEmail();
        this.status = cliente.getStatus();
    }

    public Long getIdCliente() { return idCliente; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }
}