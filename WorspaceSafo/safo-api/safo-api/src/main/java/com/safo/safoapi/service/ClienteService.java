package com.safo.safoapi.service;

import com.safo.safoapi.dto.ClienteRequestDTO;
import com.safo.safoapi.dto.ClienteResponseDTO;
import com.safo.safoapi.model.Cliente;
import com.safo.safoapi.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ClienteResponseDTO> buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .map(ClienteResponseDTO::new);
    }

    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());
        cliente.setStatus(dto.getStatus());

        Cliente salvo = clienteRepository.save(cliente);
        return new ClienteResponseDTO(salvo);
    }
}