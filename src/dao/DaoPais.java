package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Pais;

public class DaoPais extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM PAIS";
    public static final String SQLPESQUISAR = "SELECT ID, NOME, ATIVO FROM PAIS";
    public static final String SQLPESQUISAR2 = "SELECT ID, NOME, ATIVO FROM PAIS WHERE ATIVO = 'Ativo'";
    public static final String SQLCOMBOBOX = "SELECT ID, NOME FROM PAIS WHERE ATIVO = 'Ativo' ORDER BY NOME";
    private final String SQLINCLUIR = "INSERT INTO PAIS VALUES (?, ?, ?)";
    private final String SQLALTERAR = "UPDATE PAIS SET NOME = ?, ATIVO = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM PAIS WHERE ID = ?";
    private final String SQLCONSULTAR = "SELECT * FROM PAIS WHERE ID = ?";
    private Pais pais;

    public DaoPais(Pais pais) {
        this.pais = pais;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            pais.setId(pegaGenerator("GEN_PAIS_ID"));
            ps.setInt(1, pais.getId());
            ps.setString(2, pais.getNome());
            ps.setString(3, (pais.isAtivo() ? "Ativo" : "Inativo"));
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir o país.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, pais.getNome());
            ps.setString(2, (pais.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(3, pais.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar o país.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, pais.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir o país.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, pais.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pais.setNome(rs.getString(2));
                pais.setAtivo(rs.getString(3).equals("Ativo"));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o país.");
            return false;
        }
    }
}
