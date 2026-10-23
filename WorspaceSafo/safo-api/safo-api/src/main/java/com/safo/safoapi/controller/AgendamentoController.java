package com.safo.safoapi.controller;

import com.safo.safoapi.model.Agendamento;
import com.safo.safoapi.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @GetMapping
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Agendamento agendamento) {
        if (agendamento.getQuadra() == null || agendamento.getQuadra().getIdQuadra() == null) {
            return ResponseEntity.badRequest().body("Quadra não informada.");
        }

        boolean existeConflito = agendamentoRepository.existeSobreposicao(
                agendamento.getQuadra().getIdQuadra(),
                agendamento.getDataHoraInicio(),
                agendamento.getDataHoraFim()
        );

        if (existeConflito) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe uma reserva para esta quadra neste horário.");
        }

        Agendamento salvo = agendamentoRepository.save(agendamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizar(@PathVariable Long id, @RequestBody Agendamento agendamentoAtualizado) {
        Optional<Agendamento> op = agendamentoRepository.findById(id);
        if (op.isPresent()) {
            Agendamento agendamento = op.get();
            agendamento.setStatus(agendamentoAtualizado.getStatus());
            if (agendamentoAtualizado.getDataHoraInicio() != null) {
                agendamento.setDataHoraInicio(agendamentoAtualizado.getDataHoraInicio());
            }
            if (agendamentoAtualizado.getDataHoraFim() != null) {
                agendamento.setDataHoraFim(agendamentoAtualizado.getDataHoraFim());
            }
            Agendamento salvo = agendamentoRepository.save(agendamento);
            return ResponseEntity.ok(salvo);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (agendamentoRepository.existsById(id)) {
            agendamentoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}