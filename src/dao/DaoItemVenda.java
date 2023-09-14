package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import pojo.ItemVenda;
import pojo.Venda;

public class DaoItemVenda extends DaoPadrao {
    private static final String SQL_EXCLUIR_ITENS =
            "DELETE FROM ITEMVENDA WHERE IDVENDA = ?";
//    private static final String SQL_CONSULTAR_ITENS =
//            "SELECT * FROM ITEMVENDA WHERE IDVENDA = ?";
    private static final String SQL_CONSULTAR_ITENS =
            "SELECT IV.ID, IV.IDVENDA, IV.IDSERVICO, S.NOME, IV.IDPRODUTO, P.NOME, IV.QUANTIDADE, IV.VALORUNITARIO, IV.DESCONTO, IV.VALORTOTAL "
                    + "FROM ITEMVENDA IV "
                    + "LEFT JOIN SERVICO S USING (ID) "
                    + "LEFT JOIN PRODUTO P USING (ID) "
                    + "WHERE IV.IDVENDA = ?";
    private final String SQL_INCLUIR =
            "INSERT INTO ITEMVENDA VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQL_ALTERAR =
            "UPDATE ITEMVENDA SET QUANTIDADE = ?, VALORUNITARIO = ?, DESCONTO = ?,"
                    + " VALORTOTAL = ?, IDVENDA = ?, IDPRODUTO = ?, IDSERVICO = ? WHERE ID = ?";
    private final String SQL_EXCLUIR =
            "DELETE FROM ITEMVENDA WHERE ID = ?";
    private final String SQL_CONSULTAR =
            "SELECT IV.ID, IV.IDVENDA, IV.IDSERVICO, S.NOME, IV.IDPRODUTO, P.NOME, IV.QUANTIDADE, IV.VALORUNITARIO, IV.DESCONTO, IV.VALORTOTAL "
                    + "FROM ITEMVENDA IV "
                    + "LEFT JOIN SERVICO S USING (ID) "
                    + "LEFT JOIN PRODUTO P USING (ID) "
                    + "WHERE IV.ID = ?";
    private ItemVenda itemVenda;

    public DaoItemVenda(ItemVenda itemVenda) {
        this.itemVenda = itemVenda;
    }

    public static boolean excluirItens(Venda venda) {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_EXCLUIR_ITENS);
            ps.setInt(1, venda.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir os itens de venda.");
            return false;
        }
    }

    public static void consultarItens(Venda venda) {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_CONSULTAR_ITENS);
            ps.setInt(1, venda.getId());
            ResultSet rs = ps.executeQuery();
            List<ItemVenda> itens = new ArrayList();
            ItemVenda itemVenda;
            while (rs.next()) {
                itemVenda = new ItemVenda();
                itemVenda.setId(rs.getInt(1));
                itemVenda.getVenda().setId(rs.getInt(2));
                itemVenda.getServico().setId(rs.getInt(3));
                itemVenda.getServico().setNome(rs.getString(4));
                itemVenda.getProduto().setId(rs.getInt(5));
                itemVenda.getProduto().setNome(rs.getString(6));
                itemVenda.setQuantidade(rs.getInt(7));
                itemVenda.setValorUnitario(rs.getBigDecimal(8));
                itemVenda.setDesconto(rs.getBigDecimal(9));
                itemVenda.setValorTotal(rs.getBigDecimal(10));             
                if (itemVenda.getServico().getId() != 0) {
                    itemVenda.setTipo(1);
                } else {
                    itemVenda.setTipo(2);
                }
                DaoProduto daoProduto = new DaoProduto(itemVenda.getProduto());
                daoProduto.consultar();
                DaoServico daoServico = new DaoServico(itemVenda.getServico());
                daoServico.consultar();
                itens.add(itemVenda);
            }
            venda.setItensVenda(itens);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar os itens de venda.");
            e.printStackTrace();
        }
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_INCLUIR);
            itemVenda.setId(pegaGenerator("GEN_ITEMVENDA_ID"));
            ps.setInt(1, itemVenda.getId());
            ps.setInt(2, itemVenda.getQuantidade());
            ps.setBigDecimal(3, itemVenda.getValorUnitario());
            ps.setBigDecimal(4, itemVenda.getDesconto());
            ps.setBigDecimal(5, itemVenda.getValorTotal());
            ps.setInt(6, itemVenda.getVenda().getId());
            if (itemVenda.getProduto().getId() > 0) {
                ps.setInt(7, itemVenda.getProduto().getId());
            } else {
                ps.setNull(7, itemVenda.getProduto().getId());
            }
            if (itemVenda.getServico().getId() > 0) {
                ps.setInt(8, itemVenda.getServico().getId());
            } else {
                ps.setNull(8, itemVenda.getServico().getId());
            }
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar incluir o Item de Venda.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_ALTERAR);
            ps.setInt(1, itemVenda.getQuantidade());
            ps.setBigDecimal(2, itemVenda.getValorUnitario());
            ps.setBigDecimal(3, itemVenda.getDesconto());
            ps.setBigDecimal(4, itemVenda.getValorTotal());
            ps.setInt(5, itemVenda.getVenda().getId());
            if (itemVenda.getProduto().getId() > 0) {
                ps.setInt(6, itemVenda.getProduto().getId());
            } else {
                ps.setNull(6, itemVenda.getProduto().getId());
            }
            if (itemVenda.getServico().getId() > 0) {
                ps.setInt(7, itemVenda.getServico().getId());
            } else {
                ps.setNull(7, itemVenda.getServico().getId());
            }
            ps.setInt(8, itemVenda.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Houve um problema ao tentar alterar o Item de Venda.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_EXCLUIR);
            ps.setInt(1, itemVenda.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir o Item de Venda.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_CONSULTAR);
            ps.setInt(1, itemVenda.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {       
                itemVenda.setId(rs.getInt(1));
                itemVenda.getVenda().setId(rs.getInt(2));
                itemVenda.getServico().setId(rs.getInt(3));
                itemVenda.getServico().setNome(rs.getString(4));
                itemVenda.getProduto().setId(rs.getInt(5));
                itemVenda.getProduto().setNome(rs.getString(6));
                itemVenda.setQuantidade(rs.getInt(7));
                itemVenda.setValorUnitario(rs.getBigDecimal(8));
                itemVenda.setDesconto(rs.getBigDecimal(9));
                itemVenda.setValorTotal(rs.getBigDecimal(10));
                if (itemVenda.getServico().getId() != 0) {
                    itemVenda.setTipo(1);
                } else {
                    itemVenda.setTipo(2);
                }
                return true;
            } else {
                return false;
            }   
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o Item de Venda.");
            e.printStackTrace();
            return false;
        }
    }
}