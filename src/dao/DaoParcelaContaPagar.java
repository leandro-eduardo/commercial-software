package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import pojo.ContaPagar;
import pojo.ParcelaContaPagar;
import util.MetodosGenericos;

public class DaoParcelaContaPagar extends DaoPadrao {
    private static final String SQL_EXCLUIR_ITENS = "DELETE FROM PARCELACONTAPAGAR WHERE IDCONTAPAGAR = ?";
    private static final String SQL_CONSULTAR_ITENS = "SELECT * FROM PARCELACONTAPAGAR WHERE IDCONTAPAGAR = ?";
    private final String SQL_INCLUIR = "INSERT INTO PARCELACONTAPAGAR VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String SQL_ALTERAR = "UPDATE PARCELACONTAPAGAR SET VENCIMENTO = ? WHERE ID = ?";
    private final String SQL_EXCLUIR = "DELETE FROM PARCELACONTAPAGAR WHERE ID = ?";
    private final String SQL_CONSULTAR = "SELECT * FROM PARCELACONTAPAGAR WHERE ID = ?";
    public static final String SQL_PESQUISAR = "SELECT PARCELACONTAPAGAR.ID, PESSOA.NOMERAZAOSOCIAL AS PESSOA_NOME, CONTAPAGAR.ID AS CONTAPAGAR_ID, CONTAPAGAR.DATA AS CONTAPAGAR_DATA, PARCELACONTAPAGAR.VENCIMENTO AS VENCIMENTO, PARCELACONTAPAGAR.PARCELAS AS PARCELAS, PARCELACONTAPAGAR.VALOR AS VALOR, PARCELACONTAPAGAR.VALORPENDENTE AS VALORPENDENTE\n" +
                                         "FROM PARCELACONTAPAGAR, CONTAPAGAR, PESSOA\n" +
                                         "WHERE PARCELACONTAPAGAR.IDCONTAPAGAR = CONTAPAGAR.ID AND PESSOA.ID = CONTAPAGAR.IDPESSOA\n" +
                                         "AND PARCELACONTAPAGAR.QUITADA = 'N'";
    private ParcelaContaPagar parcelaContaPagar;

    public DaoParcelaContaPagar(ParcelaContaPagar parcelaContaPagar) {
        this.parcelaContaPagar = parcelaContaPagar;
    }

    public static boolean excluirItens(ContaPagar contaPagar) {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_EXCLUIR_ITENS);
            ps.setInt(1, contaPagar.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir as parcelas da conta a pagar.");
            return false;
        }
    }

    public static void consultarItens(ContaPagar contaPagar) {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_CONSULTAR_ITENS);
            ps.setInt(1, contaPagar.getId());
            ResultSet rs = ps.executeQuery();
            List<ParcelaContaPagar> itens = new ArrayList();
            ParcelaContaPagar parcelaContaPagar;
            while (rs.next()) {
                parcelaContaPagar = new ParcelaContaPagar();
                parcelaContaPagar.setId(rs.getInt(1));
                parcelaContaPagar.setVencimento(MetodosGenericos.dataDoBanco(rs.getDate(2)));
                parcelaContaPagar.setValor(rs.getBigDecimal(3));
                parcelaContaPagar.setValorPendente(rs.getBigDecimal(4));
                parcelaContaPagar.setParcelas(rs.getString(5));
                parcelaContaPagar.setQuitada(rs.getString(6).equals("S"));
                parcelaContaPagar.getContaPagar().setId(rs.getInt(7));
                itens.add(parcelaContaPagar);
            }
            contaPagar.setItensContaPagar(itens);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar as parcelas da conta a pagar.");
            e.printStackTrace();
        }
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_INCLUIR);
            parcelaContaPagar.setId(pegaGenerator("GEN_PARCELACONTAPAGAR_ID"));
            ps.setInt(1, parcelaContaPagar.getId());
            ps.setDate(2, MetodosGenericos.dataParaBanco(parcelaContaPagar.getVencimento()));
            ps.setBigDecimal(3, parcelaContaPagar.getValor());
            ps.setBigDecimal(4, parcelaContaPagar.getValorPendente());
            ps.setString(5, parcelaContaPagar.getParcelas());
            ps.setString(6, (parcelaContaPagar.isQuitada() ? "S" : "N"));
            ps.setInt(7, parcelaContaPagar.getContaPagar().getId());
            System.out.println("ID CONTA PAGAR -> " + parcelaContaPagar.getContaPagar().getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar incluir a parcela de conta a pagar.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_ALTERAR);
            ps.setDate(1, MetodosGenericos.dataParaBanco(parcelaContaPagar.getVencimento()));
            ps.setInt(2, parcelaContaPagar.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar alterar a parcela de conta a pagar.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_EXCLUIR);
            ps.setInt(1, parcelaContaPagar.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir a parcela de conta a pagar.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_CONSULTAR);
            ps.setInt(1, parcelaContaPagar.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                parcelaContaPagar.setVencimento(MetodosGenericos.dataDoBanco(rs.getDate(2)));
                parcelaContaPagar.setValor(rs.getBigDecimal(3));
                parcelaContaPagar.setValorPendente(rs.getBigDecimal(4));
                parcelaContaPagar.setParcelas(rs.getString(5));
                parcelaContaPagar.setQuitada(rs.getString(6).equals("S"));
                parcelaContaPagar.getContaPagar().setId(rs.getInt(7));
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a parcela de conta a pagar.");
            e.printStackTrace();
            return false;
        }
    }
}