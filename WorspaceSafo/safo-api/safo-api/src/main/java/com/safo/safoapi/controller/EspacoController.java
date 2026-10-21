package com.safo.safoapi.controller;

import com.safo.safoapi.dto.EspacoRequestDTO;
import com.safo.safoapi.dto.EspacoResponseDTO;
import com.safo.safoapi.model.Espaco;
import com.safo.safoapi.repository.EspacoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/espacos")
public class EspacoController {

    @Autowired
    private EspacoRepository espacoRepository;

    @GetMapping
    public List<EspacoResponseDTO> listarTodos() {
        return espacoRepository.findAll()
                .stream()
                .map(EspacoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspacoResponseDTO> buscarPorId(@PathVariable Long id) {
        return espacoRepository.findById(id)
                .map(espaco -> ResponseEntity.ok(new EspacoResponseDTO(espaco)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EspacoResponseDTO> criar(@Valid @RequestBody EspacoRequestDTO dto) {
        Espaco espaco = new Espaco();
        espaco.setNome(dto.getNome());
        espaco.setCapacidade(dto.getCapacidade());
        espaco.setValorBase(dto.getValorBase());
        espaco.setStatus(dto.getStatus());

        Espaco salvo = espacoRepository.save(espaco);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EspacoResponseDTO(salvo));
    }
}