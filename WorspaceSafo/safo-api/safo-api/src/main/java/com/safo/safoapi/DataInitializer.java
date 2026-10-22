package com.safo.safoapi;

import com.safo.safoapi.model.Cliente;
import com.safo.safoapi.model.Colaborador;
import com.safo.safoapi.model.Espaco;
import com.safo.safoapi.repository.ClienteRepository;
import com.safo.safoapi.repository.ColaboradorRepository;
import com.safo.safoapi.repository.EspacoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            EspacoRepository espacoRepository,
            ClienteRepository clienteRepository,
            ColaboradorRepository colaboradorRepository) {

        return args -> {
            // Insere espaços somente se a tabela estiver vazia
            if (espacoRepository.count() == 0) {
                Espaco e1 = new Espaco();
                e1.setNome("Quadra de Futsal A");
                e1.setCapacidade(10);
                e1.setValorBase(new BigDecimal("120.00"));
                e1.setStatus("DISPONIVEL");

                Espaco e2 = new Espaco();
                e2.setNome("Campo Society 1");
                e2.setCapacidade(14);
                e2.setValorBase(new BigDecimal("200.00"));
                e2.setStatus("DISPONIVEL");

                Espaco e3 = new Espaco();
                e3.setNome("Quadra de Tênis");
                e3.setCapacidade(4);
                e3.setValorBase(new BigDecimal("90.00"));
                e3.setStatus("DISPONIVEL");

                espacoRepository.saveAll(List.of(e1, e2, e3));
            }

            // Insere clientes somente se a tabela estiver vazia
            if (clienteRepository.count() == 0) {
                Cliente c1 = new Cliente();
                c1.setNome("Carlos Silva");
                c1.setCpf("12345678901");
                c1.setTelefone("11999998888");
                c1.setEmail("carlos@email.com");
                c1.setStatus("ATIVO");

                Cliente c2 = new Cliente();
                c2.setNome("Mariana Oliveira");
                c2.setCpf("98765432100");
                c2.setTelefone("11977776666");
                c2.setEmail("mariana@email.com");
                c2.setStatus("ATIVO");

                clienteRepository.saveAll(List.of(c1, c2));
            }

            // Insere colaboradores somente se a tabela estiver vazia
            if (colaboradorRepository.count() == 0) {
                Colaborador col1 = new Colaborador();
                col1.setNome("Roberto Santos");
                col1.setCpf("11122233344");
                col1.setEspecialidade("Gerente de Quadra");
                col1.setHorarioTrabalho("08:00 - 17:00");
                col1.setStatus("ATIVO");

                colaboradorRepository.save(col1);
            }
        };
    }
}