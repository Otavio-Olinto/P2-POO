package adapters;

import entity.Produto;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoDAO implements InterfaceProdutoDAO {

    Connection con;

    public ProdutoDAO() { 
        try { 
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("Classe carregada com sucesso");
            con = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/bar",
                "root",
                "alunofatec");
            System.out.println("Conectado no banco de dados");
        } catch (ClassNotFoundException e) { 
            System.out.println("Classe Maria DB não encontrada");
        } catch (SQLException e) { 
            System.out.println("Erro ao conectar no banco de dados");
        }
    }

    @Override
    public void criarProduto( Produto p ){

        try{
            // Padrão de inserção na tabela Produto
            String sql = """
            INSERT INTO produto 
            (nome_produto, marca_produto, preco_compra_produto, preco_venda_produto)
            VALUES (?,?,?,?)
            """;

            // Criando variável de inserção para inserir Produto
            PreparedStatement stmt = con.prepareStatement( sql, 
                Statement.RETURN_GENERATED_KEYS );

            // Inserções
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getMarca());
            stmt.setFloat(3, p.getPrecoCompra());
            stmt.setFloat(4, p.getPrecoVenda());

            // Executando a atualização
            stmt.executeUpdate();

            // Pegando o resultado da inserção e sua chave gerada pelo banco
            ResultSet rs = stmt.getGeneratedKeys();

            // O ResultSet funciona como um cursor que fica apontando 
            // Antes da primiera posição. Ele percorre todos os registros
            // com o next ele acessa o próximo.
            // Como neste caso ele é o resultado da inserção
            // Ele está apontando para antes dela (nada no momento)
            // If(rs.next) verifica se foi inserido algo, se sim
            // Retorda o valor de uma coluna da tabela do banco, a  posição indicada
            // Como queremos o id auto incrementado e ele é a primeira coluna, por isso 1
            if (rs.next()) { 
                p.setId(rs.getInt(1));
            }

            addValidade( p );

        }catch(SQLException e){
            System.out.println("Erro ao inserir Produto no banco de dados");
        }

    }

    @Override
    public void addValidade( Produto p ){

        try{

            // Padrão de inserção na tabela Validade
            String sql = """
            INSERT INTO validade
            (cod_produto_validade, data_validade, quantidade_validade)
            VALUES (?,?,?)
            """;

            // Alterando variável de inserção para inserir Validade
            PreparedStatement stmt = con.prepareStatement( sql );

            // Realizando as inserções
            stmt.setInt(1, p.getId());

            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(p.getValidade(), formato);
            stmt.setDate(2, java.sql.Date.valueOf(data));

            stmt.setInt(3, p.getQuantidade());

            // Executando a atualização
            stmt.executeUpdate();

        }catch(SQLException e){
            System.out.println("Erro ao inserir Validade no banco de dados");
        }
    }

    @Override
    public List<Produto> pesquisarTodos(){

        List<Produto> lista = new ArrayList<>();

        try { 
            // Três aspas duplas permite escrever blocos de strings que pulam linhas
            // Sem isso, só da pra escrever string em numa linha
            String sql = """
                SELECT cod_produto, nome_produto, 
                marca_produto, preco_compra_produto, preco_venda_produto,
                quantidade_validade, data_validade FROM produto INNER JOIN
                validade ON cod_produto = cod_produto_validade
                ORDER BY nome_produto, marca_produto
                """;

            PreparedStatement stmt = con.prepareStatement( sql );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto p = new Produto();
                p.setId( rs.getInt("cod_produto") );
                p.setNome( rs.getString("nome_produto") );
                p.setMarca( rs.getString("marca_produto") );
                p.setPrecoCompra( rs.getFloat("preco_compra_produto") );
                p.setPrecoVenda( rs.getFloat("preco_venda_produto") );
                p.setValidade(rs.getDate("data_validade").toLocalDate());
                p.setQuantidade(rs.getInt("quantidade_validade"));
                lista.add( p );
            }
        } catch (SQLException e) { 
            System.out.println("Erro ao pesquisar no banco de dados");
        }

        return lista;
    }

    @Override
    public List<Produto> pesquisarProdutos(){

        List<Produto> lista = new ArrayList<>();

        try { 
            // Três aspas duplas permite escrever blocos de strings que pulam linhas
            // Sem isso, só da pra escrever string em numa linha
            String sql = """
                SELECT cod_produto, nome_produto, 
                marca_produto, preco_venda_produto, SUM(quantidade_validade) AS quantidade
                FROM produto INNER JOIN validade ON cod_produto = cod_produto_validade
                GROUP BY cod_produto
                ORDER BY nome_produto, marca_produto
                """;

            PreparedStatement stmt = con.prepareStatement( sql );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto p = new Produto();
                p.setId( rs.getInt("cod_produto") );
                p.setNome( rs.getString("nome_produto") );
                p.setMarca( rs.getString("marca_produto") );
                p.setPrecoVenda( rs.getFloat("preco_venda_produto") );
                p.setQuantidade(rs.getInt("quantidade"));

                if(p.getQuantidade() > 0){
                    lista.add( p );
                }
                
            }
        } catch (SQLException e) { 
            System.out.println("Erro ao pesquisar no banco de dados");
        }

        return lista;
    }

    @Override
    public void excluir( Produto p ){

        try{
            // Comando para deletar do banco
            String sql = "DELETE FROM validade WHERE cod_produto_validade = ? AND data_validade = ?";

            PreparedStatement stmt = con.prepareStatement( sql );
            stmt.setInt(1, p.getId());

            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(p.getValidade(), formato);
            stmt.setDate(2, java.sql.Date.valueOf(data));

            stmt.executeUpdate();

        }catch(SQLException e){
            System.out.println("Erro ao deletar produto do banco de dados");
        }
    }

    @Override
    public void atualizar(Produto p, String validadeErrada ){

        try{
            String sql = """
            UPDATE produto SET nome_produto = ?, marca_produto = ?, 
            preco_compra_produto = ?, preco_venda_produto = ?
            WHERE cod_produto = ?
            """;

            PreparedStatement stmt = con.prepareStatement( sql );

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getMarca());
            stmt.setFloat(3, p.getPrecoCompra());
            stmt.setFloat(4, p.getPrecoVenda());
            stmt.setInt(5, p.getId());

            stmt.executeUpdate();

            sql = """
                UPDATE validade SET data_validade = ?, quantidade_validade = ?
                WHERE cod_produto_validade = ? AND data_validade = ? 
                """;

            stmt = con.prepareStatement( sql );

            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(p.getValidade(), formato);
            stmt.setDate(1, java.sql.Date.valueOf(data));

            stmt.setInt(2, p.getQuantidade());
            stmt.setInt(3, p.getId());

            data = LocalDate.parse(validadeErrada, formato);
            stmt.setDate(4, java.sql.Date.valueOf(data));

            stmt.executeUpdate();

        }catch(SQLException e){
            System.out.println("Erro ao atualizar produto do banco de dados");
        }
        
    }
}