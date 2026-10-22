package com.safo.safoapi.service;

import com.safo.safoapi.dto.ColaboradorRequestDTO;
import com.safo.safoapi.dto.ColaboradorResponseDTO;
import com.safo.safoapi.model.Colaborador;
import com.safo.safoapi.repository.ColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    public List<ColaboradorResponseDTO> listarTodos() {
        return colaboradorRepository.findAll()
                .stream()
                .map(ColaboradorResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ColaboradorResponseDTO> buscarPorId(Long id) {
        return colaboradorRepository.findById(id)
                .map(ColaboradorResponseDTO::new);
    }

    public ColaboradorResponseDTO salvar(ColaboradorRequestDTO dto) {
        Colaborador colaborador = new Colaborador();
        colaborador.setNome(dto.getNome());
        colaborador.setCpf(dto.getCpf());
        colaborador.setEspecialidade(dto.getEspecialidade());
        colaborador.setHorarioTrabalho(dto.getHorarioTrabalho());
        colaborador.setStatus(dto.getStatus());

        Colaborador salvo = colaboradorRepository.save(colaborador);
        return new ColaboradorResponseDTO(salvo);
    }
}