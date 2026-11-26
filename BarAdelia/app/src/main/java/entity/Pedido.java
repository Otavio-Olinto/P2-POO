package entity;

public class Pedido {
    
    private int numPedido;
    private int numMesa;
    private Produto produto = new Produto();
    private int quantidadePedido;
    private String status;
    private float desconto;
    private float valorTotal;
    private String descricao;

    public void setNumPedido(int num){
        this.numPedido = num;
    }

    public int getNumPedido(){
        return this.numPedido;
    }

    public void setNumMesa(int num){
        this.numMesa = num;
    }

    public int getNumMesa(){
        return this.numMesa;
    }
    
    public void setProduto(Produto produto){
        this.produto = produto;
    }

    public void setQuantidadePedido(int num){
        this.quantidadePedido = num;
    }

    public int getQuantidadePedido(){
        return this.quantidadePedido;
    }

    public Produto getProduto(){
        return this.produto;
    }
    
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return this.status;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return this.descricao;
    }
    
    public void setDesconto(float desconto){
        this.desconto = desconto;
    }

    public float getDesconto(){
        return this.desconto;
    }
    
    public void setValorTotal(float valor){
        this.valorTotal = valor;
    }

    public float getValorTotal() {
        return this.valorTotal;
    }
}