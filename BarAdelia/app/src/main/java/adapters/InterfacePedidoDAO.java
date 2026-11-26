package adapters;

import java.util.List;

import entity.Pedido;

public interface InterfacePedidoDAO{
    public void criarPedido(Pedido p);
    public void addProduto(Pedido p);
    public void atualizarPedido(Pedido p);
    public List<Pedido> pesquisarTodos();
    public void excluir(Pedido p);
    public float fecharPedido(int numPedido);
}