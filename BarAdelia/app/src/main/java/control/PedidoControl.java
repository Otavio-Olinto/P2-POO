package control;

import entity.Pedido;
import entity.Produto;
import adapters.PedidoDAO;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

public class PedidoControl {

    // Criando as variáveis observáveis
    private IntegerProperty numPedido = new SimpleIntegerProperty();
    private IntegerProperty numMesa = new SimpleIntegerProperty();
    private ObjectProperty<Produto> produtoSelecionado = new SimpleObjectProperty<>();
    private IntegerProperty quantidadeProduto = new SimpleIntegerProperty();
    private FloatProperty desconto = new SimpleFloatProperty();
    private StringProperty descricao = new SimpleStringProperty();
    private FloatProperty valorTotal = new SimpleFloatProperty();
    private ObservableList<Pedido> listaTabelaPedido = FXCollections.observableArrayList();
    private Produto produtoAntigo =  new Produto();
    private PedidoDAO daoPedido = new PedidoDAO();

    public PedidoControl(){
        pesquisarTodos();
    }

    public void salvar() throws Exception{

        Pedido p = new Pedido();

        p.setNumPedido(numPedido.get());
        p.setNumMesa(numMesa.get());
        p.setProduto(produtoSelecionado.get());
        p.setQuantidadePedido(quantidadeProduto.get());
        p.setStatus("Aberto");
        p.setDesconto(desconto.get());
        p.setDescricao(descricao.get());

        if(numPedido.get() == 0){

            daoPedido.criarPedido(p);
            pesquisarTodos();
            throw new Exception("Pedido criado com sucesso!");
        }else{
            if(p.getProduto().equals( produtoAntigo )){
                daoPedido.atualizarPedido( p );
                pesquisarTodos();
                throw new Exception("Pedido num: " + p.getNumPedido() + 
                    " atualizado com sucesso!");
            }else{
                daoPedido.addProduto( p );
                pesquisarTodos();
                throw new Exception("Produto: " + p.getProduto().toString() +
                    "\nAdicionado ao pedido num: " + p.getNumPedido());
            }
            
        }

    }

    public void limparCampos(){
        numPedido.set(0);
        numMesa.set(0);
        produtoSelecionado.set(null);
        quantidadeProduto.set(0);
        valorTotal.set(0);
        produtoSelecionado.set(null);
        desconto.set(0);
        descricao.set("");
    }

    public void pesquisarTodos(){
        limparCampos();
        listaTabelaPedido.clear();

        listaTabelaPedido.addAll(daoPedido.pesquisarTodos());
    }

    public void pesquisarPedido(){

        listaTabelaPedido.clear();

        if(numPedido.get() != 0 ){

            List<Pedido> lista = new ArrayList<>(daoPedido.pesquisarTodos());

            for(Pedido p: lista){
                if(p.getNumPedido() == numPedido.get()){
                    listaTabelaPedido.add(p);
                }
            } 
            
            desconto.set(listaTabelaPedido.get(0).getDesconto());
            produtoSelecionado.set(null);
            quantidadeProduto.set(0);
            valorTotal.set(0);

        }else{
            pesquisarTodos();
        }
    }
    
    public void atualizarPedido(Pedido p){
        produtoAntigo = p.getProduto();
        numPedido.set(p.getNumPedido());
        numMesa.set(p.getNumMesa());
        produtoSelecionado.set(p.getProduto());
        quantidadeProduto.set(p.getQuantidadePedido());
        desconto.set(p.getDesconto());
        descricao.set(p.getDescricao());
    }

    public void excluirPedido(Pedido p){
        daoPedido.excluir(p);
        pesquisarTodos();
    }

    public void fecharPedido() throws Exception{

        if(numPedido.get() == 0){
            throw new Exception("Por favor!\nSeleceione um pedido para finalizar");
        }else{
            pesquisarPedido();

            Pedido p = listaTabelaPedido.get(0);
            numPedido.set(p.getNumPedido());
            numMesa.set(p.getNumMesa());
            desconto.set(p.getDesconto());
            valorTotal.set(daoPedido.fecharPedido(numPedido.get()));
        }
    }

    // Retorno das variáveis observáveis

    public IntegerProperty numPedidoProperty(){
        return numPedido;
    }

    public IntegerProperty numMesaProperty(){
        return numMesa;
    }

    public ObjectProperty<Produto> produtoSelecionadoProperty() {
        return produtoSelecionado;
    }

    public IntegerProperty quantidadeProdutoProperty(){
        return quantidadeProduto;
    }

    public FloatProperty descontoProperty(){
        return desconto;
    }

    public StringProperty descricaoProperty(){
        return descricao;
    }

    public FloatProperty valorTotalProperty(){
        return valorTotal;
    }

    public ObservableList<Pedido> getListaTabelaPedidos(){
        return listaTabelaPedido;
    }
}