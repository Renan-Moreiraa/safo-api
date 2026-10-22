package com.safo.safoapi.controller;

import com.safo.safoapi.dto.ColaboradorRequestDTO;
import com.safo.safoapi.dto.ColaboradorResponseDTO;
import com.safo.safoapi.model.Colaborador;
import com.safo.safoapi.repository.ColaboradorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @GetMapping
    public List<ColaboradorResponseDTO> listarTodos() {
        return colaboradorRepository.findAll()
                .stream()
                .map(ColaboradorResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorResponseDTO> buscarPorId(@PathVariable Long id) {
        return colaboradorRepository.findById(id)
                .map(colaborador -> ResponseEntity.ok(new ColaboradorResponseDTO(colaborador)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ColaboradorResponseDTO> criar(@Valid @RequestBody ColaboradorRequestDTO dto) {
        Colaborador colaborador = new Colaborador();
        colaborador.setNome(dto.getNome());
        colaborador.setCpf(dto.getCpf());
        colaborador.setEspecialidade(dto.getEspecialidade());
        colaborador.setHorarioTrabalho(dto.getHorarioTrabalho());
        colaborador.setStatus(dto.getStatus());

        Colaborador salvo = colaboradorRepository.save(colaborador);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ColaboradorResponseDTO(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (colaboradorRepository.existsById(id)) {
            colaboradorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}