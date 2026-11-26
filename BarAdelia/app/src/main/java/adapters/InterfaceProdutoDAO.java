package adapters;

import java.util.List;

import entity.Produto;

public interface InterfaceProdutoDAO {
    
    public void criarProduto( Produto p );
    public void addValidade( Produto p );
    public List<Produto> pesquisarTodos();
    public List<Produto> pesquisarProdutos();
    public void excluir( Produto p );
    public void atualizar( Produto p, String validadeErrada );
}