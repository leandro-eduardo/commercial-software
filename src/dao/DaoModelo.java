package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Modelo;

public class DaoModelo extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM MODELO";
    public static final String SQLPESQUISAR = "SELECT MODELO.ID, MODELO.NOME AS MODELO_NOME, FABRICANTE.NOME AS FABRICANTE_NOME, MODELO.ATIVO FROM MODELO, FABRICANTE WHERE FABRICANTE.ID = MODELO.IDFABRICANTE";
    public static final String SQLPESQUISAR2 = "SELECT MODELO.ID, MODELO.NOME AS MODELO_NOME, FABRICANTE.NOME AS FABRICANTE_NOME, MODELO.ATIVO FROM MODELO, FABRICANTE WHERE FABRICANTE.ID = MODELO.IDFABRICANTE AND MODELO.ATIVO = 'Ativo'";
    public static final String SQLCOMBOBOX = "SELECT MODELO.ID, MODELO.NOME || ' - ' || FABRICANTE.NOME AS NOME FROM MODELO, FABRICANTE WHERE MODELO.ATIVO = 'Ativo' AND FABRICANTE.ID = MODELO.IDFABRICANTE ORDER BY MODELO.NOME";
    public final String SQLCONSULTAR = "SELECT * FROM MODELO WHERE ID = ?";
    private final String SQLINCLUIR = "INSERT INTO MODELO VALUES (?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE MODELO SET NOME = ?, ATIVO = ?, IDFABRICANTE = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM MODELO WHERE ID = ?";
    private Modelo modelo;

    public DaoModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            modelo.setId(pegaGenerator("GEN_MODELO_ID"));
            ps.setInt(1, modelo.getId());
            ps.setString(2, modelo.getNome());
            ps.setString(3, (modelo.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(4, modelo.getFabricante().getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir o modelo.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, modelo.getNome());
            ps.setString(2, (modelo.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(3, modelo.getFabricante().getId());
            ps.setInt(4, modelo.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar o modelo.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, modelo.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir o modelo.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, modelo.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                modelo.setNome(rs.getString(2));
                modelo.setAtivo(rs.getString(3).equals("Ativo"));
                modelo.getFabricante().setId(rs.getInt(4));
            } else {
                JOptionPane.showMessageDialog(null, "Modelo não encontrado.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o modelo.");
            return false;
        }
    }
}
