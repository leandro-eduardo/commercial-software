package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import pojo.ContaPagar;
import pojo.ParcelaContaPagar;
import util.MetodosGenericos;

public class DaoContaPagar extends DaoPadrao {

    public static final String SQLPESQUISAR = "SELECT CONTAPAGAR.ID, PESSOA.NOMERAZAOSOCIAL AS PESSOA_NOME, COMPRA.ID AS COMPRA_ID, CONTAPAGAR.DATA, CONTAPAGAR.VALORLIQUIDO, CONDICAOPAGAMENTO.DESCRICAO, CONTAPAGAR.QUITADA, COMPRA.CPGERADA FROM CONTAPAGAR"
                                            + " LEFT JOIN COMPRA ON COMPRA.ID = CONTAPAGAR.IDCOMPRA"
                                            + " LEFT JOIN PESSOA ON PESSOA.ID = CONTAPAGAR.IDPESSOA"
                                            + " LEFT JOIN CONDICAOPAGAMENTO ON CONDICAOPAGAMENTO.ID = CONTAPAGAR.IDCONDICAOPAGAMENTO";
    public static final String SQLCOMBOBOX = "SELECT CONTAPAGAR.ID FROM CONTAPAGAR";
    public final String SQLCONSULTAR = "SELECT * FROM CONTAPAGAR WHERE ID = ?";
    private final String SQLINCLUIR = "INSERT INTO CONTAPAGAR VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE CONTAPAGAR SET DATA = ?, VALORTOTAL = ?, DESCONTO = ?, VALORLIQUIDO = ?, VALORPENDENTE = ?, QUITADA = ?, DESCRICAO = ?, IDCOMPRA = ?, IDPESSOA = ?, IDCONDICAOPAGAMENTO = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM CONTAPAGAR WHERE ID = ?";
    private ContaPagar contaPagar;

    public DaoContaPagar(ContaPagar contaPagar) {
        this.contaPagar = contaPagar;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            contaPagar.setId(pegaGenerator("GEN_CONTAPAGAR_ID"));
            ps.setInt(1, contaPagar.getId());
            ps.setDate(2, MetodosGenericos.dataParaBanco(contaPagar.getData()));
            ps.setBigDecimal(3, contaPagar.getValortotal());
            ps.setBigDecimal(4, contaPagar.getDesconto());
            ps.setBigDecimal(5, contaPagar.getValorliquido());
            ps.setBigDecimal(6, contaPagar.getValorpendente());
            ps.setString(7, (contaPagar.isQuitada() ? "S" : "N"));
            ps.setString(8, contaPagar.getDescricao());
            if ((contaPagar.getCompra().getId() == 0)) {
                ps.setNull(9, java.sql.Types.INTEGER);
            } else {
                ps.setInt(9, contaPagar.getCompra().getId());
            }
            ps.setInt(10, contaPagar.getPessoa().getId());
            ps.setInt(11, contaPagar.getCondicaoPagamento().getId());
            ps.executeUpdate();
            for (int i = 0; i < contaPagar.getItensContaPagar().size(); i++) {
                DaoParcelaContaPagar daoParcelaContaPagar = new DaoParcelaContaPagar(contaPagar.getItensContaPagar().get(i));
                contaPagar.getItensContaPagar().get(i).setContaPagar(contaPagar);
                daoParcelaContaPagar.incluir();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir a conta a pagar.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setDate(1, MetodosGenericos.dataParaBanco(contaPagar.getData()));
            ps.setBigDecimal(2, contaPagar.getValortotal());
            ps.setBigDecimal(3, contaPagar.getDesconto());
            ps.setBigDecimal(4, contaPagar.getValorliquido());
            ps.setBigDecimal(5, contaPagar.getValorpendente());
            ps.setString(6, (contaPagar.isQuitada() ? "S" : "N"));
            ps.setString(7, contaPagar.getDescricao());
            if ((contaPagar.getCompra().getId() == 0)) {
                ps.setNull(8, java.sql.Types.INTEGER);
            } else {
                ps.setInt(8, contaPagar.getCompra().getId());
            }
            ps.setInt(9, contaPagar.getPessoa().getId());
            ps.setInt(10, contaPagar.getCondicaoPagamento().getId());
            ps.setInt(11, contaPagar.getId());
            ps.executeUpdate();
            for (int i = 0; i < contaPagar.getItensContaPagar().size(); i++) {
                DaoParcelaContaPagar daoParcelaContaPagar = new DaoParcelaContaPagar(contaPagar.getItensContaPagar().get(i));
                if (contaPagar.getItensContaPagar().get(i).getId() > 0) {
                    daoParcelaContaPagar.alterar();
                } else {
                    contaPagar.getItensContaPagar().get(i).setContaPagar(contaPagar);
                    daoParcelaContaPagar.incluir();
                }
            }
            for (int i = 0; i < contaPagar.getControlePksItens().size(); i++) {
                boolean achou = false;
                for (int ii = 0; ii < contaPagar.getItensContaPagar().size(); ii++) {
                    if (contaPagar.getControlePksItens().get(i)
                            == contaPagar.getItensContaPagar().get(ii).getId()) {
                        achou = true;
                    }
                }
                if (achou == false) {
                    ParcelaContaPagar parcelaContaPagar = new ParcelaContaPagar();
                    parcelaContaPagar.setId(contaPagar.getControlePksItens().get(i));
                    DaoParcelaContaPagar daoParcelaContaPagar = new DaoParcelaContaPagar(parcelaContaPagar);
                    daoParcelaContaPagar.excluir();
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar a conta a pagar.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            DaoParcelaContaPagar.excluirItens(contaPagar);
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, contaPagar.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir a conta a pagar.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, contaPagar.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                contaPagar.setData(MetodosGenericos.dataDoBanco(rs.getDate(2)));
                contaPagar.setValortotal(rs.getBigDecimal(3));
                contaPagar.setDesconto(rs.getBigDecimal(4));
                contaPagar.setValorliquido(rs.getBigDecimal(5));
                contaPagar.setValorpendente(rs.getBigDecimal(6));
                contaPagar.setQuitada(rs.getString(7).equals("S"));
                contaPagar.setDescricao(rs.getString(8));
                //if (contaPagar.getCompra().getId() != 0) {
                contaPagar.getCompra().setId(rs.getInt(9));
                //} else {
                //contaPagar.getCompra().setId(0);
                //}
                //if (contaPagar.getPessoa().getId() != 0) {
                contaPagar.getPessoa().setId(rs.getInt(10));
                //}
                contaPagar.getCondicaoPagamento().setId(rs.getInt(11));
                DaoPessoa pessoa = new DaoPessoa(contaPagar.getPessoa());
                pessoa.consultar();
                DaoParcelaContaPagar.consultarItens(contaPagar);
                List<Integer> controlePksItens = new ArrayList();
                for (int i = 0; i < contaPagar.getItensContaPagar().size(); i++) {
                    controlePksItens.add(contaPagar.getItensContaPagar().get(i).getId());
                }
                contaPagar.setControlePksItens(controlePksItens);
            } else {
                JOptionPane.showMessageDialog(null, "Conta a pagar não encontrada.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a conta a pagar.");
            return false;
        }
    }
}
