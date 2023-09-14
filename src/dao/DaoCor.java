package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Cor;

public class DaoCor extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM COR";
    public static final String SQLPESQUISAR = "SELECT ID, NOME, ATIVO FROM COR";
    public static final String SQLPESQUISAR2 = "SELECT ID, NOME, ATIVO FROM COR WHERE ATIVO = 'Ativo'";
    public static final String SQLCOMBOBOX = "SELECT ID, NOME FROM COR WHERE ATIVO = 'Ativo' ORDER BY NOME";
    private final String SQLINCLUIR = "INSERT INTO COR VALUES (?, ?, ?)";
    private final String SQLALTERAR = "UPDATE COR SET NOME = ?, ATIVO = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM COR WHERE ID = ?";
    private final String SQLCONSULTAR = "SELECT * FROM COR WHERE ID = ?";
    private Cor cor;

    public DaoCor(Cor cor) {
        this.cor = cor;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            cor.setId(pegaGenerator("GEN_COR_ID"));
            ps.setInt(1, cor.getId());
            ps.setString(2, cor.getNome());
            ps.setString(3, (cor.isAtivo() ? "Ativo" : "Inativo"));
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir a cor.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, cor.getNome());
            ps.setString(2, (cor.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(3, cor.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar a cor.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, cor.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir a cor.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, cor.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cor.setNome(rs.getString(2));
                cor.setAtivo(rs.getString(3).equals("Ativo"));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a cor.");
            return false;
        }
    }
}
