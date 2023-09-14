package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Estado;

public class DaoEstado extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM ESTADO";
    public static final String SQLPESQUISAR = "SELECT ESTADO.ID, ESTADO.NOME AS ESTADO_NOME, ESTADO.SIGLA, PAIS.NOME AS PAIS_NOME, ESTADO.ATIVO FROM ESTADO, PAIS WHERE PAIS.ID = ESTADO.IDPAIS";
    public static final String SQLPESQUISAR2 = "SELECT ESTADO.ID, ESTADO.NOME AS ESTADO_NOME, ESTADO.SIGLA, PAIS.NOME AS PAIS_NOME, ESTADO.ATIVO FROM ESTADO, PAIS WHERE PAIS.ID = ESTADO.IDPAIS AND ESTADO.ATIVO = 'Ativo'";
    public static final String SQLCOMBOBOX = "SELECT ID, NOME || ' - ' || SIGLA AS NOME FROM ESTADO WHERE ATIVO = 'Ativo' ORDER BY NOME";
    public final String SQLCONSULTAR = "SELECT * FROM ESTADO WHERE ID = ?";
    private final String SQLINCLUIR = "INSERT INTO ESTADO VALUES (?, ?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE ESTADO SET NOME = ?, SIGLA = ?, ATIVO = ?, IDPAIS = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM ESTADO WHERE ID = ?";
    private Estado estado;

    public DaoEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            estado.setId(pegaGenerator("GEN_ESTADO_ID"));
            ps.setInt(1, estado.getId());
            ps.setString(2, estado.getNome());
            ps.setString(3, estado.getSigla());
            ps.setString(4, (estado.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(5, estado.getPais().getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir o estado.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, estado.getNome());
            ps.setString(2, estado.getSigla());
            ps.setString(3, (estado.isAtivo() ? "Ativo" : "Inativo"));
            ps.setInt(4, estado.getPais().getId());
            ps.setInt(5, estado.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar o estado.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, estado.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir o estado.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, estado.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                estado.setNome(rs.getString(2));
                estado.setSigla(rs.getString(3));
                estado.setAtivo(rs.getString(4).equals("Ativo"));
                estado.getPais().setId(rs.getInt(5));
            } else {
                JOptionPane.showMessageDialog(null, "Estado não encontrado.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o estado.");
            return false;
        }
    }
}
