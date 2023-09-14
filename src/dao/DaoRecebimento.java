package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Recebimento;
import util.MetodosGenericos;

public class DaoRecebimento extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM RECEBIMENTO";
    public static final String SQLPESQUISAR = "SELECT RECEBIMENTO.ID, PESSOA.NOMERAZAOSOCIAL AS PESSOA_NOME, PARCELACONTARECEBER.ID AS PARCELACONTARECEBER_ID, RECEBIMENTO.DATA, RECEBIMENTO.VALOR, RECEBIMENTO.JUROS, RECEBIMENTO.MULTA, RECEBIMENTO.VALORTOTAL FROM RECEBIMENTO"
                                            + " LEFT JOIN PARCELACONTARECEBER ON PARCELACONTARECEBER.ID = RECEBIMENTO.IDPARCELACONTARECEBER"
                                            + " LEFT JOIN CONTARECEBER ON CONTARECEBER.ID = PARCELACONTARECEBER.IDCONTARECEBER"
                                            + " LEFT JOIN PESSOA ON PESSOA.ID = CONTARECEBER.IDPESSOA";
    public static final String SQLCOMBOBOX = "SELECT RECEBIMENTO.ID FROM RECEBIMENTO";
    public final String SQLCONSULTAR = "SELECT * FROM RECEBIMENTO WHERE ID = ?";
    private final String SQLINCLUIR = "INSERT INTO RECEBIMENTO VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE RECEBIMENTO SET DATA = ?, VALOR = ?, DESCONTO = ?, JUROS = ?, MULTA = ?, VALORTOTAL = ?, DESCRICAO = ?, IDPARCELACONTARECEBER = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM RECEBIMENTO WHERE ID = ?";
    private Recebimento recebimento;

    public DaoRecebimento(Recebimento recebimento) {
        this.recebimento = recebimento;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            recebimento.setId(pegaGenerator("GEN_RECEBIMENTO_ID"));
            ps.setInt(1, recebimento.getId());
            ps.setDate(2, MetodosGenericos.dataParaBanco(recebimento.getData()));
            ps.setBigDecimal(3, recebimento.getValor());
            ps.setBigDecimal(4, recebimento.getDesconto());
            ps.setBigDecimal(5, recebimento.getJuros());
            ps.setBigDecimal(6, recebimento.getMulta());
            ps.setBigDecimal(7, recebimento.getValortotal());
            ps.setString(8, recebimento.getDescricao());
            ps.setInt(9, recebimento.getParcelaContaReceber().getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir o Recebimento.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setDate(1, MetodosGenericos.dataParaBanco(recebimento.getData()));
            ps.setBigDecimal(2, recebimento.getValor());
            ps.setBigDecimal(3, recebimento.getDesconto());
            ps.setBigDecimal(4, recebimento.getJuros());
            ps.setBigDecimal(5, recebimento.getMulta());
            ps.setBigDecimal(6, recebimento.getValortotal());
            ps.setString(7, recebimento.getDescricao());
            ps.setInt(8, recebimento.getParcelaContaReceber().getId());
            ps.setInt(9, recebimento.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar o Recebimento.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, recebimento.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir o Recebimento.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, recebimento.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                recebimento.setData(MetodosGenericos.dataDoBanco(rs.getDate(2)));
                recebimento.setValor(rs.getBigDecimal(3));
                recebimento.setDesconto(rs.getBigDecimal(4));
                recebimento.setJuros(rs.getBigDecimal(5));
                recebimento.setMulta(rs.getBigDecimal(6));
                recebimento.setValortotal(rs.getBigDecimal(7));
                recebimento.setDescricao(rs.getString(8));
                recebimento.getParcelaContaReceber().setId(rs.getInt(9));
            } else {
                JOptionPane.showMessageDialog(null, "Recebimento não encontrado.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o recebimento.");
            return false;
        }
    }
}
