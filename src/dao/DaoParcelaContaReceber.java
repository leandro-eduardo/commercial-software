package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import pojo.ContaReceber;
import pojo.ParcelaContaReceber;
import util.MetodosGenericos;

public class DaoParcelaContaReceber extends DaoPadrao {
    private static final String SQL_EXCLUIR_ITENS = "DELETE FROM PARCELACONTARECEBER WHERE IDCONTARECEBER = ?";
    private static final String SQL_CONSULTAR_ITENS = "SELECT * FROM PARCELACONTARECEBER WHERE IDCONTARECEBER = ?";
    private final String SQL_INCLUIR = "INSERT INTO PARCELACONTARECEBER VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String SQL_ALTERAR = "UPDATE PARCELACONTARECEBER SET VENCIMENTO = ?, VALOR = ? WHERE ID = ?";
    private final String SQL_EXCLUIR = "DELETE FROM PARCELACONTARECEBER WHERE ID = ?";
    private final String SQL_CONSULTAR = "SELECT * FROM PARCELACONTARECEBER WHERE ID = ?";
    public static final String SQL_PESQUISAR = "SELECT PARCELACONTARECEBER.ID, PESSOA.NOMERAZAOSOCIAL AS PESSOA_NOME, CONTARECEBER.ID AS CONTARECEBER_ID, CONTARECEBER.DATA AS CONTARECEBER_DATA, PARCELACONTARECEBER.VENCIMENTO AS VENCIMENTO, PARCELACONTARECEBER.PARCELAS AS PARCELAS, PARCELACONTARECEBER.VALOR AS VALOR, PARCELACONTARECEBER.VALORPENDENTE AS VALORPENDENTE\n" +
                                         "FROM PARCELACONTARECEBER, CONTARECEBER, PESSOA\n" +
                                         "WHERE PARCELACONTARECEBER.IDCONTARECEBER = CONTARECEBER.ID AND PESSOA.ID = CONTARECEBER.IDPESSOA\n" +
                                         "AND PARCELACONTARECEBER.QUITADA = 'N'";
    private ParcelaContaReceber parcelaContaReceber;

    public DaoParcelaContaReceber(ParcelaContaReceber parcelaContaReceber) {
        this.parcelaContaReceber = parcelaContaReceber;
    }

    public static boolean excluirItens(ContaReceber contaReceber) {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_EXCLUIR_ITENS);
            ps.setInt(1, contaReceber.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir as parcelas da Conta a Receber.");
            return false;
        }
    }

    public static void consultarItens(ContaReceber contaReceber) {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_CONSULTAR_ITENS);
            ps.setInt(1, contaReceber.getId());
            ResultSet rs = ps.executeQuery();
            List<ParcelaContaReceber> itens = new ArrayList();
            ParcelaContaReceber parcelaContaReceber;
            while (rs.next()) {
                parcelaContaReceber = new ParcelaContaReceber();
                parcelaContaReceber.setId(rs.getInt(1));
                parcelaContaReceber.setVencimento(MetodosGenericos.dataDoBanco(rs.getDate(2)));
                parcelaContaReceber.setValor(rs.getBigDecimal(3));
                parcelaContaReceber.setValorPendente(rs.getBigDecimal(4));
                parcelaContaReceber.setParcelas(rs.getString(5));
                parcelaContaReceber.setQuitada(rs.getString(6).equals("S"));
                parcelaContaReceber.getContaReceber().setId(rs.getInt(7));
                itens.add(parcelaContaReceber);
            }
            contaReceber.setItensContaReceber(itens);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar as parcelas da Conta a Receber.");
            e.printStackTrace();
        }
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_INCLUIR);
            parcelaContaReceber.setId(pegaGenerator("GEN_PARCELACONTARECEBER_ID"));
            ps.setInt(1, parcelaContaReceber.getId());
            ps.setDate(2, MetodosGenericos.dataParaBanco(parcelaContaReceber.getVencimento()));
            ps.setBigDecimal(3, parcelaContaReceber.getValor());
            ps.setBigDecimal(4, parcelaContaReceber.getValorPendente());
            ps.setString(5, parcelaContaReceber.getParcelas());
            ps.setString(6, (parcelaContaReceber.isQuitada() ? "S" : "N"));
            ps.setInt(7, parcelaContaReceber.getContaReceber().getId());
            System.out.println("ID CONTA RECEBER -> " + parcelaContaReceber.getContaReceber().getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar incluir a parcela de Conta a Receber.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_ALTERAR);
            ps.setDate(1, MetodosGenericos.dataParaBanco(parcelaContaReceber.getVencimento()));
            ps.setBigDecimal(2, parcelaContaReceber.getValor());
            ps.setInt(3, parcelaContaReceber.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar alterar a parcela de Conta a Receber.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_EXCLUIR);
            ps.setInt(1, parcelaContaReceber.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir a parcela de Conta a Receber.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_CONSULTAR);
            ps.setInt(1, parcelaContaReceber.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                parcelaContaReceber.setVencimento(MetodosGenericos.dataDoBanco(rs.getDate(2)));
                parcelaContaReceber.setValor(rs.getBigDecimal(3));
                parcelaContaReceber.setValorPendente(rs.getBigDecimal(4));
                parcelaContaReceber.setParcelas(rs.getString(5));
                parcelaContaReceber.setQuitada(rs.getString(6).equals("S"));
                parcelaContaReceber.getContaReceber().setId(rs.getInt(7));
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a parcela de Conta a Receber.");
            e.printStackTrace();
            return false;
        }
    }
}