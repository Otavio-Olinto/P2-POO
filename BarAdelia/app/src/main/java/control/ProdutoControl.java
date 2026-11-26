package control;

import entity.Produto;
import adapters.ProdutoDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

public class ProdutoControl {
    
    // Criando as variáveis para sincronizar com a tela
    private Integer id = 0;
    private StringProperty nome = new SimpleStringProperty();
    private StringProperty marca = new SimpleStringProperty();
    private FloatProperty precoCompra = new SimpleFloatProperty();
    private FloatProperty precoVenda = new SimpleFloatProperty();
    private StringProperty validade = new SimpleStringProperty();
    private IntegerProperty quantidade = new SimpleIntegerProperty();
    private ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();
    private ObservableList<Produto> listaProdutosDisponiveis = FXCollections.observableArrayList();
    private ObservableList<Produto> listaTabelaProdutos = FXCollections.observableArrayList();
    private ProdutoDAO daoProduto = new ProdutoDAO();
    private String validadeErrada;

    public ProdutoControl(){
        pesquisarTodos();
    }

    // Operações de controle dos produtos
    public void adicionarItem() throws Exception{

        Produto p = new Produto();
        p.setId(id);
        p.setNome(nome.get());
        p.setMarca(marca.get());
        p.setPrecoCompra(precoCompra.get());
        p.setPrecoVenda(precoVenda.get());
        p.setQuantidade(quantidade.get());
        p.setValidade(stringPraData(validade.get()));

        if(id == 0){

            for(Produto p2: listaProdutos){

                if(p.getNome().equals(p2.getNome()) &&
                    p.getMarca().equals(p2.getMarca())){
                        p.setId(p2.getId());
                        daoProduto.addValidade( p );
                        pesquisarTodos();
                        throw new Exception("Quantidade com nova validade adicionada para o produto: " +
                            p.getNome() + " - " + p.getMarca()
                        );
                }
            }

            daoProduto.criarProduto( p );
            pesquisarTodos();
            throw new Exception("Novo produto adicionado: " + 
                p.getNome() + " - " + p.getMarca()
            );

        }else{

            daoProduto.atualizar( p, validadeErrada );
            id = 0;
            pesquisarTodos();
            throw new Exception("Dados do Produto: " +  p.getNome() + " - " + p.getMarca() + 
                "\nAlterados com sucesso!");
        }
    }

    public void pesquisarTodos() { 
        limparCampos();
        listaProdutos.clear();
        listaProdutosDisponiveis.clear();
        listaTabelaProdutos.clear();
        
        listaProdutos.addAll( daoProduto.pesquisarProdutos() );
        listaProdutosDisponiveis.addAll(listaProdutos);
        verificarQuantidade();
        listaTabelaProdutos.addAll( daoProduto.pesquisarTodos() );

    }

    private void verificarQuantidade(){
        for(Produto p: listaProdutosDisponiveis){
            if(p.getQuantidade() < 1){
                listaProdutosDisponiveis.remove(p);
            }
        }
    }

    public void pesquisarItem(){

        listaTabelaProdutos.clear();

        if(nome.get().equals("") || nome.get() == null){
            pesquisarTodos();;
        }else{

            List<Produto> list = new ArrayList<>();
            list.addAll(daoProduto.pesquisarTodos());

            for(Produto p: list){
                if(p.getNome().contains(nome.get())){
                    listaTabelaProdutos.add(p);
                }
            }
        }
        
    }

    public void atualizarProduto(Produto p){
        validadeErrada = p.getValidade();
        id = p.getId();
        nome.set(p.getNome());
        marca.set(p.getMarca());
        precoCompra.set(p.getPrecoCompra());
        precoVenda.set(p.getPrecoVenda());
        quantidade.set(p.getQuantidade());
        validade.set(p.getValidade());
    }

    public void excluirProduto( Produto p){
        daoProduto.excluir( p );
        pesquisarTodos();
    }

    private LocalDate stringPraData(String data){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(data, formato);
    }

    private void limparCampos(){
        nome.set("");
        marca.set("");
        precoCompra.set(0);
        precoVenda.set(0);
        quantidade.set(0);
        validade.set("");
    }

    // Funções de retorno Property para a tabela
    public StringProperty nomeProperty(){
        return this.nome;
    }

    public StringProperty marcaProperty(){
        return this.marca;
    }

    public FloatProperty precoCompraProperty(){
        return this.precoCompra;
    }

    public FloatProperty precoVendaProperty(){
        return this.precoVenda;
    }

    public IntegerProperty quantidadeProperty(){
        return this.quantidade;
    }

    public StringProperty validadeProperty(){
        return this.validade;
    }

    public ObservableList<Produto> getListaTabelaProdutos(){
        return this.listaTabelaProdutos;
    }

    public ObservableList<Produto> getListaProdutosDisponiveis(){
        return this.listaProdutosDisponiveis;
    }   
}