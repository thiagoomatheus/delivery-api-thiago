CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    ativo BOOLEAN NOT NULL,
    data_criacao TIMESTAMP NOT NULL
);

CREATE TABLE restaurante (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    categoria VARCHAR(255),
    telefone VARCHAR(20),
    taxa_entrega DECIMAL(10, 2),
    avaliacao DECIMAL(3, 1),
    ativo BOOLEAN NOT NULL,
    tempo_entrega_minutos INT NOT NULL
);

CREATE TABLE produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    categoria VARCHAR(255),
    descricao VARCHAR(500),
    preco DECIMAL(10, 2) NOT NULL,
    disponivel BOOLEAN NOT NULL,
    restaurante_id BIGINT,
    FOREIGN KEY (restaurante_id) REFERENCES restaurante(id)
);

CREATE TABLE pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_pedido TIMESTAMP NOT NULL,
    endereco_entrega VARCHAR(255) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    cliente_id BIGINT NOT NULL,
    restaurante_id BIGINT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (restaurante_id) REFERENCES restaurante(id)
);

CREATE TABLE item_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    pedido_id BIGINT,
    produto_id BIGINT,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

CREATE TABLE entregador (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    ativo BOOLEAN NOT NULL,
    data_criacao TIMESTAMP NOT NULL
);

CREATE TABLE entrega (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    endereco_entrega VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    horario_estimado_entrega TIMESTAMP NOT NULL,
    horario_realizado_entrega TIMESTAMP,
    taxa_entrega DECIMAL(10, 2) NOT NULL,
    pedido_id BIGINT,
    entregador_id BIGINT,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (entregador_id) REFERENCES entregador(id)
);

CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    ativo BOOLEAN NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    restaurante_id BIGINT NOT NULL
);