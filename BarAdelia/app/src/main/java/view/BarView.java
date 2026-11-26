package view;

import entity.*;
import control.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.util.converter.NumberStringConverter;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

public class BarView extends Application {

    // Criando as tabelas
    private TableView<Produto> tableProdutos = new TableView<>();
    private TableView<Pedido> tablePedidos = new TableView<>();

    // Criando os Objetos de Controle
    PedidoControl pedidoControl = new PedidoControl();
    ProdutoControl produtoControl = new ProdutoControl();
    

    public void start(Stage stage){
        // Criando o painel de abas
        TabPane tabPane = new TabPane();

        // Criando as duas abas
        Tab tabProduto = new Tab("Produtos");
        Tab tabPedido = new Tab("Pedidos");

        // Adicionando as abas na TabPane
        tabPane.getTabs().addAll(tabProduto, tabPedido);


        // ----------------------- Pane Produtos -----------------------

        // Criando o Painel Principal dos Produtos
        BorderPane painelPrincipalProduto = new BorderPane();

        // Criando o Painel do topo dos Produtos
        GridPane painelTopoProduto = new GridPane();
        painelTopoProduto.setHgap(15);
        painelTopoProduto.setVgap(5);
        painelPrincipalProduto.setTop(painelTopoProduto);

        // Adicionando a tabela produto ao grid
        painelPrincipalProduto.setCenter(tableProdutos);

        // Adicionando os painéis dos produtos na aba produtos
        tabProduto.setContent(painelPrincipalProduto);


        // ----------------------- Pane Pedidos -----------------------

        // Criando o painel principal dos Pedidos
        BorderPane painelPrincipalPedido = new BorderPane();

        // Criando o painel do topo dos Pedidos
        GridPane painelTopoPedido = new GridPane();
        painelTopoPedido.setHgap(15);
        painelTopoPedido.setVgap(5);
        painelPrincipalPedido.setTop(painelTopoPedido);

        // Adicionando a tabela pedido ao grid
        painelPrincipalPedido.setCenter(tablePedidos);

        // Adicionando os paineis na aba Pedido
        tabPedido.setContent(painelPrincipalPedido);

        
        // ----------------------- Adicianando o painel no cenário -----------------------
        Scene cena = new Scene(tabPane, 600, 600);

        stage.setScene(cena);
        stage.setTitle("Gerenciar Estabelecimento");


        // ---------------------- Criando elementos da aba Produto ----------------------

        // -------- Nome produto --------
        Label lblNome = new Label("Nome do produto: ");
        painelTopoProduto.add(lblNome, 0, 0);

        TextField txtNome = new TextField();
        painelTopoProduto.add(txtNome, 1, 0);

        // -------- Marca produto --------
        Label lblMarca = new Label("Marca do produto: ");
        painelTopoProduto.add(lblMarca, 0, 1);

        TextField txtMarca = new TextField();
        painelTopoProduto.add(txtMarca, 1, 1);

        // -------- Preço de Compra produto --------
        Label lblPrecoCompra = new Label("Preço de Compra: ");
        painelTopoProduto.add(lblPrecoCompra, 0, 2);

        TextField txtPrecoCompra = new TextField();
        painelTopoProduto.add(txtPrecoCompra, 1, 2);

        // -------- Preço de Venda produto --------
        Label lblPrecoVenda = new Label("Preço de Venda: ");
        painelTopoProduto.add(lblPrecoVenda, 0, 3);

        TextField txtPrecoVenda = new TextField();
        painelTopoProduto.add(txtPrecoVenda, 1, 3);

        // -------- Data de Validade --------
        Label lblValidade = new Label("Data de validade: ");
        painelTopoProduto.add(lblValidade, 0, 4);

        TextField txtValidade = new TextField();
        painelTopoProduto.add(txtValidade, 1, 4);

        // -------- Quantidade do produto --------
        Label lblQuantidade = new Label("Quantidade: ");
        painelTopoProduto.add(lblQuantidade, 0, 5);

        TextField txtQuantidade = new TextField();
        painelTopoProduto.add(txtQuantidade, 1, 5);

        // -------- Sincronizando as variáveis do Produto com o Controller --------
        Bindings.bindBidirectional(txtNome.textProperty(), produtoControl.nomeProperty());
        Bindings.bindBidirectional(txtMarca.textProperty(), produtoControl.marcaProperty());
        Bindings.bindBidirectional(txtPrecoCompra.textProperty(), 
            produtoControl.precoCompraProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtPrecoVenda.textProperty(), 
            produtoControl.precoVendaProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtValidade.textProperty(), produtoControl.validadeProperty());
        Bindings.bindBidirectional(txtQuantidade.textProperty(), 
            produtoControl.quantidadeProperty(), new NumberStringConverter());

        // -------- Botões Produto --------
        Button btnAdicionarProduto = new Button("Adicionar");
        painelTopoProduto.add(btnAdicionarProduto, 0, 6);
        
        btnAdicionarProduto.setOnAction( e -> {

            try{
                produtoControl.adicionarItem();
            }catch (Exception exc){
                produtoControl.pesquisarTodos();
                new Alert(AlertType.INFORMATION, exc.getMessage(), ButtonType.OK).showAndWait();
            }
            
        });

        Button btnPesquisarProduto = new Button("Pesquisar");
        painelTopoProduto.add(btnPesquisarProduto, 1, 6);

        btnPesquisarProduto.setOnAction( e -> {
            produtoControl.pesquisarItem();
        });

        // --------------- Tabela Produto ---------------
		
		// --- Criando as Colunas ---
        TableColumn<Produto, String> colNomeMarca = new TableColumn<>("Nome - Marca");
		colNomeMarca.setPrefWidth(160);
		colNomeMarca.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().toString())
        );

        TableColumn<Produto, Number> colQuantidadeProduto = new TableColumn<>("Qnt");
		colQuantidadeProduto.setPrefWidth(50);
		colQuantidadeProduto.setCellValueFactory(
            item -> new ReadOnlyIntegerWrapper(item.getValue().getQuantidade())
        );
	
        TableColumn<Produto, String> colPrecos = new TableColumn<>("Preços:\nCompra - Venda");
		colPrecos.setPrefWidth(130);
		colPrecos.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getPrecos())
        );

        TableColumn<Produto, String> colValidade = new TableColumn<>("Data de Validade");
		colValidade.setPrefWidth(130);
		colValidade.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getValidade())
        );

        TableColumn<Produto, Void> colAcao = new TableColumn<>("Ações");
		colAcao.setPrefWidth(130);

        Callback<TableColumn<Produto, Void>, TableCell<Produto, Void>> callBack = 
            new Callback<>(){ 
                public TableCell<Produto, Void> call(TableColumn<Produto, Void> tc) { 
                    TableCell<Produto, Void> tableCell = new TableCell<>(){

                        private final Button btnExcluir = new Button("Excluir");
                        private final Button btnAtualizar = new Button("Atualizar");
                        private final HBox painelBotoes = new HBox();
                        
                        {
                            painelBotoes.getChildren().addAll(btnAtualizar, btnExcluir);
                            btnExcluir.setOnAction( e -> {
                                    produtoControl.excluirProduto( getTableRow().getItem() );
                                });
                            btnAtualizar.setOnAction( e -> {
                                    produtoControl.atualizarProduto( getTableRow().getItem() );
                                });
                        }
                        public void updateItem(Void valor, boolean empty) {
                            if (!empty) {                                
                                setGraphic( painelBotoes );
                            } else { 
                                setGraphic(null);
                            }
                        }
                    };
                    return tableCell;
                }
            };
        colAcao.setCellFactory( callBack );
		

        // Definindo a posição das colunas e o item da tabela
		tableProdutos.getColumns().add(colNomeMarca); 
        tableProdutos.getColumns().add(colQuantidadeProduto);
		tableProdutos.getColumns().add(colPrecos);
		tableProdutos.getColumns().add(colValidade);
		tableProdutos.getColumns().add(colAcao);

        // Definindo o item que tabela irá pegar os valores
        tableProdutos.setItems(produtoControl.getListaTabelaProdutos());


        // ---------------------- Criando elementos da aba Pedido ----------------------

        // -------- Número do Pedido --------
        Label lblNumPedido = new Label("Número do Pedido: ");
        painelTopoPedido.add(lblNumPedido, 0, 0);

        TextField txtNumPedido = new TextField();
        painelTopoPedido.add(txtNumPedido, 1, 0);

        // -------- Número da mesa do Pedido --------
        Label lblNumMesa = new Label("Número da mesa: ");
        painelTopoPedido.add(lblNumMesa, 0, 1);

        TextField txtNumMesa = new TextField();
        painelTopoPedido.add(txtNumMesa, 1, 1);

        // -------- Produto do Pedido --------
        Label lblProduto = new Label("Produto: ");
        painelTopoPedido.add(lblProduto, 0, 2);

        ComboBox<Produto> cboxProduto = new ComboBox<>();
        cboxProduto.setPrefWidth(200);
        painelTopoPedido.add(cboxProduto, 1, 2);

        cboxProduto.setItems(produtoControl.getListaProdutosDisponiveis());

        // -------- Quantidade do Produto Pedido --------
        Label lblQuantidadePedido = new Label("Quantidade: ");
        painelTopoPedido.add(lblQuantidadePedido, 0, 3);

        TextField txtQuantidadePedido = new TextField();
        painelTopoPedido.add(txtQuantidadePedido, 1, 3);

        // -------- Desconto do Pedido --------
        Label lblDesconto = new Label("Desconto: ");
        painelTopoPedido.add(lblDesconto, 2, 3);

        TextField txtDesconto = new TextField();
        painelTopoPedido.add(txtDesconto, 3, 3);

        // -------- Descrição do Pedido --------
        Label lblDescricao= new Label("Descrição: ");
        painelTopoPedido.add(lblDescricao, 0, 4);

        TextArea txtAreaDescricao = new TextArea();
        txtAreaDescricao.setPrefWidth(200);
        txtAreaDescricao.setPrefHeight(50);
        painelTopoPedido.add(txtAreaDescricao, 1, 4);

        // -------- Valor Total do Pedido --------
        Label lblValorTotal = new Label("Valor Total: ");
        painelTopoPedido.add(lblValorTotal, 2, 1);

        TextField txtValorTotal = new TextField();
        txtValorTotal.setEditable(false);
        painelTopoPedido.add(txtValorTotal, 3, 1);

        // Sincronizando a Tela Pedido com as variáveis de controle
        
        Bindings.bindBidirectional(txtNumPedido.textProperty(), 
            pedidoControl.numPedidoProperty(), new NumberStringConverter());

        Bindings.bindBidirectional(txtNumMesa.textProperty(), 
            pedidoControl.numMesaProperty(), new NumberStringConverter());

        cboxProduto.valueProperty().bindBidirectional(
            pedidoControl.produtoSelecionadoProperty()
            );

        Bindings.bindBidirectional(txtQuantidadePedido.textProperty(), 
            pedidoControl.quantidadeProdutoProperty(), new NumberStringConverter());

        Bindings.bindBidirectional(txtDesconto.textProperty(), 
            pedidoControl.descontoProperty(), new NumberStringConverter());

        Bindings.bindBidirectional(txtAreaDescricao.textProperty(), 
            pedidoControl.descricaoProperty());

        Bindings.bindBidirectional(txtValorTotal.textProperty(), 
            pedidoControl.valorTotalProperty(), new NumberStringConverter());

        // -------- Botões Pedido --------
        Button btnFecharPedido = new Button("Fechar Pedido");
        painelTopoPedido.add(btnFecharPedido, 2, 0);

        btnFecharPedido.setOnAction( e -> {

            try{
                pedidoControl.fecharPedido();
            }catch(Exception exc){
                pedidoControl.pesquisarTodos();
                new Alert(AlertType.INFORMATION, exc.getMessage(), ButtonType.OK).showAndWait();
            }
        });

        Button btnSalvarPedido = new Button("Salvar");
        painelTopoPedido.add(btnSalvarPedido, 0, 5);

        btnSalvarPedido.setOnAction(e -> {
            try{
                pedidoControl.salvar();
            }catch(Exception exc){
                pedidoControl.pesquisarTodos();
                new Alert(AlertType.INFORMATION, exc.getMessage(), ButtonType.OK).showAndWait();
            }
        });

        Button btnPesquisarPedido = new Button("Pesquisar");
        painelTopoPedido.add(btnPesquisarPedido, 1, 5);

        btnPesquisarPedido.setOnAction(e -> {
            pedidoControl.pesquisarPedido();
        });

        // --------------- Tabela Pedido ---------------
		
		// --- Criando as Colunas ---
        TableColumn<Pedido, Number> colNumeroPedido = new TableColumn<>("Num.\nPedido");
		colNumeroPedido.setPrefWidth(50);
		colNumeroPedido.setCellValueFactory(
            item -> new ReadOnlyIntegerWrapper(item.getValue().getNumPedido())
        );

        TableColumn<Pedido, Number> colMesaPedido = new TableColumn<>("Mesa");
		colMesaPedido.setPrefWidth(50);
		colMesaPedido.setCellValueFactory(
            item -> new ReadOnlyIntegerWrapper(item.getValue().getNumMesa())
        );

        TableColumn<Pedido, String> colProduto = new TableColumn<>("Produto");
		colProduto.setPrefWidth(185);
		colProduto.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getProduto().toString())
        );

        TableColumn<Pedido, Number> colQuantidadePedido = new TableColumn<>("Qnt");
		colQuantidadePedido.setPrefWidth(50);
		colQuantidadePedido.setCellValueFactory(
            item -> new ReadOnlyIntegerWrapper(item.getValue().getQuantidadePedido())
        );

        TableColumn<Pedido, Number> colPrecoPedido = new TableColumn<>("Preço");
		colPrecoPedido.setPrefWidth(70);
		colPrecoPedido.setCellValueFactory(
            item -> new ReadOnlyFloatWrapper(item.getValue().getProduto().getPrecoVenda()*
            item.getValue().getQuantidadePedido())
        );

        TableColumn<Pedido, String> colStatusPedido = new TableColumn<>("Status");
		colStatusPedido.setPrefWidth(82.5);
		colStatusPedido.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getStatus())
        );

        TableColumn<Pedido, Void> colAcaoPedido = new TableColumn<>("Ações");
		colAcaoPedido.setPrefWidth(112.5);

        Callback<TableColumn<Pedido, Void>, TableCell<Pedido, Void>> callBackPedido = 
            new Callback<>(){ 
                public TableCell<Pedido, Void> call(TableColumn<Pedido, Void> tc) { 
                    TableCell<Pedido, Void> tableCell = new TableCell<>(){

                        private final Button btnExcluir = new Button("Excluir");
                        private final Button btnAtualizar = new Button("Atualizar");
                        private final HBox painelBotoes = new HBox();
                        
                        {
                            painelBotoes.getChildren().addAll(btnAtualizar, btnExcluir);
                            btnExcluir.setOnAction( e -> {
                                    pedidoControl.excluirPedido( getTableRow().getItem() );
                                });
                            btnAtualizar.setOnAction( e -> {
                                    pedidoControl.atualizarPedido( getTableRow().getItem() );
                                });
                        }
                        public void updateItem(Void valor, boolean empty) {
                            if (!empty) {                                
                                setGraphic( painelBotoes );
                            } else { 
                                setGraphic(null);
                            }
                        }
                    };
                    return tableCell;
                }
            };
        colAcaoPedido.setCellFactory( callBackPedido );

        tablePedidos.getColumns().add(colNumeroPedido);
        tablePedidos.getColumns().add(colMesaPedido);
        tablePedidos.getColumns().add(colProduto);
        tablePedidos.getColumns().add(colQuantidadePedido);
        tablePedidos.getColumns().add(colPrecoPedido);
        tablePedidos.getColumns().add(colStatusPedido);
        tablePedidos.getColumns().add(colAcaoPedido);

        // Definindo o item que tabela irá pegar os valores
        tablePedidos.setItems(pedidoControl.getListaTabelaPedidos());

        // Mostrando o palco
        stage.show();
    }

    public static void main(String[] args) {
                Application.launch(BarView.class, args);
    }
}