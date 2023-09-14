package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import pojo.ItemVenda;
import pojo.Venda;
import util.MetodosGenericos;

public class DaoVenda extends DaoPadrao {

    public static final String SQLINATIVOS
            = "SELECT * FROM VENDA";

    public static final String SQLPESQUISAR
            = "SELECT VENDA.ID, PESSOA.NOMERAZAOSOCIAL, VENDA.DATA,"
            + " VENDA.VALORLIQUIDO, VENDA.STATUS, VENDA.CRGERADA FROM VENDA, PESSOA"
            + " WHERE VENDA.IDPESSOA = PESSOA.ID";
    public static final String SQLPESQUISAR2
            = "SELECT VENDA.ID, PESSOA.NOMERAZAOSOCIAL, VENDA.DATA,"
            + " VENDA.VALORLIQUIDO, VENDA.STATUS, VENDA.CRGERADA FROM VENDA, PESSOA"
            + " WHERE VENDA.IDPESSOA = PESSOA.ID AND VENDA.STATUS = 'VF' AND CRGERADA = 'N'";
    public static final String SQLCOMBOBOX = "SELECT ID, DATA FROM VENDA";
    private final String SQLINCLUIR
            = "INSERT INTO VENDA VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQLALTERAR
            = "UPDATE VENDA SET DATA = ?, VALORTOTAL = ?, DESCONTO = ?, VALORLIQUIDO = ?,"
            + "                STATUS = ?, CRGERADA = ?, IDPESSOA = ?, IDPESSOA1 = ?, IDVEICULO = ?, IDCONDICAOPAGAMENTO = ? WHERE ID = ?";
    private final String SQLEXCLUIR
            = "DELETE FROM VENDA WHERE ID = ?";
    private final String SQLCONSULTAR
            = "SELECT * FROM VENDA WHERE ID = ?";
    private Venda venda;

    public DaoVenda(Venda venda) {
        this.venda = venda;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            venda.setId(pegaGenerator("GEN_VENDA_ID"));
            ps.setInt(1, venda.getId());
            ps.setDate(2, MetodosGenericos.dataParaBanco(venda.getData()));
            ps.setBigDecimal(3, venda.getValorTotal());
            ps.setBigDecimal(4, venda.getDesconto());
            ps.setBigDecimal(5, venda.getValorLiquido());
            ps.setString(6, venda.getStatus());
            ps.setString(7, (venda.isCrGerada() ? "S" : "N"));
            ps.setInt(8, venda.getPessoa().getId());
            ps.setInt(9, venda.getPessoa1().getId());
            if (venda.getVeiculo().getId() == 0) {
                ps.setNull(10, venda.getVeiculo().getId());
            } else {
                ps.setInt(10, venda.getVeiculo().getId());
            }
            ps.setInt(11, venda.getCondicaoPagamento().getId());
            ps.executeUpdate();
            for (int i = 0; i < venda.getItensVenda().size(); i++) {
                DaoItemVenda daoItemVenda = new DaoItemVenda(venda.getItensVenda().get(i));
                venda.getItensVenda().get(i).setVenda(venda);
                daoItemVenda.incluir();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar incluir a venda.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setDate(1, MetodosGenericos.dataParaBanco(venda.getData()));
            ps.setBigDecimal(2, venda.getValorTotal());
            ps.setBigDecimal(3, venda.getDesconto());
            ps.setBigDecimal(4, venda.getValorLiquido());
            ps.setString(5, venda.getStatus());
            ps.setString(6, (venda.isCrGerada() ? "S" : "N"));
            ps.setInt(7, venda.getPessoa().getId());
            ps.setInt(8, venda.getPessoa1().getId());
            if (venda.getVeiculo().getId() == 0) {
                ps.setNull(9, venda.getVeiculo().getId());
            } else {
                ps.setInt(9, venda.getVeiculo().getId());
            }
            ps.setInt(10, venda.getCondicaoPagamento().getId());
            ps.setInt(11, venda.getId());
            ps.executeUpdate();
            for (int i = 0; i < venda.getItensVenda().size(); i++) {
                DaoItemVenda daoItemVenda = new DaoItemVenda(venda.getItensVenda().get(i));
                if (venda.getItensVenda().get(i).getId() > 0) {
                    daoItemVenda.alterar();
                } else {
                    venda.getItensVenda().get(i).setVenda(venda);
                    daoItemVenda.incluir();
                }
            }
            for (int i = 0; i < venda.getControlePksItens().size(); i++) {
                boolean achou = false;
                for (int ii = 0; ii < venda.getItensVenda().size(); ii++) {
                    if (venda.getControlePksItens().get(i)
                            == venda.getItensVenda().get(ii).getId()) {
                        achou = true;
                    }
                }
                if (achou == false) {
                    ItemVenda itemVenda = new ItemVenda();
                    itemVenda.setId(venda.getControlePksItens().get(i));
                    DaoItemVenda daoItemVenda = new DaoItemVenda(itemVenda);
                    daoItemVenda.excluir();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar alterar a Venda.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            DaoItemVenda.excluirItens(venda);
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, venda.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar excluir a Venda.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, venda.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                venda.setId(rs.getInt(1));
                venda.setData(MetodosGenericos.dataDoBanco(rs.getDate(2)));
                venda.setValorTotal(rs.getBigDecimal(3));
                venda.setDesconto(rs.getBigDecimal(4));
                venda.setValorLiquido(rs.getBigDecimal(5));
                venda.setStatus(rs.getString(6));
                venda.setCrGerada(rs.getString(7).equals("S"));
                venda.getPessoa().setId(rs.getInt(8));
                venda.getPessoa1().setId(rs.getInt(9));
                venda.getVeiculo().setId(rs.getInt(10));
                venda.getCondicaoPagamento().setId(rs.getInt(11));
                DaoPessoa pessoa = new DaoPessoa(venda.getPessoa());
                pessoa.consultar();
                DaoItemVenda.consultarItens(venda);
                List<Integer> controlePksItens = new ArrayList();
                for (int i = 0; i < venda.getItensVenda().size(); i++) {
                    controlePksItens.add(venda.getItensVenda().get(i).getId());
                }
                venda.setControlePksItens(controlePksItens);
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a venda.");
            e.printStackTrace();
            return false;
        }
    }
}
