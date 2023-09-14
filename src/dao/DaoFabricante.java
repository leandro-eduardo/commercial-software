package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Fabricante;

public class DaoFabricante extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM FABRICANTE";
    public static final String SQLPESQUISAR = "SELECT ID, NOME, ATIVO FROM FABRICANTE";
    public static final String SQLPESQUISAR2 = "SELECT ID, NOME, ATIVO FROM FABRICANTE WHERE ATIVO = 'Ativo'";
    public static final String SQLCOMBOBOX = "SELECT ID, NOME FROM FABRICANTE WHERE ATIVO = 'Ativo' ORDER BY NOME";
    private final String SQLINCLUIR = "INSERT INTO FABRICANTE VALUES (?, ?, ?)";
    private final String SQLALTERAR = "UPDATE FABRICANTE SET NOME = ?, ATIVO = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM FABRICANTE WHERE ID = ?";
    private final String SQLCONSULTAR = "SELECT * FROM FABRICANTE WHERE ID = ?";
    private Fabricante fabricante;

    public DaoFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            fabricante.setId(pegaGenerator("GEN_FABRICANTE_ID"));
            ps.setInt(1, fabricante.getId());
            ps.setString(2, fabricante.getNome());
            ps.setString(3, (fabricante.isAtivo() ? "Ativo" : "Inativo"));
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir a fabricante.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, fabricante.getNome());
            ps.setString(2, (fabricante.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(3, fabricante.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar a fabricante.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, fabricante.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir a fabricante.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, fabricante.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                fabricante.setNome(rs.getString(2));
                fabricante.setAtivo(rs.getString(3).equals("Ativo"));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a fabricante.");
            return false;
        }
    }
}
