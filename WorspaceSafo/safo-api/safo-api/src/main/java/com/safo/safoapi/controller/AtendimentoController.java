package com.safo.safoapi.controller;

import com.safo.safoapi.dto.AtendimentoRequestDTO;
import com.safo.safoapi.dto.AtendimentoResponseDTO;
import com.safo.safoapi.service.AtendimentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {

    @Autowired
    private AtendimentoService atendimentoService;

    @GetMapping
    public List<AtendimentoResponseDTO> listarTodos() {
        return atendimentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return atendimentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AtendimentoResponseDTO> criar(@Valid @RequestBody AtendimentoRequestDTO dto) {
        AtendimentoResponseDTO criado = atendimentoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
}