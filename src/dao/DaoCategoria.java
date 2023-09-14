package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Categoria;

public class DaoCategoria extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM CATEGORIA";
    public static final String SQLPESQUISAR = "SELECT ID, NOME, ATIVO FROM CATEGORIA";
    public static final String SQLPESQUISAR2 = "SELECT ID, NOME, ATIVO FROM CATEGORIA WHERE ATIVO = 'Ativo'";
    public static final String SQLCOMBOBOX = "SELECT ID, NOME FROM CATEGORIA WHERE ATIVO = 'Ativo' ORDER BY NOME";
    private final String SQLINCLUIR = "INSERT INTO CATEGORIA VALUES (?, ?, ?)";
    private final String SQLALTERAR = "UPDATE CATEGORIA SET NOME = ?, ATIVO = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM CATEGORIA WHERE ID = ?";
    private final String SQLCONSULTAR = "SELECT * FROM CATEGORIA WHERE ID = ?";
    private Categoria categoria;

    public DaoCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            categoria.setId(pegaGenerator("GEN_CATEGORIA_ID"));
            ps.setInt(1, categoria.getId());
            ps.setString(2, categoria.getNome());
            ps.setString(3, (categoria.isAtivo() ? "Ativo" : "Inativo"));
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir a categoria.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, categoria.getNome());
            ps.setString(2, (categoria.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(3, categoria.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar a categoria.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, categoria.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir a categoria.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, categoria.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                categoria.setNome(rs.getString(2));
                categoria.setAtivo(rs.getString(3).equals("Ativo"));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a categoria.");
            return false;
        }
    }
}
