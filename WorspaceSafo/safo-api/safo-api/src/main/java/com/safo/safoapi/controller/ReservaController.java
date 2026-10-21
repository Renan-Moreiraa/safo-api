package com.safo.safoapi.controller;

import com.safo.safoapi.dto.ReservaRequestDTO;
import com.safo.safoapi.dto.ReservaResponseDTO;
import com.safo.safoapi.model.Cliente;
import com.safo.safoapi.model.Espaco;
import com.safo.safoapi.model.Reserva;
import com.safo.safoapi.repository.ClienteRepository;
import com.safo.safoapi.repository.EspacoRepository;
import com.safo.safoapi.repository.ReservaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    @GetMapping
    public List<ReservaResponseDTO> listarTodas() {
        return reservaRepository.findAll()
                .stream()
                .map(ReservaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(@PathVariable Long id) {
        return reservaRepository.findById(id)
                .map(reserva -> ResponseEntity.ok(new ReservaResponseDTO(reserva)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody ReservaRequestDTO dto) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(dto.getIdCliente());
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Cliente não encontrado com o ID fornecido.");
        }

        Optional<Espaco> espacoOpt = espacoRepository.findById(dto.getIdEspaco());
        if (espacoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Espaço não encontrado com o ID fornecido.");
        }

        List<Reserva> conflitos = reservaRepository.buscarConflitos(
                dto.getIdEspaco(),
                dto.getData(),
                dto.getHorarioInicio(),
                dto.getHorarioFim()
        );

        if (!conflitos.isEmpty()) {
            return ResponseEntity.badRequest().body("Horário indisponível para o espaço selecionado.");
        }

        Reserva reserva = new Reserva();
        reserva.setData(dto.getData());
        reserva.setHorarioInicio(dto.getHorarioInicio());
        reserva.setHorarioFim(dto.getHorarioFim());
        reserva.setStatus(dto.getStatus());
        reserva.setValor(dto.getValor());
        reserva.setCliente(clienteOpt.get());
        reserva.setEspaco(espacoOpt.get());

        Reserva salva = reservaRepository.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReservaResponseDTO(salva));
    }
}