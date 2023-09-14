package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Usuario;

public class DaoUsuario extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM USUARIO";
    public static final String SQLPESQUISAR = "SELECT USUARIO.ID, USUARIO.USUARIO, USUARIO.STATUS\n"
            + "FROM USUARIO\n"
            + "LEFT OUTER JOIN pessoa USING (ID)";
    public final String SQLCONSULTAR = "SELECT * FROM USUARIO WHERE ID = ?";
    private final String SQLINCLUIR = "INSERT INTO USUARIO VALUES (?, ?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE USUARIO SET USUARIO = ?, SENHA = ?, STATUS = ?, IDPESSOA = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM USUARIO WHERE ID = ?";
    private final String SQLLOGIN = "SELECT ID FROM USUARIO WHERE USUARIO = ? AND SENHA = ?";
    private final String SQLBLOQUEIO = "SELECT ID FROM USUARIO WHERE USUARIO = ? AND SENHA = ? AND STATUS = 'Inativo'";
    private Usuario usuario;

    public DaoUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean checarLogin(String login, String senha) {
        boolean check = false;
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLLOGIN);
            ps.setString(1, login);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                check = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o usuário.");
            return false;
        }
        return check;
    }

    public boolean verificaBloqueio(String login, String senha) {
        boolean check = false;
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLBLOQUEIO);
            ps.setString(1, login);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                check = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o usuário.");
            return false;
        }
        return check;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            usuario.setId(pegaGenerator("GEN_USUARIO_ID"));
            ps.setInt(1, usuario.getId());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, (usuario.isStatus() ? "Ativo" : "Inativo"));
            if (usuario.getPessoa().getId() == 0) {
                ps.setNull(5, usuario.getPessoa().getId());
            } else {
                ps.setInt(5, usuario.getPessoa().getId());
            }
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir o usuario.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getSenha());
            ps.setString(3, (usuario.isStatus() ? "Ativo" : "Inativo"));
            if (usuario.getPessoa().getId() == 0) {
                ps.setNull(4, usuario.getPessoa().getId());
            } else {
                ps.setInt(4, usuario.getPessoa().getId());
            }
            ps.setInt(5, usuario.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar o usuario.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir o usuario.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, usuario.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario.setUsuario(rs.getString(2));
                usuario.setSenha(rs.getString(3));
                usuario.setStatus(rs.getString(4).equals("Ativo"));
                usuario.getPessoa().setId(rs.getInt(5));
            } else {
                JOptionPane.showMessageDialog(null, "Usuario não encontrado.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar o usuario.");
            return false;
        }
    }
}
