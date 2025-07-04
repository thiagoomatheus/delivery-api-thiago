-- Dados de exemplo para testes
-- Arquivo: src/main/resources/data.sql

-- Inserir clientes
INSERT INTO cliente (nome, email, telefone, endereco, data_cadastro, ativo, role) VALUES
('João Silva', 'joao@email.com', '(11) 99999-1111', 'Rua A, 123 - São Paulo/SP', NOW(), true, 'CUSTOMER'),
('Maria Santos', 'maria@email.com', '(11) 99999-2222', 'Rua B, 456 - São Paulo/SP', NOW(), true, 'CUSTOMER'),
('Pedro Oliveira', 'pedro@email.com', '(11) 99999-3333', 'Rua C, 789 - São Paulo/SP', NOW(), true, 'CUSTOMER');

-- Inserir restaurantes
INSERT INTO restaurante (nome, categoria, endereco, telefone, taxa_entrega, avaliacao, ativo) VALUES
('Pizzaria Bella', 'Italiana', 'Av. Paulista, 1000 - São Paulo/SP', '(11) 3333-1111', 5.00, 4.5, true),
('Burger House', 'Hamburgueria', 'Rua Augusta, 500 - São Paulo/SP', '(11) 3333-2222', 3.50, 4.2, true),
('Sushi Master', 'Japonesa', 'Rua Liberdade, 200 - São Paulo/SP', '(11) 3333-3333', 8.00, 4.8, true);

-- Inserir produtos
INSERT INTO produto (nome, descricao, preco, categoria, disponivel, restaurante_id) VALUES
-- Pizzaria Bella
('Pizza Margherita', 'Molho de tomate, mussarela e manjericão', 35.90, 'Pizza', true, 1),
('Pizza Calabresa', 'Molho de tomate, mussarela e calabresa', 38.90, 'Pizza', true, 1),
('Lasanha Bolonhesa', 'Lasanha tradicional com molho bolonhesa', 28.90, 'Massa', true, 1),

-- Burger House
('X-Burger', 'Hambúrguer, queijo, alface e tomate', 18.90, 'Hambúrguer', true, 2),
('X-Bacon', 'Hambúrguer, queijo, bacon, alface e tomate', 22.90, 'Hambúrguer', true, 2),
('Batata Frita', 'Porção de batata frita crocante', 12.90, 'Acompanhamento', true, 2),

-- Sushi Master
('Combo Sashimi', '15 peças de sashimi variado', 45.90, 'Sashimi', true, 3),
('Hot Roll Salmão', '8 peças de hot roll de salmão', 32.90, 'Hot Roll', true, 3),
('Temaki Atum', 'Temaki de atum com cream cheese', 15.90, 'Temaki', true, 3);

INSERT INTO pedido (numero_pedido, data_pedido, endereco_entrega, subtotal, taxa_entrega, valor_total, observacoes, status, cliente_id, restaurante_id) VALUES
('PED-ABCDEF01', NOW(), 'Rua A, 123 - São Paulo/SP', 64.80, 5.00, 64.80 + 5.00, 'Sem cebola na pizza', 'PENDENTE', 1, 1), -- Valor total = (35.90 + 28.90) + 5.00
('PED-GHIJKL02', NOW(), 'Rua B, 456 - São Paulo/SP', 41.80, 3.50, 41.80 + 3.50, '', 'CONFIRMADO', 2, 2),   -- Valor total = (22.90 + 18.90) + 3.50
('PED-MNOPQR03', NOW(), 'Rua C, 789 - São Paulo/SP', 78.80, 8.00, 78.80 + 8.00, 'Wasabi à parte', 'ENTREGUE', 3, 3);    -- Valor total = (45.90 + 32.90) + 8.00

-- Inserir itens dos pedidos
INSERT INTO item_pedido (quantidade, preco_unitario, subtotal, pedido_id, produto_id) VALUES
-- Pedido 1 (João - Pizzaria Bella)
(1, 35.90, 35.90, 1, 1), -- Pizza Margherita
(1, 28.90, 28.90, 1, 3), -- Lasanha

-- Pedido 2 (Maria - Burger House)
(1, 22.90, 22.90, 2, 5), -- X-Bacon
(1, 18.90, 18.90, 2, 4), -- X-Burger

-- Pedido 3 (Pedro - Sushi Master)
(1, 45.90, 45.90, 3, 7), -- Combo Sashimi
(1, 32.90, 32.90, 3, 8); -- Hot Roll