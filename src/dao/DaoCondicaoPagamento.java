package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.CondicaoPagamento;

public class DaoCondicaoPagamento extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM CONDICAOPAGAMENTO";
    public static final String SQLPESQUISAR = "SELECT ID, DESCRICAO, PARCELAS, CARENCIA, PRAZO, ATIVO FROM CONDICAOPAGAMENTO";
    public static final String SQLPESQUISAR2 = "SELECT ID, DESCRICAO, PARCELAS, CARENCIA, PRAZO, ATIVO FROM CONDICAOPAGAMENTO WHERE ATIVO = 'Ativo'";
    public static final String SQLCOMBOBOX = "SELECT ID, DESCRICAO FROM CONDICAOPAGAMENTO WHERE ATIVO = 'Ativo' ORDER BY DESCRICAO";
    private final String SQLINCLUIR = "INSERT INTO CONDICAOPAGAMENTO VALUES (?, ?, ?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE CONDICAOPAGAMENTO SET DESCRICAO = ?,  PARCELAS = ?, CARENCIA = ?, PRAZO = ?, ATIVO = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM CONDICAOPAGAMENTO WHERE ID = ?";
    private final String SQLCONSULTAR = "SELECT * FROM CONDICAOPAGAMENTO WHERE ID = ?";
    private CondicaoPagamento condicaoPagamento;

    public DaoCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            condicaoPagamento.setId(pegaGenerator("GEN_CONDICAOPAGAMENTO_ID"));
            ps.setInt(1, condicaoPagamento.getId());
            ps.setString(2, condicaoPagamento.getDescricao());
            ps.setInt(3, condicaoPagamento.getParcelas());
            ps.setInt(4, condicaoPagamento.getCarencia());
            ps.setInt(5, condicaoPagamento.getPrazo());
            ps.setString(6, (condicaoPagamento.isAtivo() ? "Ativo" : "Inativo"));
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir a condição de pagamento.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, condicaoPagamento.getDescricao());
            ps.setInt(2, condicaoPagamento.getParcelas());
            ps.setInt(3, condicaoPagamento.getCarencia());
            ps.setInt(4, condicaoPagamento.getPrazo());
            ps.setString(5, (condicaoPagamento.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(6, condicaoPagamento.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar a condição de pagamento.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, condicaoPagamento.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir a condiçao de pagamento.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, condicaoPagamento.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                condicaoPagamento.setDescricao(rs.getString(2));
                condicaoPagamento.setParcelas(rs.getInt(3));
                condicaoPagamento.setCarencia(rs.getInt(4));
                condicaoPagamento.setPrazo(rs.getInt(5));
                condicaoPagamento.setAtivo(rs.getString(6).equals("Ativo"));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a condição de pagamento.");
            return false;
        }
    }
}
