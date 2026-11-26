package adapters;

import entity.*;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class PedidoDAO implements InterfacePedidoDAO {

    Connection con;

    public PedidoDAO() { 
        try { 
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("Classe Pedido carregada com sucesso");
            con = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/bar",
                "root",
                "alunofatec");
            System.out.println("Conectado no banco de dados Pedido");
        } catch (ClassNotFoundException e) { 
            System.out.println("Classe Maria DB não encontrada");
        } catch (SQLException e) { 
            System.out.println("Erro ao conectar no banco de dados");
        }
    }

    @Override
    public void criarPedido(Pedido p){

        try{
            // Padrão de inserção na tabela Produto
            String sql = """
            INSERT INTO pedido 
            (num_mesa_pedido, desconto_pedido, status_pedido, descricao_pedido)
            VALUES (?,?,?,?)
            """;

            // Criando variável de inserção para inserir Produto
            PreparedStatement stmt = con.prepareStatement( sql, 
                Statement.RETURN_GENERATED_KEYS );

            // Inserções
            stmt.setInt(1, p.getNumMesa());
            stmt.setFloat(2, p.getDesconto());
            stmt.setString(3, p.getStatus());
            stmt.setString(4, p.getDescricao());

            // Executando a atualização
            stmt.executeUpdate();

            // Pegando o resultado da inserção e sua chave gerada pelo banco
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) { 
                p.setNumPedido(rs.getInt(1));
            }

            addProduto( p );

        }catch(SQLException e){
            System.out.println("Erro ao criar Pedido no banco de dados");
        }

    }

    @Override
    public void addProduto(Pedido p){
        try{
            // Padrão de inserção na tabela Produto
            String sql = """
            INSERT INTO itens_pedido 
            (cod_produto_itens, num_pedido_itens, quantidade_pedido)
            VALUES (?,?,?)
            """;

            // Criando variável de inserção para inserir Produto
            PreparedStatement stmt = con.prepareStatement( sql );

            // Inserções
            stmt.setInt(1, p.getProduto().getId());
            stmt.setInt(2, p.getNumPedido());
            stmt.setInt(3, p.getQuantidadePedido());

            // Executando a atualização
            stmt.executeUpdate();

        }catch(SQLException e){
            System.out.println("Erro ao inserir Produto no Pedido no banco de dados");
        }
    }

    @Override
    public void atualizarPedido(Pedido p){

        try{
            String sql = """
            UPDATE pedido SET num_mesa_pedido = ?, desconto_pedido = ?, 
            status_pedido = ?, descricao_pedido = ?
            WHERE num_pedido = ?
            """;

            PreparedStatement stmt = con.prepareStatement( sql );

            stmt.setInt(1, p.getNumMesa());
            stmt.setFloat(2, p.getDesconto());
            stmt.setString(3, p.getStatus());
            stmt.setString(4, p.getDescricao());
            stmt.setInt(5, p.getNumPedido());

            stmt.executeUpdate();

            atualizarItem(p);

        }catch(SQLException e){
            System.out.println("Erro ao atualizar pedido do banco de dados");
        }

    }

    private void atualizarItem(Pedido p){
        try{
            String sql = """
                UPDATE itens_pedido SET quantidade_pedido = ?
                WHERE cod_produto_itens = ? AND num_pedido_itens = ? 
                """;

            PreparedStatement stmt = con.prepareStatement( sql );

            stmt = con.prepareStatement( sql );

            stmt.setInt(1, p.getQuantidadePedido());

            stmt.setInt(2, p.getProduto().getId());
            stmt.setInt(3, p.getNumPedido());

            stmt.executeUpdate();

        }catch(SQLException e){
            System.out.println("Erro ao atualizar itens do pedido do banco de dados");
        }
    }

    @Override
    public List<Pedido> pesquisarTodos(){

        List<Pedido> lista = new ArrayList<>();

        try { 
            
            String sql = """   
                SELECT
                p.num_pedido,
                p.num_mesa_pedido,
                p.status_pedido,
                p.desconto_pedido,
                p.descricao_pedido,
                ip.cod_produto_itens,
                ip.quantidade_pedido,
                pro.nome_produto,
                pro.marca_produto,
                pro.preco_compra_produto,
                pro.preco_venda_produto
                FROM pedido p
                INNER JOIN itens_pedido ip
                ON p.num_pedido = ip.num_pedido_itens
                INNER JOIN produto pro
                ON ip.cod_produto_itens = pro.cod_produto
                ORDER BY status_pedido, num_pedido
            """;
            PreparedStatement stmt = con.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Pedido p = new Pedido();
            
                p.setNumPedido(rs.getInt("num_pedido"));
                p.setNumMesa(rs.getInt("num_mesa_pedido"));
                p.setQuantidadePedido(rs.getInt("quantidade_pedido"));
                p.setStatus(rs.getString("status_pedido"));
                p.setDesconto(rs.getFloat("desconto_pedido"));
                p.setDescricao(rs.getString("descricao_pedido"));
            
                Produto pro = new Produto();
            
                pro.setId(rs.getInt("cod_produto_itens"));
                pro.setNome(rs.getString("nome_produto"));
                pro.setMarca(rs.getString("marca_produto"));
                pro.setPrecoCompra(rs.getFloat("preco_compra_produto"));
                pro.setPrecoVenda(rs.getFloat("preco_venda_produto"));

                p.setProduto(pro);

                lista.add(p);

            }
            
        } catch (SQLException e) { 
            System.out.println("Erro ao pesquisar todos os pedidos banco de dados");
        }

        return lista;
    }

    @Override
    public void excluir(Pedido p){

        try{
            String sql = "DELETE FROM itens_pedido WHERE num_pedido_itens = ? AND cod_produto_itens = ?";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, p.getNumPedido());
            stmt.setInt(2, p.getProduto().getId());

            stmt.executeUpdate();

        }catch(Exception e){
            System.out.println("Erro ao deletar item do pedido");
        }

    }

    @Override
    public float fecharPedido(int numPedido){

        float valor = 0;

        try{
            String sql = """
                SELECT
                SUM(ip.quantidade_pedido * p.preco_venda_produto) AS valor_total,
                pe.desconto_pedido
                FROM itens_pedido ip
                INNER JOIN produto p
                ON p.cod_produto = ip.cod_produto_itens
                INNER JOIN pedido pe
                ON pe.num_pedido = ip.num_pedido_itens
                WHERE ip.num_pedido_itens = ?
                GROUP BY pe.num_pedido, pe.desconto_pedido;
            """;

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, numPedido);

            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                valor = rs.getFloat("valor_total") - 
                rs.getFloat("desconto_pedido");
            }

            sql = "UPDATE pedido SET status_pedido = ? WHERE num_pedido = ?";

            stmt = con.prepareStatement(sql);

            stmt.setString(1, "Finalizado");
            stmt.setInt(2, numPedido);

            stmt.executeUpdate();

        }catch(Exception e){
            System.out.println("Erro ao finalizar Pedido");
        }

        return valor;
    }
}