package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Produto;

public class DaoProduto extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM PRODUTO";
    public static final String SQLPESQUISAR = "SELECT PRODUTO.ID, PRODUTO.NOME AS PRODUTO_NOME, PRODUTO.QUANTIDADE AS PRODUTO_QUANTIDADE, FABRICANTE.NOME AS FABRICANTE_NOME, CATEGORIA.NOME AS CATEGORIA_NOME, PRODUTO.ATIVO FROM PRODUTO, FABRICANTE, CATEGORIA WHERE FABRICANTE.ID = PRODUTO.IDFABRICANTE AND CATEGORIA.ID = PRODUTO.IDCATEGORIA";
    public static final String SQLPESQUISAR2 = "SELECT PRODUTO.ID, PRODUTO.NOME AS PRODUTO_NOME, PRODUTO.QUANTIDADE, FABRICANTE.NOME AS FABRICANTE_NOME, CATEGORIA.NOME AS CATEGORIA_NOME, PRODUTO.ATIVO FROM PRODUTO, FABRICANTE, CATEGORIA WHERE FABRICANTE.ID = PRODUTO.IDFABRICANTE AND CATEGORIA.ID = PRODUTO.IDCATEGORIA AND PRODUTO.ATIVO = 'Ativo'";
    public static final String SQLCOMBOBOX = "SELECT PRODUTO.ID, PRODUTO.NOME || ' - ' || FABRICANTE.NOME AS PRODUTO FROM PRODUTO, FABRICANTE WHERE PRODUTO.ATIVO = 'Ativo' AND FABRICANTE.ID = PRODUTO.IDFABRICANTE ORDER BY PRODUTO.NOME";
    public final String SQLCONSULTAR = "SELECT * FROM PRODUTO WHERE ID = ?";
    private final String SQLINCLUIR = "INSERT INTO PRODUTO VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE PRODUTO SET NOME = ?, PRECOCOMPRA = ?, PRECOVENDA = ?, VALORLUCRO = ?, QUANTIDADE = ?, ATIVO = ?, IDFABRICANTE = ?, IDCATEGORIA = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM PRODUTO WHERE ID = ?";
    private Produto produto;

    public DaoProduto(Produto produto) {
        this.produto = produto;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            produto.setId(pegaGenerator("GEN_PRODUTO_ID"));
            ps.setInt(1, produto.getId());
            ps.setString(2, produto.getNome());
            ps.setBigDecimal(3, produto.getPrecocompra());
            ps.setBigDecimal(4, produto.getPrecovenda());
            ps.setBigDecimal(5, produto.getValorlucro());
            ps.setInt(6, produto.getQuantidade());
            ps.setString(7, (produto.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(8, produto.getFabricante().getId());
            ps.setInt(9, produto.getCategoria().getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir o produto.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, produto.getNome());
            ps.setBigDecimal(2, produto.getPrecocompra());
            ps.setBigDecimal(3, produto.getPrecovenda());
            ps.setBigDecimal(4, produto.getValorlucro());
            ps.setInt(5, produto.getQuantidade());
            ps.setString(6, (produto.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(7, produto.getFabricante().getId());
            ps.setInt(8, produto.getCategoria().getId());
            ps.setInt(9, produto.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar o produto.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, produto.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir o produto.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, produto.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                produto.setId(rs.getInt(1));
                produto.setNome(rs.getString(2));
                produto.setPrecocompra(rs.getBigDecimal(3));
                produto.setPrecovenda(rs.getBigDecimal(4));
                produto.setValorlucro(rs.getBigDecimal(5));
                produto.setQuantidade(rs.getInt(6));
                produto.setAtivo(rs.getString(7).equals("Ativo"));
                produto.getFabricante().setId(rs.getInt(8));
                produto.getCategoria().setId(rs.getInt(9));
            } else {
                //JOptionPane.showMessageDialog(null, "Produto não encontrado.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o produto.");
            return false;
        }
    }
    
}
