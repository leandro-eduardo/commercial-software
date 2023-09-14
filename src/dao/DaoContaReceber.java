package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import pojo.ContaReceber;
import pojo.ParcelaContaReceber;
import util.MetodosGenericos;

public class DaoContaReceber extends DaoPadrao {

    public static final String SQLPESQUISAR = "SELECT CONTARECEBER.ID, PESSOA.NOMERAZAOSOCIAL AS PESSOA_NOME, VENDA.ID AS VENDA_ID, CONTARECEBER.DATA, CONTARECEBER.VALORLIQUIDO, CONDICAOPAGAMENTO.DESCRICAO, CONTARECEBER.QUITADA, VENDA.CRGERADA FROM CONTARECEBER"
                                            + " LEFT JOIN VENDA ON VENDA.ID = CONTARECEBER.IDVENDA"
                                            + " LEFT JOIN PESSOA ON PESSOA.ID = CONTARECEBER.IDPESSOA"
                                            + " LEFT JOIN CONDICAOPAGAMENTO ON CONDICAOPAGAMENTO.ID = CONTARECEBER.IDCONDICAOPAGAMENTO";
    public static final String SQLCOMBOBOX = "SELECT CONTARECEBER.ID FROM CONTARECEBER";
    public final String SQLCONSULTAR = "SELECT * FROM CONTARECEBER WHERE ID = ?";
    private final String SQLINCLUIR = "INSERT INTO CONTARECEBER VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE CONTARECEBER SET DATA = ?, VALORTOTAL = ?, DESCONTO = ?, VALORLIQUIDO = ?, VALORPENDENTE = ?, QUITADA = ?, DESCRICAO = ?, IDVENDA = ?, IDPESSOA = ?, IDCONDICAOPAGAMENTO = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM CONTARECEBER WHERE ID = ?";
    private ContaReceber contaReceber;

    public DaoContaReceber(ContaReceber contaReceber) {
        this.contaReceber = contaReceber;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            contaReceber.setId(pegaGenerator("GEN_CONTARECEBER_ID"));
            ps.setInt(1, contaReceber.getId());
            ps.setDate(2, MetodosGenericos.dataParaBanco(contaReceber.getData()));
            ps.setBigDecimal(3, contaReceber.getValortotal());
            ps.setBigDecimal(4, contaReceber.getDesconto());
            ps.setBigDecimal(5, contaReceber.getValorliquido());
            ps.setBigDecimal(6, contaReceber.getValorpendente());
            ps.setString(7, (contaReceber.isQuitada() ? "S" : "N"));
            ps.setString(8, contaReceber.getDescricao());
            if ((contaReceber.getVenda().getId() == 0)) {
                ps.setNull(9, java.sql.Types.INTEGER);
            } else {
                ps.setInt(9, contaReceber.getVenda().getId());
            }
            ps.setInt(10, contaReceber.getPessoa().getId());
            ps.setInt(11, contaReceber.getCondicaoPagamento().getId());
            ps.executeUpdate();
            for (int i = 0; i < contaReceber.getItensContaReceber().size(); i++) {
                DaoParcelaContaReceber daoParcelaContaReceber = new DaoParcelaContaReceber(contaReceber.getItensContaReceber().get(i));
                contaReceber.getItensContaReceber().get(i).setContaReceber(contaReceber);
                daoParcelaContaReceber.incluir();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir a conta a receber.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setDate(1, MetodosGenericos.dataParaBanco(contaReceber.getData()));
            ps.setBigDecimal(2, contaReceber.getValortotal());
            ps.setBigDecimal(3, contaReceber.getDesconto());
            ps.setBigDecimal(4, contaReceber.getValorliquido());
            ps.setBigDecimal(5, contaReceber.getValorpendente());
            ps.setString(6, (contaReceber.isQuitada() ? "S" : "N"));
            ps.setString(7, contaReceber.getDescricao());
            if ((contaReceber.getVenda().getId() == 0)) {
                ps.setNull(8, java.sql.Types.INTEGER);
            } else {
                ps.setInt(8, contaReceber.getVenda().getId());
            }
            ps.setInt(9, contaReceber.getPessoa().getId());
            ps.setInt(10, contaReceber.getCondicaoPagamento().getId());
            ps.setInt(11, contaReceber.getId());
            ps.executeUpdate();
            for (int i = 0; i < contaReceber.getItensContaReceber().size(); i++) {
                DaoParcelaContaReceber daoParcelaContaReceber = new DaoParcelaContaReceber(contaReceber.getItensContaReceber().get(i));
                if (contaReceber.getItensContaReceber().get(i).getId() > 0) {
                    daoParcelaContaReceber.alterar();
                } else {
                    contaReceber.getItensContaReceber().get(i).setContaReceber(contaReceber);
                    daoParcelaContaReceber.incluir();
                }
            }
            for (int i = 0; i < contaReceber.getControlePksItens().size(); i++) {
                boolean achou = false;
                for (int ii = 0; ii < contaReceber.getItensContaReceber().size(); ii++) {
                    if (contaReceber.getControlePksItens().get(i)
                            == contaReceber.getItensContaReceber().get(ii).getId()) {
                        achou = true;
                    }
                }
                if (achou == false) {
                    ParcelaContaReceber parcelaContaReceber = new ParcelaContaReceber();
                    parcelaContaReceber.setId(contaReceber.getControlePksItens().get(i));
                    DaoParcelaContaReceber daoParcelaContaReceber = new DaoParcelaContaReceber(parcelaContaReceber);
                    daoParcelaContaReceber.excluir();
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar a conta a receber.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            DaoParcelaContaReceber.excluirItens(contaReceber);
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, contaReceber.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir a conta a receber.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, contaReceber.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                contaReceber.setData(MetodosGenericos.dataDoBanco(rs.getDate(2)));
                contaReceber.setValortotal(rs.getBigDecimal(3));
                contaReceber.setDesconto(rs.getBigDecimal(4));
                contaReceber.setValorliquido(rs.getBigDecimal(5));
                contaReceber.setValorpendente(rs.getBigDecimal(6));
                contaReceber.setQuitada(rs.getString(7).equals("S"));
                contaReceber.setDescricao(rs.getString(8));
                //if (contaReceber.getVenda().getId() != 0) {
                contaReceber.getVenda().setId(rs.getInt(9));
                //} else {
                //contaReceber.getVenda().setId(0);
                //}
                //if (contaReceber.getPessoa().getId() != 0) {
                contaReceber.getPessoa().setId(rs.getInt(10));
                //}
                contaReceber.getCondicaoPagamento().setId(rs.getInt(11));
                DaoPessoa pessoa = new DaoPessoa(contaReceber.getPessoa());
                pessoa.consultar();
                DaoParcelaContaReceber.consultarItens(contaReceber);
                List<Integer> controlePksItens = new ArrayList();
                for (int i = 0; i < contaReceber.getItensContaReceber().size(); i++) {
                    controlePksItens.add(contaReceber.getItensContaReceber().get(i).getId());
                }
                contaReceber.setControlePksItens(controlePksItens);
            } else {
                JOptionPane.showMessageDialog(null, "Conta a receber não encontrada.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a conta a receber.");
            return false;
        }
    }
}
