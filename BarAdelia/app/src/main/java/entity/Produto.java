package entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Produto {

    private Integer id;
    private String nome;
    private String marca;
    private float precoCompra;
    private float precoVenda;
    private LocalDate dataValidade;
    private int quantidade;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

    public void setMarca(String marca){
        this.marca = marca;
    }

    public String getMarca(){
        return this.marca;
    }
    
    public void setPrecoCompra(float preco){
        this.precoCompra = preco;
    }

    public float getPrecoCompra(){
        return this.precoCompra;
    }
    
    public void setPrecoVenda(float preco){
        this.precoVenda = preco;
    }

    public float getPrecoVenda(){
        return this.precoVenda;
    }

    public String getPrecos(){

        StringBuffer buffer = new StringBuffer();
        buffer.append(precoCompra);
        buffer.append(" - ");
        buffer.append(precoVenda);

        return buffer.toString();
    }
    
    public void setValidade(LocalDate date){
        this.dataValidade = date;
    }

    public String getValidade(){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataValidade.format(formato);
    }

    public void setQuantidade(int qnt){
        this.quantidade = qnt;
    }

    public int getQuantidade(){
        return this.quantidade;
    }

    public String toString(){
        return this.nome + " - " + this.marca;
    }

    public boolean equals(Produto p){
        return this.toString().equals(p.toString());
    }
}