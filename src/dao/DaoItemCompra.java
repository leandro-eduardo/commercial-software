package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import pojo.Compra;
import pojo.ItemCompra;

public class DaoItemCompra extends DaoPadrao {
    private static final String SQL_EXCLUIR_ITENS =
            "DELETE FROM ITEMCOMPRA WHERE IDCOMPRA = ?";
    private static final String SQL_CONSULTAR_ITENS =
            "SELECT * FROM ITEMCOMPRA WHERE IDCOMPRA = ?";
    private final String SQL_INCLUIR =
            "INSERT INTO ITEMCOMPRA VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String SQL_ALTERAR =
            "UPDATE ITEMCOMPRA SET QUANTIDADE = ?, VALORUNITARIO = ?, DESCONTO = ?,"
                    + " VALORTOTAL = ?, IDCOMPRA = ?, IDPRODUTO = ? WHERE ID = ?";
    private final String SQL_EXCLUIR =
            "DELETE FROM ITEMCOMPRA WHERE ID = ?";
    private final String SQL_CONSULTAR =
            "SELECT * FROM ITEMCOMPRA WHERE ID = ?";
    private ItemCompra itemCompra;

    public DaoItemCompra(ItemCompra itemCompra) {
        this.itemCompra = itemCompra;
    }
    
    public static boolean excluirItens(Compra compra) {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_EXCLUIR_ITENS);
            ps.setInt(1, compra.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir os itens de compra.");
            return false;
        }
    }

    public static void consultarItens(Compra compra) {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_CONSULTAR_ITENS);
            ps.setInt(1, compra.getId());
            ResultSet rs = ps.executeQuery();
            List<ItemCompra> itens = new ArrayList();
            ItemCompra itemCompra;
            while (rs.next()) {
                itemCompra = new ItemCompra();
                itemCompra.setId(rs.getInt(1));
                itemCompra.setQuantidade(rs.getInt(2));
                itemCompra.setValorUnitario(rs.getBigDecimal(3));
                itemCompra.setDesconto(rs.getBigDecimal(4));
                itemCompra.setValorTotal(rs.getBigDecimal(5));
                itemCompra.getCompra().setId(rs.getInt(6));
                itemCompra.getProduto().setId(rs.getInt(7));
                DaoProduto daoProduto = new DaoProduto(itemCompra.getProduto());
                daoProduto.consultar();
                itens.add(itemCompra);
            }
            compra.setItensCompra(itens);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar os itens de compra.");
            e.printStackTrace();
        }
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_INCLUIR);
            itemCompra.setId(pegaGenerator("GEN_ITEMCOMPRA_ID"));
            ps.setInt(1, itemCompra.getId());
            ps.setInt(2, itemCompra.getQuantidade());
            ps.setBigDecimal(3, itemCompra.getValorUnitario());
            ps.setBigDecimal(4, itemCompra.getDesconto());
            ps.setBigDecimal(5, itemCompra.getValorTotal());
            ps.setInt(6, itemCompra.getCompra().getId());
            ps.setInt(7, itemCompra.getProduto().getId());
            System.out.println("ID COMPRA -> " + itemCompra.getCompra().getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar incluir o Item de Compra.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_ALTERAR);
            ps.setInt(1, itemCompra.getQuantidade());
            ps.setBigDecimal(2, itemCompra.getValorUnitario());
            ps.setBigDecimal(3, itemCompra.getDesconto());
            ps.setBigDecimal(4, itemCompra.getValorTotal());
            ps.setInt(5, itemCompra.getCompra().getId());
            ps.setInt(6, itemCompra.getProduto().getId());
            ps.setInt(7, itemCompra.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar alterar o Item de Compra.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_EXCLUIR);
            ps.setInt(1, itemCompra.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir o Item de Compra.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_CONSULTAR);
            ps.setInt(1, itemCompra.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                itemCompra.setQuantidade(rs.getInt(2));
                itemCompra.setValorUnitario(rs.getBigDecimal(3));
                itemCompra.setDesconto(rs.getBigDecimal(4));
                itemCompra.setValorTotal(rs.getBigDecimal(5));
                itemCompra.getCompra().setId(rs.getInt(6));
                itemCompra.getProduto().setId(rs.getInt(7));
                DaoProduto produto = new DaoProduto(itemCompra.getProduto());
                produto.consultar();
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o Item de Compra.");
            e.printStackTrace();
            return false;
        }
    }
}