package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import pojo.Caixa;
import pojo.MovimentoCaixa;
import util.MetodosGenericos;

public class DaoMovimentoCaixa extends DaoPadrao {
    public static final String SQL_PESQUISAR = "SELECT * FROM MOVIMENTOCAIXA";
    private static final String SQL_EXCLUIR_ITENS = "DELETE FROM MOVIMENTOCAIXA WHERE IDCAIXA = ?";
    public static final String SQL_CONSULTAR_ITENS = "SELECT ID, DESCRICAO, DEBITOCREDITO, VALOR, DATA, HORATRANSACAO, IDPAGAMENTO, IDRECEBIMENTO FROM MOVIMENTOCAIXA WHERE IDCAIXA = ?";
    //public static final String SQL_CONSULTAR_DEZ_ITENS = "SELECT FIRST 10 ID, DESCRICAO, DEBITOCREDITO, VALOR, DATA, HORATRANSACAO, IDPAGAMENTO, IDRECEBIMENTO FROM MOVIMENTOCAIXA WHERE IDCAIXA = ?";
    private final String SQL_INCLUIR = "INSERT INTO MOVIMENTOCAIXA VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQL_ALTERAR = "UPDATE MOVIMENTOCAIXA SET DESCRICAO = ?, DEBITOCREDITO = ?, VALOR = ?, HORATRANSACAO = ?, IDCAIXA = ?, IDPAGAMENTO = ?, IDRECEBIMENTO WHERE ID = ?";
    private final String SQL_EXCLUIR = "DELETE FROM MOVIMENTOCAIXA WHERE ID = ?";
    private final String SQL_CONSULTAR = "SELECT * FROM MOVIMENTOCAIXA WHERE ID = ?";
   
    private MovimentoCaixa movimentoCaixa;

    public DaoMovimentoCaixa(MovimentoCaixa movimentoCaixa) {
        this.movimentoCaixa = movimentoCaixa;
    }

    public static boolean excluirItens(Caixa caixa) {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_EXCLUIR_ITENS);
            ps.setInt(1, caixa.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir os movimentos do caixa.");
            return false;
        }
    }

    public static void consultarItens(Caixa caixa) {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_CONSULTAR_ITENS);
            ps.setInt(1, caixa.getId());
            ResultSet rs = ps.executeQuery();
            List<MovimentoCaixa> itens = new ArrayList();
            MovimentoCaixa movimentoCaixa;
            while (rs.next()) {
                movimentoCaixa = new MovimentoCaixa();
                movimentoCaixa.setId(rs.getInt(1));
                movimentoCaixa.setDescricao(rs.getString(2));
                movimentoCaixa.setDebitoCredito(rs.getString(3));
                movimentoCaixa.setValor(rs.getBigDecimal(4));
                movimentoCaixa.setData(rs.getDate(5));
                movimentoCaixa.setHoraTransacao(rs.getDate(6));
                //movimentoCaixa.getCaixa().setId(rs.getInt(7)); //o caixa não precisa aparecer na tabela pois essa tabela está na tela de consulta de caixa onde o ID do caixa ficará visivel ao usuário
                movimentoCaixa.getPagamento().setId(rs.getInt(7));
                movimentoCaixa.getRecebimento().setId(rs.getInt(8));
                itens.add(movimentoCaixa);
            }
            caixa.setItensCaixa(itens);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar os movimentos do caixa.");
            e.printStackTrace();
        }
    }
    
    
    public static void consultarItensPaginados(Caixa caixa, String sql) {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(sql);
            ps.setInt(1, caixa.getId());
            ResultSet rs = ps.executeQuery();
            List<MovimentoCaixa> itens = new ArrayList();
            MovimentoCaixa movimentoCaixa;
            while (rs.next()) {
                movimentoCaixa = new MovimentoCaixa();
                movimentoCaixa.setId(rs.getInt(1));
                movimentoCaixa.setDescricao(rs.getString(2));
                movimentoCaixa.setDebitoCredito(rs.getString(3));
                movimentoCaixa.setValor(rs.getBigDecimal(4));
                movimentoCaixa.setData(rs.getDate(5));
                movimentoCaixa.setHoraTransacao(rs.getDate(6));
                //movimentoCaixa.getCaixa().setId(rs.getInt(7)); //o caixa não precisa aparecer na tabela pois essa tabela está na tela de consulta de caixa onde o ID do caixa ficará visivel ao usuário
                movimentoCaixa.getPagamento().setId(rs.getInt(7));
                movimentoCaixa.getRecebimento().setId(rs.getInt(8));
                itens.add(movimentoCaixa);
            }
            caixa.setItensCaixa(itens);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar os dez movimentos do caixa.");
            e.printStackTrace();
        }
    }
    
    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_INCLUIR);
            movimentoCaixa.setId(pegaGenerator("GEN_MOVIMENTOCAIXA_ID"));
            ps.setInt(1, movimentoCaixa.getId());
            ps.setString(2,movimentoCaixa.getDescricao());
            ps.setString(3, movimentoCaixa.getDebitoCredito());
            ps.setBigDecimal(4, movimentoCaixa.getValor());
            ps.setDate(5, MetodosGenericos.dataParaBanco(movimentoCaixa.getData()));
            ps.setDate(6, MetodosGenericos.dataParaBanco(new Date()));
            ps.setInt(7, movimentoCaixa.getCaixa().getId());
            ps.setNull(8, movimentoCaixa.getPagamento().getId()); //setNull pois sempre será um movimento avulso
            ps.setNull(9, movimentoCaixa.getRecebimento().getId()); //setNull pois sempre será um movimento avulso
            return Conexao.executaUpdate(ps);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar incluir o movimento de caixa.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_ALTERAR);
            ps.setString(1, movimentoCaixa.getDescricao());
            ps.setString(2, movimentoCaixa.getDebitoCredito());
            ps.setDate(3, MetodosGenericos.dataParaBanco(movimentoCaixa.getData()));
            ps.setDate(4, MetodosGenericos.dataParaBanco(movimentoCaixa.getHoraTransacao()));
            ps.setInt(5, movimentoCaixa.getCaixa().getId());
            ps.setInt(6, movimentoCaixa.getPagamento().getId());
            ps.setInt(7, movimentoCaixa.getRecebimento().getId());
            ps.setInt(8, movimentoCaixa.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar alterar o movimento de caixa.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_EXCLUIR);
            ps.setInt(1, movimentoCaixa.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir o movimento de caixa.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_CONSULTAR);
            ps.setInt(1, movimentoCaixa.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                movimentoCaixa.setDescricao(rs.getString(2));
                movimentoCaixa.setDebitoCredito(rs.getString(3));
                movimentoCaixa.setValor(rs.getBigDecimal(4));
                movimentoCaixa.setData(rs.getDate(5));
                movimentoCaixa.setHoraTransacao(rs.getTime(6));
                movimentoCaixa.getCaixa().setId(rs.getInt(7));
                movimentoCaixa.getPagamento().setId(rs.getInt(8));
                movimentoCaixa.getRecebimento().setId(rs.getInt(9)); 
            } else {
                JOptionPane.showMessageDialog(null, "Movimento não encontrado.");
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o movimento de caixa.");
            e.printStackTrace();
            return false;
        }
    }
}