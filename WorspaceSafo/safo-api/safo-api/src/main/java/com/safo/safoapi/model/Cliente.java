package com.safo.safoapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "CLIENTES")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    // --- ANOTAÇÕES DO NOME ---
    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    // --- ANOTAÇÕES DO CPF ---
    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 dígitos.")
    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    // --- ANOTAÇÃO DO TELEFONE ---
    @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres.")
    @Column(length = 15)
    private String telefone;

    // --- ANOTAÇÕES DO E-MAIL ---
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Insira um e-mail válido.")
    @Size(max = 50, message = "O e-mail deve ter no máximo 50 caracteres.")
    @Column(nullable = false, length = 50, unique = true)
    private String email;

    // --- ANOTAÇÃO DO STATUS ---
    @NotBlank(message = "O status é obrigatório.")
    @Column(nullable = false, length = 10)
    private String status;

    public Cliente() {}

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}