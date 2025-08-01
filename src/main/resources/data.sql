-- data.sql

-- Inserir clientes
INSERT INTO cliente (nome, email, ativo, data_criacao) VALUES
('João Silva', 'joao@email.com', true, NOW()),
('Maria Santos', 'maria@email.com', true, NOW()),
('Pedro Oliveira', 'pedro@email.com', true, NOW());

-- Inserir restaurantes
INSERT INTO restaurante (nome, categoria, telefone, taxa_entrega, avaliacao, tempo_entrega_minutos, ativo) VALUES
('Pizzaria Bella', 'Italiana', '(11) 3333-1111', 5.00, 4.5, 45, true),
('Burger House', 'Hamburgueria', '(11) 3333-2222', 3.50, 4.2, 30, true),
('Sushi Master', 'Japonesa', '(11) 3333-3333', 8.00, 4.8, 40, true),
('Chicken Express', 'Restaurante', '(11) 3333-4444', 6.00, 4.3, 50, true),
('Pasta Paradise', 'Italiana', '(11) 3333-5555', 7.00, 4.4, 35, true),
('Pizza Express', 'Italiana', '(11) 3333-6666', 4.50, 4.1, 25, true),
('Pastel do João', 'Pastelaria', '(11) 3333-7777', 2.50, 4.0, 20, true),
('Bora Bora', 'Restaurante', '(11) 3333-8888', 9.00, 4.6, 55, true),
('Madeiro', 'Hamburgueria', '(11) 3333-9999', 10.00, 4.7, 60, true),
('Sushi Express', 'Japonesa', '(11) 3333-0000', 5.50, 4.2, 40, true),
('Comida Caseira', 'Restaurante', '(11) 3333-1111', 2.00, 4.0, 15, true),
('Churras Gaúcho', 'Restaurante', '(11) 3333-1111', 2.00, 4.0, 15, true),
('Pizzaria do Zezinho', 'Italiana', '(11) 3333-1111', 2.00, 4.0, 15, true),
('Coxinhas Gostosas', 'Lanchonete', '(11) 3333-1111', 2.00, 4.0, 15, true),
('Comida Mexicana', 'Mexicana', '(11) 3333-1111', 2.00, 4.0, 15, true),
('Comida Brasileira', 'Brasileira', '(11) 3333-1111', 2.00, 4.0, 15, true),
('Tailand Express', 'Tailandesa', '(11) 3333-1111', 2.00, 4.0, 15, true);

-- Inserir produtos
INSERT INTO produto (nome, categoria, descricao, preco, disponivel, restaurante_id) VALUES
('Pizza Margherita', 'Pizza', 'Molho de tomate, mussarela e manjericão', 35.90, true, 1),
('Pizza Calabresa', 'Pizza', 'Molho de tomate, mussarela e calabresa', 38.90, true, 1),
('Lasanha Bolonhesa', 'Massa', 'Lasanha tradicional com molho bolonhesa', 28.90, true, 1),
('X-Burger', 'Hambúrguer', 'Hambúrguer, queijo, alface e tomate', 18.90, true, 2),
('X-Bacon', 'Hambúrguer', 'Hambúrguer, queijo, bacon, alface e tomate', 22.90, true, 2),
('Sushi Combo', 'Sushi', 'Combo de sushi variado', 45.90, true, 3);

-- Inserir pedidos
INSERT INTO pedido (data_pedido, endereco_entrega, total, status, cliente_id, restaurante_id) VALUES
(NOW(), '{"rua": "Rua A", "numero": "123", "bairro": "São Paulo", "cidade": "São Paulo", "estado": "SP", "cep": "12345-678"}', 64.80, 'CRIADO', 1, 1),
(NOW(), '{"rua": "Rua B", "numero": "456", "bairro": "São Paulo", "cidade": "São Paulo", "estado": "SP", "cep": "12345-678"}', 41.80, 'EM_PREPARACAO', 2, 2),
(NOW(), '{"rua": "Rua C", "numero": "789", "bairro": "São Paulo", "cidade": "São Paulo", "estado": "SP", "cep": "12345-678"}', 78.80, 'ENTREGUE', 3, 3);

-- Inserir itens de pedidos
INSERT INTO item_pedido (quantidade, preco_unitario, pedido_id, produto_id) VALUES
(1, 35.90, 1, 1), -- Pizza Margherita
(1, 28.90, 1, 3), -- Lasanha Bolonhesa
(1, 22.90, 2, 5), -- X-Bacon
(1, 18.90, 2, 4), -- X-Burger
(1, 45.90, 3, 6); -- Sushi Combo

-- Inserir entregadores
INSERT INTO entregador (nome, email, telefone, ativo, data_criacao) VALUES
('João Entregador', 'joaoentregador@email.com', '(11) 3333-4444', true, NOW()),
('Maria Entregadora', 'mariaentregadora@email.com', '(11) 3333-5555', true, NOW());

-- Inserir entregas
INSERT INTO entrega (endereco_entrega, status, horario_estimado_entrega, horario_realizado_entrega, taxa_entrega, pedido_id, entregador_id) VALUES
('{"rua": "Rua A", "numero": "123", "bairro": "São Paulo", "cidade": "São Paulo", "estado": "SP", "cep": "12345-678"}', 'ENTREGUE', NOW(), NOW(), 5.00, 1, 1),
('{"rua": "Rua B", "numero": "456", "bairro": "São Paulo", "cidade": "São Paulo", "estado": "SP", "cep": "12345-678"}', 'EM_TRANSITO', NOW(), NULL, 3.50, 2, 2),
('{"rua": "Rua C", "numero": "789", "bairro": "São Paulo", "cidade": "São Paulo", "estado": "SP", "cep": "12345-678"}', 'AGUARDANDO', NOW(), NULL, 8.00, 3, 1);