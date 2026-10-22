package com.safo.safoapi.service;

import com.safo.safoapi.dto.ReservaRequestDTO;
import com.safo.safoapi.dto.ReservaResponseDTO;
import com.safo.safoapi.model.Cliente;
import com.safo.safoapi.model.Espaco;
import com.safo.safoapi.model.Reserva;
import com.safo.safoapi.repository.ClienteRepository;
import com.safo.safoapi.repository.EspacoRepository;
import com.safo.safoapi.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    public List<ReservaResponseDTO> listarTodas() {
        return reservaRepository.findAll()
                .stream()
                .map(ReservaResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ReservaResponseDTO> buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .map(ReservaResponseDTO::new);
    }

    public ReservaResponseDTO criar(ReservaRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID fornecido."));

        Espaco espaco = espacoRepository.findById(dto.getIdEspaco())
                .orElseThrow(() -> new IllegalArgumentException("Espaço não encontrado com o ID fornecido."));

        List<Reserva> conflitos = reservaRepository.buscarConflitos(
                dto.getIdEspaco(),
                dto.getData(),
                dto.getHorarioInicio(),
                dto.getHorarioFim()
        );

        if (!conflitos.isEmpty()) {
            throw new IllegalStateException("Horário indisponível para o espaço selecionado.");
        }

        Reserva reserva = new Reserva();
        reserva.setData(dto.getData());
        reserva.setHorarioInicio(dto.getHorarioInicio());
        reserva.setHorarioFim(dto.getHorarioFim());
        reserva.setStatus(dto.getStatus());
        reserva.setValor(dto.getValor());
        reserva.setCliente(cliente);
        reserva.setEspaco(espaco);

        Reserva salva = reservaRepository.save(reserva);
        return new ReservaResponseDTO(salva);
    }
}