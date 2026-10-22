package com.safo.safoapi.controller;

import com.safo.safoapi.model.Quadra;
import com.safo.safoapi.repository.QuadraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quadras")
public class QuadraController {

    @Autowired
    private QuadraRepository quadraRepository;

    @GetMapping
    public List<Quadra> listarTodas() {
        return quadraRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quadra> buscarPorId(@PathVariable Long id) {
        return quadraRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Quadra> criar(@RequestBody Quadra quadra) {
        Quadra salva = quadraRepository.save(quadra);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quadra> atualizar(@PathVariable Long id, @RequestBody Quadra quadra) {
        return quadraRepository.findById(id)
                .map(q -> {
                    q.setNome(quadra.getNome());
                    q.setTipoEsporte(quadra.getTipoEsporte());
                    q.setPrecoHora(quadra.getPrecoHora());
                    q.setStatus(quadra.getStatus());
                    return ResponseEntity.ok(quadraRepository.save(q));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (quadraRepository.existsById(id)) {
            quadraRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}