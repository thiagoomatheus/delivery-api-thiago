-- Tabela Clientes (corrigido nome e adicionadas colunas)
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    endereco VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL, -- Corresponde ao NOW() no data.sql
    ativo BOOLEAN NOT NULL,           -- Adicionado de data.sql
    role VARCHAR(50) NOT NULL
);

-- Tabela Restaurantes (corrigido nome e adicionadas colunas, removido cnpj)
CREATE TABLE restaurante (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    categoria VARCHAR(255),          -- Adicionado de data.sql
    endereco VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),            -- Adicionado de data.sql
    taxa_entrega DECIMAL(10, 2),     -- Adicionado de data.sql
    avaliacao DECIMAL(3, 1),         -- Adicionado de data.sql (Ex: 4.5)
    ativo BOOLEAN NOT NULL           -- Adicionado de data.sql
    -- CNPJ removido, pois não está presente no data.sql
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
    numero_pedido VARCHAR(20) UNIQUE NOT NULL, -- Mapeia numeroPedido, gerado pela entity
    data_pedido TIMESTAMP NOT NULL,          -- Mapeia dataPedido, gerado pela entity
    endereco_entrega VARCHAR(255) NOT NULL,  -- Mapeia enderecoEntrega
    subtotal DECIMAL(10, 2) NOT NULL,
    taxa_entrega DECIMAL(10, 2) NOT NULL,    -- Mapeia taxaEntrega
    valor_total DECIMAL(10, 2) NOT NULL,     -- Mapeia valorTotal (subtotal da entity parece redundante para BD)
    observacoes VARCHAR(500),                -- Mapeia observacoes
    status VARCHAR(50) NOT NULL,             -- Mapeia status (Enum StatusPedido)
    cliente_id BIGINT NOT NULL,              -- Mapeia cliente (ManyToOne)
    restaurante_id BIGINT NOT NULL,          -- Mapeia restaurante (ManyToOne)
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (restaurante_id) REFERENCES restaurante(id)
);

-- Tabela Itens_Pedido (adicionada para corresponder ao data.sql)
-- Substitui a tabela de junção pedido_produto
CREATE TABLE item_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Chave primária para identificar cada item único
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    pedido_id BIGINT,
    produto_id BIGINT,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);