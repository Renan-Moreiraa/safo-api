package com.safo.safoapi.service;

import com.safo.safoapi.dto.AtendimentoRequestDTO;
import com.safo.safoapi.dto.AtendimentoResponseDTO;
import com.safo.safoapi.model.Agendamento;
import com.safo.safoapi.model.Atendimento;
import com.safo.safoapi.model.Colaborador;
import com.safo.safoapi.repository.AgendamentoRepository;
import com.safo.safoapi.repository.AtendimentoRepository;
import com.safo.safoapi.repository.ColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AtendimentoService {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public List<AtendimentoResponseDTO> listarTodos() {
        return atendimentoRepository.findAll()
                .stream()
                .map(AtendimentoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<AtendimentoResponseDTO> buscarPorId(Long id) {
        return atendimentoRepository.findById(id)
                .map(AtendimentoResponseDTO::new);
    }

    public AtendimentoResponseDTO criar(AtendimentoRequestDTO dto) {
        Colaborador colaborador = colaboradorRepository.findById(dto.getIdColaborador())
                .orElseThrow(() -> new IllegalArgumentException("Colaborador não encontrado com o ID fornecido."));

        Agendamento agendamento = agendamentoRepository.findById(dto.getIdAgendamento())
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado com o ID fornecido."));

        Atendimento atendimento = new Atendimento();
        atendimento.setData(dto.getData());
        atendimento.setStatus(dto.getStatus());
        atendimento.setJustificativaCancelamento(dto.getJustificativaCancelamento());
        atendimento.setColaborador(colaborador);
        atendimento.setAgendamento(agendamento);

        Atendimento salvo = atendimentoRepository.save(atendimento);
        return new AtendimentoResponseDTO(salvo);
    }

    public AtendimentoResponseDTO atualizar(Long id, AtendimentoRequestDTO dto) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atendimento não encontrado com o ID: " + id));

        Colaborador colaborador = colaboradorRepository.findById(dto.getIdColaborador())
                .orElseThrow(() -> new IllegalArgumentException("Colaborador não encontrado com o ID fornecido."));

        Agendamento agendamento = agendamentoRepository.findById(dto.getIdAgendamento())
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado com o ID fornecido."));

        atendimento.setData(dto.getData());
        atendimento.setStatus(dto.getStatus());
        atendimento.setJustificativaCancelamento(dto.getJustificativaCancelamento());
        atendimento.setColaborador(colaborador);
        atendimento.setAgendamento(agendamento);

        Atendimento atualizado = atendimentoRepository.save(atendimento);
        return new AtendimentoResponseDTO(atualizado);
    }
}