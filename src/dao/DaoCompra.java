package dao;

import bd.Conexao;
import pojo.Compra;
import pojo.ItemCompra;
import util.MetodosGenericos;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DaoCompra extends DaoPadrao {

    public static final String SQLINATIVOS
            = "SELECT * FROM COMPRA";
    public static final String SQLPESQUISAR
            = "SELECT COMPRA.ID, PESSOA.NOMERAZAOSOCIAL, COMPRA.DATA,"
            + " COMPRA.VALORLIQUIDO, COMPRA.STATUS, COMPRA.CPGERADA FROM COMPRA, PESSOA"
            + " WHERE COMPRA.IDPESSOA = PESSOA.ID";
    public static final String SQLPESQUISAR2
            = "SELECT COMPRA.ID, PESSOA.NOMERAZAOSOCIAL, COMPRA.DATA,"
            + " COMPRA.VALORLIQUIDO, COMPRA.STATUS, COMPRA.CPGERADA FROM COMPRA, PESSOA"
            + " WHERE COMPRA.IDPESSOA = PESSOA.ID AND COMPRA.STATUS = 'CF' AND CPGERADA = 'N'";
    public static final String SQLCOMBOBOX = "SELECT ID, DATA FROM COMPRA";
    private final String SQLINCLUIR
            = "INSERT INTO COMPRA VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQLALTERAR
            = "UPDATE COMPRA SET DATA = ?, VALORTOTAL = ?, DESCONTO = ?, VALORLIQUIDO = ?,"
            + "                STATUS = ?, CPGERADA = ?, IDPESSOA = ?, IDCONDICAOPAGAMENTO = ? WHERE ID = ?";
    private final String SQLEXCLUIR
            = "DELETE FROM COMPRA WHERE ID = ?";
    private final String SQLCONSULTAR
            = "SELECT * FROM COMPRA WHERE ID = ?";
    private Compra compra;

    public DaoCompra(Compra compra) {
        this.compra = compra;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            compra.setId(pegaGenerator("GEN_COMPRA_ID"));
            ps.setInt(1, compra.getId());
            ps.setDate(2, MetodosGenericos.dataParaBanco(compra.getData()));
            ps.setBigDecimal(3, compra.getValorTotal());
            ps.setBigDecimal(4, compra.getDesconto());
            ps.setBigDecimal(5, compra.getValorLiquido());
            ps.setString(6, compra.getStatus());
            ps.setString(7, (compra.isCpGerada() ? "S" : "N"));
            ps.setInt(8, compra.getPessoa().getId());
            ps.setInt(9, compra.getCondicaoPagamento().getId());
            ps.executeUpdate();
            for (int i = 0; i < compra.getItensCompra().size(); i++) {
                DaoItemCompra daoItemCompra = new DaoItemCompra(compra.getItensCompra().get(i));
                compra.getItensCompra().get(i).setCompra(compra);
                daoItemCompra.incluir();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar incluir a compra.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setDate(1, MetodosGenericos.dataParaBanco(compra.getData()));
            ps.setBigDecimal(2, compra.getValorTotal());
            ps.setBigDecimal(3, compra.getDesconto());
            ps.setBigDecimal(4, compra.getValorLiquido());
            ps.setString(5, compra.getStatus());
            ps.setString(6, (compra.isCpGerada() ? "S" : "N"));
            ps.setInt(7, compra.getPessoa().getId());
            ps.setInt(8, compra.getCondicaoPagamento().getId());
            ps.setInt(9, compra.getId());
            ps.executeUpdate();
            for (int i = 0; i < compra.getItensCompra().size(); i++) {
                DaoItemCompra daoItemCompra = new DaoItemCompra(compra.getItensCompra().get(i));
                if (compra.getItensCompra().get(i).getId() > 0) {
                    daoItemCompra.alterar();
                } else {
                    compra.getItensCompra().get(i).setCompra(compra);
                    daoItemCompra.incluir();
                }
            }
            for (int i = 0; i < compra.getControlePksItens().size(); i++) {
                boolean achou = false;
                for (int ii = 0; ii < compra.getItensCompra().size(); ii++) {
                    if (compra.getControlePksItens().get(i)
                            == compra.getItensCompra().get(ii).getId()) {
                        achou = true;
                    }
                }
                if (achou == false) {
                    ItemCompra itemCompra = new ItemCompra();
                    itemCompra.setId(compra.getControlePksItens().get(i));
                    DaoItemCompra daoItemCompra = new DaoItemCompra(itemCompra);
                    daoItemCompra.excluir();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar alterar a Compra.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            DaoItemCompra.excluirItens(compra);
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, compra.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir a Compra.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, compra.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                compra.setData(MetodosGenericos.dataDoBanco(rs.getDate(2)));
                compra.setValorTotal(rs.getBigDecimal(3));
                compra.setDesconto(rs.getBigDecimal(4));
                compra.setValorLiquido(rs.getBigDecimal(5));
                compra.setStatus(rs.getString(6));
                compra.setCpGerada(rs.getString(7).equals("S"));
                compra.getPessoa().setId(rs.getInt(8));
                compra.getCondicaoPagamento().setId(rs.getInt(9));
                DaoPessoa pessoa = new DaoPessoa(compra.getPessoa());
                pessoa.consultar();
                DaoItemCompra.consultarItens(compra);
                List<Integer> controlePksItens = new ArrayList();
                for (int i = 0; i < compra.getItensCompra().size(); i++) {
                    controlePksItens.add(compra.getItensCompra().get(i).getId());
                }
                compra.setControlePksItens(controlePksItens);
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a compra.");
            e.printStackTrace();
            return false;
        }
    }
}
