CREATE DATABASE IF NOT EXISTS safo;
USE safo;

-- 1. Criação das Tabelas
CREATE TABLE CLIENTES (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    telefone VARCHAR(15),
    email VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(10) NOT NULL
);

CREATE TABLE COLABORADORES (
    id_colaborador INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    especialidade VARCHAR(30),
    horario_trabalho VARCHAR(50),
    status VARCHAR(10) NOT NULL
);

CREATE TABLE ESPACOS (
    id_espaco INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    capacidade INT NOT NULL,
    valor_base DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE RESERVAS (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    data DATE NOT NULL,
    horario_inicio TIME NOT NULL,
    horario_fim TIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    id_cliente INT NOT NULL,
    id_espaco INT NOT NULL,
    CONSTRAINT fk_reserva_cliente FOREIGN KEY (id_cliente) REFERENCES CLIENTES(id_cliente),
    CONSTRAINT fk_reserva_espaco FOREIGN KEY (id_espaco) REFERENCES ESPACOS(id_espaco)
);

CREATE TABLE ATENDIMENTOS (
    id_atendimento INT AUTO_INCREMENT PRIMARY KEY,
    data DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    justificativa_cancelamento VARCHAR(200),
    id_colaborador INT NOT NULL,
    id_reserva INT NOT NULL,
    CONSTRAINT fk_atendimento_colab FOREIGN KEY (id_colaborador) REFERENCES COLABORADORES(id_colaborador),
    CONSTRAINT fk_atendimento_reserva FOREIGN KEY (id_reserva) REFERENCES RESERVAS(id_reserva)
);

-- 2. Criação do Usuário para o Spring Boot
CREATE USER 'safo_user'@'localhost' IDENTIFIED BY 'SuaSenhaSegura123';
GRANT ALL PRIVILEGES ON safo.* TO 'safo_user'@'localhost';
FLUSH PRIVILEGES;