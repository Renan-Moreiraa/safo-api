package com.safo.safoapi.service;

import com.safo.safoapi.dto.EspacoRequestDTO;
import com.safo.safoapi.dto.EspacoResponseDTO;
import com.safo.safoapi.model.Espaco;
import com.safo.safoapi.repository.EspacoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EspacoService {

    @Autowired
    private EspacoRepository espacoRepository;

    public List<EspacoResponseDTO> listarTodos() {
        return espacoRepository.findAll()
                .stream()
                .map(EspacoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<EspacoResponseDTO> buscarPorId(Long id) {
        return espacoRepository.findById(id)
                .map(EspacoResponseDTO::new);
    }

    public EspacoResponseDTO salvar(EspacoRequestDTO dto) {
        Espaco espaco = new Espaco();
        espaco.setNome(dto.getNome());
        espaco.setCapacidade(dto.getCapacidade());
        espaco.setValorBase(dto.getValorBase());
        espaco.setStatus(dto.getStatus());

        Espaco salvo = espacoRepository.save(espaco);
        return new EspacoResponseDTO(salvo);
    }
}