DROP DATABASE bar;

CREATE DATABASE bar;

USE bar; 

CREATE TABLE produto(
    
    cod_produto INT AUTO_INCREMENT,
    nome_produto VARCHAR(100) NOT NULL,
    marca_produto VARCHAR(40) NOT NULL,
    preco_compra_produto NUMERIC(5, 2) NOT NULL,
    preco_venda_produto NUMERIC(5, 2) NOT NULL,

    CONSTRAINT pk_cod_produto PRIMARY KEY(cod_produto)
);

CREATE TABLE validade(

    data_validade DATE,
    cod_produto_validade INT,
    quantidade_validade INT NOT NULL,

    CONSTRAINT fk_cod_produto_validade FOREIGN KEY(cod_produto_validade) 
        REFERENCES produto(cod_produto) ON DELETE CASCADE,
    CONSTRAINT pk_data_validate PRIMARY KEY(cod_produto_validade, data_validade)
);

CREATE TABLE pedido(

    num_pedido INT AUTO_INCREMENT,
    num_mesa_pedido INT NOT NULL,
    desconto_pedido NUMERIC(4, 2) DEFAULT(0),
    status_pedido ENUM('Aberto', 'Finalizado'),
    descricao_pedido VARCHAR(100),

    CONSTRAINT pk_num_pedido PRIMARY KEY(num_pedido)
);

CREATE TABLE itens_pedido(

    cod_produto_itens INT,
    num_pedido_itens INT,
    quantidade_pedido INT NOT NULL,

    CONSTRAINT fk_cod_produto_itens FOREIGN KEY(cod_produto_itens) 
        REFERENCES produto(cod_produto) ON DELETE CASCADE,
    CONSTRAINT fk_num_pedido_itens FOREIGN KEY(num_pedido_itens)
        REFERENCES pedido(num_pedido) ON DELETE CASCADE,
    CONSTRAINT pk_itens_pedido PRIMARY KEY(cod_produto_itens, num_pedido_itens)
);