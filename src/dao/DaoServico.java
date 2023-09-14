package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Servico;

public class DaoServico extends DaoPadrao {

    public static final String SQLPESQUISAR = "SELECT SERVICO.ID, SERVICO.NOME AS SERVICO_NOME, CATEGORIA.NOME AS CATEGORIA_NOME, SERVICO.ATIVO AS SERVICO_ATIVO FROM SERVICO, CATEGORIA WHERE CATEGORIA.ID = SERVICO.IDCATEGORIA";
    public static final String SQLPESQUISAR2 = "SELECT SERVICO.ID, SERVICO.NOME AS SERVICO_NOME, CATEGORIA.NOME AS CATEGORIA_NOME, SERVICO.ATIVO AS SERVICO_ATIVO FROM SERVICO, CATEGORIA WHERE CATEGORIA.ID = SERVICO.IDCATEGORIA AND SERVICO.ATIVO = 'Ativo'";
    public static final String SQLCOMBOBOX = "SELECT SERVICO.ID, SERVICO.NOME FROM SERVICO WHERE SERVICO.ATIVO = 'Ativo' ORDER BY SERVICO.NOME";
    public final String SQLCONSULTAR = "SELECT * FROM SERVICO WHERE ID = ?";
    public static final String SQLINATIVOS = "SELECT * FROM SERVICO";
    private final String SQLINCLUIR = "INSERT INTO SERVICO VALUES (?, ?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE SERVICO SET NOME = ?, VALOR = ?, ATIVO = ?, IDCATEGORIA = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM SERVICO WHERE ID = ?";
    private Servico servico;

    public DaoServico(Servico servico) {
        this.servico = servico;
    }
    
        public void setServico(Servico servico) {
        this.servico = servico;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            servico.setId(pegaGenerator("GEN_SERVICO_ID"));
            ps.setInt(1, servico.getId());
            ps.setString(2, servico.getNome());
            ps.setBigDecimal(3, servico.getValor());
            ps.setString(4, (servico.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(5, servico.getCategoria().getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir o serviço.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, servico.getNome());
            ps.setBigDecimal(2, servico.getValor());
            ps.setString(3, (servico.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(4, servico.getCategoria().getId());
            ps.setInt(5, servico.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar o serviço.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, servico.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir o serviço.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, servico.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                servico.setNome(rs.getString(2));
                servico.setValor(rs.getBigDecimal(3));
                servico.setAtivo(rs.getString(4).equals("Ativo"));
                servico.getCategoria().setId(rs.getInt(5));
            } else {
                //JOptionPane.showMessageDialog(null, "Serviço não encontrado.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o serviço.");
            return false;
        }
    }
}
