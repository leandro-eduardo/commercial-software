package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Cidade;

public class DaoCidade extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM CIDADE";
    public static final String SQLPESQUISAR = "SELECT CIDADE.ID, CIDADE.NOME, ESTADO.NOME AS ESTADO_NOME, CIDADE.ATIVO FROM CIDADE, ESTADO WHERE ESTADO.ID = CIDADE.IDESTADO";
    public static final String SQLPESQUISAR2 = "SELECT CIDADE.ID, CIDADE.NOME, ESTADO.NOME AS ESTADO_NOME, CIDADE.ATIVO FROM CIDADE, ESTADO WHERE ESTADO.ID = CIDADE.IDESTADO AND CIDADE.ATIVO = 'Ativo'";
    public static final String SQLCOMBOBOX = "SELECT CIDADE.ID, CIDADE.NOME || ' - ' || ESTADO.SIGLA AS CIDADE FROM CIDADE, ESTADO WHERE CIDADE.IDESTADO = ESTADO.ID AND CIDADE.ATIVO = 'Ativo' ORDER BY CIDADE.NOME";
    public final String SQLCONSULTAR = "SELECT * FROM CIDADE WHERE ID = ?";
    private final String SQLINCLUIR = "INSERT INTO CIDADE VALUES (?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE CIDADE SET NOME = ?, ATIVO = ?, IDESTADO = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM CIDADE WHERE ID = ?";
    private Cidade cidade;

    public DaoCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            cidade.setId(pegaGenerator("GEN_CIDADE_ID"));
            ps.setInt(1, cidade.getId());
            ps.setString(2, cidade.getNome());
            ps.setString(3, (cidade.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(4, cidade.getEstado().getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir a cidade.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, cidade.getNome());
            ps.setString(2, (cidade.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(3, cidade.getEstado().getId());
            ps.setInt(4, cidade.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar a cidade.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, cidade.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir a cidade.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, cidade.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cidade.setId(rs.getInt(1));
                cidade.setNome(rs.getString(2));
                cidade.setAtivo(rs.getString(3).equals("Ativo"));
                cidade.getEstado().setId(rs.getInt(4));
            } else {
                JOptionPane.showMessageDialog(null, "Cidade não encontrada.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a cidade.");
            return false;
        }
    }
}
