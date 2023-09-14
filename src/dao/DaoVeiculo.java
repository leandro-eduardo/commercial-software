package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Veiculo;

public class DaoVeiculo extends DaoPadrao {

    public static final String SQLPESQUISAR = "SELECT VEICULO.ID, MODELO.NOME AS MODELO_NOME, VEICULO.PLACA, FABRICANTE.NOME AS FABRICANTE_NOME, COR.NOME AS COR_NOME, VEICULO.ANOFABRICACAO, VEICULO.ATIVO AS VEICULO_ATIVO FROM VEICULO, MODELO, COR, FABRICANTE WHERE MODELO.ID = VEICULO.IDMODELO AND COR.ID = VEICULO.IDCOR AND MODELO.IDFABRICANTE = FABRICANTE.ID";
    public static final String SQLPESQUISAR2 = "SELECT VEICULO.ID, MODELO.NOME AS MODELO_NOME, VEICULO.PLACA, FABRICANTE.NOME AS FABRICANTE_NOME, COR.NOME AS COR_NOME, VEICULO.ANOFABRICACAO, VEICULO.ATIVO AS VEICULO_ATIVO FROM VEICULO, MODELO, COR, FABRICANTE WHERE MODELO.ID = VEICULO.IDMODELO AND COR.ID = VEICULO.IDCOR AND MODELO.IDFABRICANTE = FABRICANTE.ID AND VEICULO.ATIVO = 'Ativo'";
    public static final String SQLCOMBOBOX = "SELECT VEICULO.ID, MODELO.NOME, VEICULO.PLACA FROM VEICULO, MODELO WHERE VEICULO.ATIVO = 'Ativo' AND MODELO.ID = VEICULO.IDMODELO ORDER BY MODELO.NOME";
    public final String SQLCONSULTAR = "SELECT * FROM VEICULO WHERE ID = ?";
    public final String SQLINATIVOS = "SELECT * FROM VEICULO";
    private final String SQLINCLUIR = "INSERT INTO VEICULO VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE VEICULO SET PLACA = ?, ANOFABRICACAO = ?, CHASSI = ?, ATIVO = ?, IDCOR = ?, IDMODELO = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM VEICULO WHERE ID = ?";
    private Veiculo veiculo;

    public DaoVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            veiculo.setId(pegaGenerator("GEN_VEICULO_ID"));
            ps.setInt(1, veiculo.getId());
            ps.setString(2, veiculo.getPlaca());
            ps.setString(3, veiculo.getChassi());
            ps.setString(4, veiculo.getAnofabricacao());
            ps.setString(5, (veiculo.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(6, veiculo.getCor().getId());
            ps.setInt(7, veiculo.getModelo().getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir o veiculo.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, veiculo.getPlaca());
            ps.setString(2, veiculo.getAnofabricacao());
            ps.setString(3, veiculo.getChassi());
            ps.setString(4, (veiculo.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(5, veiculo.getCor().getId());
            ps.setInt(6, veiculo.getModelo().getId());
            ps.setInt(7, veiculo.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar o veiculo.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, veiculo.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir o veiculo.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, veiculo.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                veiculo.setPlaca(rs.getString(2));
                veiculo.setChassi(rs.getString(3));
                veiculo.setAnofabricacao(rs.getString(4));
                veiculo.setAtivo(rs.getString(5).equals("Ativo"));
                veiculo.getCor().setId(rs.getInt(6));
                veiculo.getModelo().setId(rs.getInt(7));
            } else {
                JOptionPane.showMessageDialog(null, "Veiculo não encontrado.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o veiculo.");
            return false;
        }
    }
}
