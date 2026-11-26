USE bar;

INSERT INTO produto
(cod_produto, nome_produto, marca_produto, preco_compra_produto, preco_venda_produto)
VALUES
(1, 'Coca-Cola 350ml', 'Coca-Cola', 3.50, 6.00),
(2, 'Guarana 350ml', 'Antarctica', 3.00, 5.50),
(3, 'Agua Mineral 500ml', 'Crystal', 1.50, 3.00),
(4, 'Suco de Laranja 1L', 'Del Valle', 6.00, 10.00),
(5, 'Cerveja 350ml', 'Heineken', 4.50, 8.00),
(6, 'Hamburguer', 'Sadia', 12.00, 25.00),
(7, 'Batata Frita 300g', 'McCain', 7.00, 14.00),
(8, 'Chocolate 90g', 'Nestle', 4.00, 7.50),
(9, 'Leite Condensado', 'Italac', 5.00, 8.00),
(10, 'Creme de Leite', 'Piracanjuba', 3.50, 6.00);

INSERT INTO validade
(data_validade, cod_produto_validade, quantidade_validade)
VALUES
('2027-01-15', 1, 20),
('2027-02-20', 1, 15),
('2027-03-10', 1, 10),

('2027-01-20', 2, 18),
('2027-02-25', 2, 12),

('2027-03-15', 3, 30),
('2027-04-10', 3, 25),

('2027-01-30', 4, 10),
('2027-03-20', 4, 15),
('2027-04-25', 4, 8),

('2027-02-10', 5, 24),
('2027-03-15', 5, 18),

('2027-01-25', 6, 12),
('2027-03-30', 6, 8),
('2027-04-20', 6, 10),

('2027-02-15', 7, 15),
('2027-04-10', 7, 12),

('2027-01-18', 8, 20),
('2027-03-25', 8, 15),
('2027-04-15', 8, 10),

('2027-02-28', 9, 18),
('2027-04-20', 9, 12),

('2027-03-10', 10, 20),
('2027-05-15', 10, 15);

INSERT INTO pedido
(num_pedido, num_mesa_pedido, desconto_pedido, status_pedido, descricao_pedido)
VALUES
(1, 1, 0.00, 'Aberto', 'Pedido da mesa 1'),
(2, 2, 5.00, 'Finalizado', 'Pedido da mesa 2'),
(3, 3, 0.00, 'Finalizado', 'Pedido da mesa 3'),
(4, 4, 2.50, 'Aberto', 'Pedido da mesa 4'),
(5, 5, 0.00, 'Finalizado', 'Pedido da mesa 5'),
(6, 1, 10.00, 'Aberto', 'Pedido adicional da mesa 1'),
(7, 2, 0.00, 'Finalizado', 'Pedido adicional da mesa 2'),
(8, 6, 3.00, 'Aberto', 'Pedido da mesa 6'),
(9, 7, 0.00, 'Finalizado', 'Pedido da mesa 7'),
(10, 8, 5.00, 'Aberto', 'Pedido da mesa 8');

INSERT INTO itens_pedido
(cod_produto_itens, num_pedido_itens, quantidade_pedido)
VALUES
-- Pedido 1: 4 produtos
(1, 1, 2),
(3, 1, 2),
(6, 1, 1),
(7, 1, 1),

-- Pedido 2: 3 produtos
(2, 2, 2),
(5, 2, 3),
(8, 2, 1),

-- Pedido 3: 2 produtos
(4, 3, 1),
(6, 3, 2),

-- Pedido 4: 1 produto
(9, 4, 3),

-- Pedido 5: 4 produtos
(1, 5, 3),
(2, 5, 2),
(3, 5, 4),
(10, 5, 2),

-- Pedido 6: 2 produtos
(5, 6, 2),
(7, 6, 1),

-- Pedido 7: 3 produtos
(6, 7, 1),
(8, 7, 2),
(9, 7, 1),

-- Pedido 8: 1 produto
(10, 8, 4),

-- Pedido 9: 4 produtos
(1, 9, 2),
(4, 9, 1),
(6, 9, 1),
(7, 9, 2),

-- Pedido 10: 3 produtos
(2, 10, 3),
(5, 10, 2),
(8, 10, 2);