-- Tabela Clientes (corrigido nome e adicionadas colunas)
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    endereco VARCHAR(255) NOT NULL,
    data_criacao TIMESTAMP NOT NULL, -- Corresponde ao NOW() no data.sql
    ativo BOOLEAN NOT NULL,           -- Adicionado de data.sql
    role VARCHAR(50) NOT NULL
);

CREATE TABLE restaurante (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    categoria VARCHAR(255),
    endereco VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    taxa_entrega DECIMAL(10, 2),
    avaliacao DECIMAL(3, 1),
    ativo BOOLEAN NOT NULL,
    tempo_entrega_minutos INT NOT NULL
);

-- Tabela Produtos (corrigido nome e adicionadas colunas)
CREATE TABLE produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(500),
    preco DECIMAL(10, 2) NOT NULL,
    categoria VARCHAR(255),          -- Adicionado de data.sql
    disponivel BOOLEAN NOT NULL,     -- Adicionado de data.sql
    restaurante_id BIGINT,
    FOREIGN KEY (restaurante_id) REFERENCES restaurante(id) -- Corrigida referência do nome da tabela
);

-- Tabela Pedidos (corrigido nome e adicionadas colunas, renomeada data_hora)
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

-- Tabela Itens_Pedido (adicionada para corresponder ao data.sql)
-- Substitui a tabela de junção pedido_produto
CREATE TABLE item_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Chave primária para identificar cada item único
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    pedido_id BIGINT,
    produto_id BIGINT,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(500) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL,
    role VARCHAR(50) NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    restaurante_id BIGINT
);