package com.safo.safoapi.controller;

import com.safo.safoapi.dto.AtendimentoRequestDTO;
import com.safo.safoapi.dto.AtendimentoResponseDTO;
import com.safo.safoapi.model.Atendimento;
import com.safo.safoapi.model.Colaborador;
import com.safo.safoapi.model.Reserva;
import com.safo.safoapi.repository.AtendimentoRepository;
import com.safo.safoapi.repository.ColaboradorRepository;
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
@RequestMapping("/atendimentos")
public class AtendimentoController {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping
    public List<AtendimentoResponseDTO> listarTodos() {
        return atendimentoRepository.findAll()
                .stream()
                .map(AtendimentoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return atendimentoRepository.findById(id)
                .map(atendimento -> ResponseEntity.ok(new AtendimentoResponseDTO(atendimento)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody AtendimentoRequestDTO dto) {
        Optional<Colaborador> colaboradorOpt = colaboradorRepository.findById(dto.getIdColaborador());
        if (colaboradorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Colaborador não encontrado com o ID fornecido.");
        }

        Optional<Reserva> reservaOpt = reservaRepository.findById(dto.getIdReserva());
        if (reservaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Reserva não encontrada com o ID fornecido.");
        }

        Atendimento atendimento = new Atendimento();
        atendimento.setData(dto.getData());
        atendimento.setStatus(dto.getStatus());
        atendimento.setJustificativaCancelamento(dto.getJustificativaCancelamento());
        atendimento.setColaborador(colaboradorOpt.get());
        atendimento.setReserva(reservaOpt.get());

        Atendimento salvo = atendimentoRepository.save(atendimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AtendimentoResponseDTO(salvo));
    }
}