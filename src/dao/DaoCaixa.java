package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import pojo.Caixa;
import util.MetodosGenericos;

public class DaoCaixa extends DaoPadrao {

//    public static final String SQLPESQUISAR = "SELECT CAIXA.ID, PESSOA.NOMERAZAOSOCIAL AS PESSOA_NOME, COMPRA.ID AS COMPRA_ID, CAIXA.DATA, CAIXA.VALORLIQUIDO, CONDICAOPAGAMENTO.DESCRICAO, CAIXA.QUITADA, COMPRA.CPGERADA FROM CAIXA"
//                                            + " LEFT JOIN COMPRA ON COMPRA.ID = CAIXA.IDCOMPRA"
//                                            + " LEFT JOIN PESSOA ON PESSOA.ID = CAIXA.IDPESSOA"
//                                            + " LEFT JOIN CONDICAOPAGAMENTO ON CONDICAOPAGAMENTO.ID = CAIXA.IDCONDICAOPAGAMENTO";
    public static final String SQLCONSULTARTUDO = "SELECT ID, DATA, SALDOINICIAL, SALDOFINAL, SALDO, SITUACAO FROM CAIXA";
    public static final String SQLCOMBOBOX = "SELECT CAIXA.ID, DATA, SITUACAO FROM CAIXA";
    public final String SQLCONSULTAR = "SELECT * FROM CAIXA WHERE ID = ?";
    private final String SQLINCLUIR = "INSERT INTO CAIXA VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    //private final String SQLALTERAR = "UPDATE CAIXA SET DATA = ?, SALDOINICIAL = ?, SALDOFINAL = ?, SALDOANTESFECHAMENTO = ?, SALDO = ?, SITUACAO = ? WHERE ID = ?";
    private final String SQLFECHARCAIXA = "UPDATE CAIXA SET DATAHRFECHAMENTO = ?, SALDOFINAL = ?, SALDOANTESFECHAMENTO = ?, SITUACAO = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM CAIXA WHERE ID = ?";
    private Caixa caixa;

    public DaoCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            caixa.setId(pegaGenerator("GEN_CAIXA_ID"));
            ps.setInt(1, caixa.getId());
            ps.setDate(2, MetodosGenericos.dataParaBanco(new Date())); //dataHrAbertura
            ps.setDate(3, MetodosGenericos.dataParaBanco(new Date())); //dataHrFechamento é setada como data atual no momento de incluir, depois na hora de fechar o caixa coloca data atual novamente
            ps.setBigDecimal(4, caixa.getSaldoInicial());
            ps.setBigDecimal(5, caixa.getSaldoFinal());
            ps.setBigDecimal(6, caixa.getSaldoAntesFechamento());
            ps.setBigDecimal(7, caixa.getSaldo());
            ps.setString(8, (caixa.isAberto() ? "Aberto" : "Fechado"));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível abrir o Caixa.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLFECHARCAIXA);
            //ps.setDate(1, MetodosGenericos.dataParaBanco(caixa.getData()));
            //ps.setBigDecimal(2, caixa.getSaldoInicial());
            ps.setDate(1, MetodosGenericos.dataParaBanco(new Date()));
            ps.setBigDecimal(2, caixa.getSaldoFinal());
            ps.setBigDecimal(3, caixa.getSaldoAntesFechamento());
            ps.setString(4, (caixa.isAberto() ? "Aberto" : "Fechado"));
            ps.setInt(5, caixa.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível fechar o Caixa.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            //DaoMovimentoCaixa.excluirItens(caixa);
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, caixa.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir o Caixa.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, caixa.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                caixa.setData(MetodosGenericos.dataDoBanco(rs.getDate(2)));
                caixa.setDataHrFechamento(MetodosGenericos.dataDoBanco(rs.getDate(3)));
                caixa.setSaldoInicial(rs.getBigDecimal(4));
                caixa.setSaldoFinal(rs.getBigDecimal(5));
                caixa.setSaldoAntesFechamento(rs.getBigDecimal(6));
                caixa.setSaldo(rs.getBigDecimal(7));
                caixa.setAberto(rs.getString(8).equals("Aberto"));
                //PROCEDIMENTO PARA CONSULTAR OS MOVIMENTOS PERTENCENTES À DETERMINADO CAIXA
                DaoMovimentoCaixa.consultarItens(caixa);
                List<Integer> controlePksItens = new ArrayList();
                for (int i = 0; i < caixa.getItensCaixa().size(); i++) {
                    controlePksItens.add(caixa.getItensCaixa().get(i).getId());
                }
                caixa.setControlePksItens(controlePksItens);
            } else {
                JOptionPane.showMessageDialog(null, "Caixa não encontrado.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o Caixa.");
            return false;
        }
    }
}
