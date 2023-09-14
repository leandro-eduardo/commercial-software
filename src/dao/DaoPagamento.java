package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Pagamento;
import util.MetodosGenericos;

public class DaoPagamento extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM PAGAMENTO";
    public static final String SQLPESQUISAR = "SELECT PAGAMENTO.ID, PESSOA.NOMERAZAOSOCIAL AS PESSOA_NOME, PARCELACONTAPAGAR.ID AS PARCELACONTAPAGAR_ID, PAGAMENTO.DATA, PAGAMENTO.VALOR, PAGAMENTO.JUROS, PAGAMENTO.MULTA, PAGAMENTO.VALORTOTAL FROM PAGAMENTO"
                                            + " LEFT JOIN PARCELACONTAPAGAR ON PARCELACONTAPAGAR.ID = PAGAMENTO.IDPARCELACONTAPAGAR"
                                            + " LEFT JOIN CONTAPAGAR ON CONTAPAGAR.ID = PARCELACONTAPAGAR.IDCONTAPAGAR"
                                            + " LEFT JOIN PESSOA ON PESSOA.ID = CONTAPAGAR.IDPESSOA";
    public static final String SQLCOMBOBOX = "SELECT PAGAMENTO.ID FROM PAGAMENTO";
    public final String SQLCONSULTAR = "SELECT * FROM PAGAMENTO WHERE ID = ?";
    private final String SQLINCLUIR = "INSERT INTO PAGAMENTO VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE PAGAMENTO SET DATA = ?, VALOR = ?, DESCONTO = ?, JUROS = ?, MULTA = ?, VALORTOTAL = ?, DESCRICAO = ?, IDPARCELACONTAPAGAR = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM PAGAMENTO WHERE ID = ?";
    private Pagamento pagamento;

    public DaoPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            pagamento.setId(pegaGenerator("GEN_PAGAMENTO_ID"));
            ps.setInt(1, pagamento.getId());
            ps.setDate(2, MetodosGenericos.dataParaBanco(pagamento.getData()));
            ps.setBigDecimal(3, pagamento.getValor());
            ps.setBigDecimal(4, pagamento.getDesconto());
            ps.setBigDecimal(5, pagamento.getJuros());
            ps.setBigDecimal(6, pagamento.getMulta());
            ps.setBigDecimal(7, pagamento.getValortotal());
            ps.setString(8, pagamento.getDescricao());
            ps.setInt(9, pagamento.getParcelaContaPagar().getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir o Pagamento.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setDate(1, MetodosGenericos.dataParaBanco(pagamento.getData()));
            ps.setBigDecimal(2, pagamento.getValor());
            ps.setBigDecimal(3, pagamento.getDesconto());
            ps.setBigDecimal(4, pagamento.getJuros());
            ps.setBigDecimal(5, pagamento.getMulta());
            ps.setBigDecimal(6, pagamento.getValortotal());
            ps.setString(7, pagamento.getDescricao());
            ps.setInt(8, pagamento.getParcelaContaPagar().getId());
            ps.setInt(9, pagamento.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar o Pagamento.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, pagamento.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir o Pagamento.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, pagamento.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pagamento.setData(MetodosGenericos.dataDoBanco(rs.getDate(2)));
                pagamento.setValor(rs.getBigDecimal(3));
                pagamento.setDesconto(rs.getBigDecimal(4));
                pagamento.setJuros(rs.getBigDecimal(5));
                pagamento.setMulta(rs.getBigDecimal(6));
                pagamento.setValortotal(rs.getBigDecimal(7));
                pagamento.setDescricao(rs.getString(8));
                pagamento.getParcelaContaPagar().setId(rs.getInt(9));
            } else {
                JOptionPane.showMessageDialog(null, "Pagamento não encontrado.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o pagamento.");
            return false;
        }
    }
}
