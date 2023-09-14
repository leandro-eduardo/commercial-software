package bd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.*;
import telas.TelaConfiguracao;

public class Conexao {

    //private static String path = new File("UMUARDB.FDB").getAbsolutePath();
    private static Connection conexao;
    static Preferences prefs = Preferences.userNodeForPackage(TelaConfiguracao.class);
    static TelaConfiguracao telaConfiguracao = new TelaConfiguracao();

    public static Connection getConexao() {
        try {
            if (conexao == null) {
                Class.forName("org.firebirdsql.jdbc.FBDriver");

                //conexão utilizada com configuração da tela de login, pegando a preferência do usuário
                conexao = DriverManager.getConnection("jdbc:firebirdsql://localhost/"
                        + prefs.get(telaConfiguracao.DATABASE_PATH, "value") + "?charSet=utf-8;defaultResultSetHoldable=True", "SYSDBA", "masterkey");
//                      conexao = DriverManager.getConnection("jdbc:firebirdsql://localhost/"
//                              + System.getProperty("user.dir") + "/UMUARDB.FDB?charSet=utf-8;defaultResultSetHoldable=True", "SYSDBA", "masterkey");

                //conexao = DriverManager.getConnection("jdbc:firebirdsql://localhost/C:\\Users\\leand\\Desktop/SISFUN.FDB", "SYSDBA", "masterkey");
            }
            return conexao;
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "Erro no driver JDBC. Verifique se o serviço do Firebird 2.5 está instalado e em execução.\n\n" + cnfe);
            return null;
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(null, "Erro na conexão com o banco de dados. Verifique se o caminho do banco de dados está correto.\n\n" + se);
            se.printStackTrace();
            return null;
        }
    }

    public static List<Object[]> consultarComboBox(String sql) {
        try {
            List<Object[]> retorno = new ArrayList();
            PreparedStatement ps = Conexao.getConexao().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] linha = new Object[2];
                linha[0] = rs.getInt(1);
                linha[1] = rs.getString(2);
                retorno.add(linha);
            }
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar dados");
            return null;
        }
    }

    public static List<String[]> executaQuery(String sql) {
        try {
            List<String[]> dados = new ArrayList();
            Statement st = Conexao.getConexao().createStatement();
            ResultSet rs = st.executeQuery(sql);
            int numeroColunas = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                String[] linha = new String[numeroColunas];
                for (int i = 1; i <= numeroColunas; i++) {
                    linha[i - 1] = rs.getString(i);
                }
                dados.add(linha);
            }
            return dados;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível consultar o banco de dados.");
            return new ArrayList();
        }
    }

    public static List<Object[]> pesquisar(String sql) {
        try {
            List<Object[]> retorno = new ArrayList();
            Statement st = getConexao().createStatement();
            ResultSet rs = st.executeQuery(sql);
            int colunas = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] linha = new Object[colunas];
                for (int i = 1; i <= colunas; i++) {
                    linha[i - 1] = rs.getString(i);
                }
                retorno.add(linha);
            }
            return retorno;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível executar a pesquisa");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean executaUpdate(PreparedStatement ps) {
        try {
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            if (e.getMessage().contains("DUPPAIS")) {
                JOptionPane.showMessageDialog(null, "País já cadastrado.");
            } else if (e.getMessage().contains("DUPCIDADE")) {
                JOptionPane.showMessageDialog(null, "Cidade já cadastrada.");
            } else if (e.getMessage().contains("DUPPESSOA")) {
                JOptionPane.showMessageDialog(null, "Pessoa já cadastrada.");
            } else if (e.getMessage().contains("DUPESTADO")) {
                JOptionPane.showMessageDialog(null, "Estado já cadastrado.");
            } else if (e.getMessage().contains("DUPCOR")) {
                JOptionPane.showMessageDialog(null, "Cor já cadastrada.");
            } else if (e.getMessage().contains("DUPCONDICAOPAGAMENTO")) {
                JOptionPane.showMessageDialog(null, "Condição de pagamento já cadastrada.");
            } else if (e.getMessage().contains("DUPFABRICANTE")) {
                JOptionPane.showMessageDialog(null, "Fabricante já cadastrada.");
            } else if (e.getMessage().contains("DUPMODELO")) {
                JOptionPane.showMessageDialog(null, "Modelo já cadastrado.");
            } else if (e.getMessage().contains("DUPVEICULO")) {
                JOptionPane.showMessageDialog(null, "Veículo já cadastrado.");
            } else if (e.getMessage().contains("DUPCATEGORIA")) {
                JOptionPane.showMessageDialog(null, "Categoria já cadastrada.");
            } else if (e.getMessage().contains("DUPSERVICO")) {
                JOptionPane.showMessageDialog(null, "Serviço já cadastrado.");
            } else if (e.getMessage().contains("DUPPRODUTO")) {
                JOptionPane.showMessageDialog(null, "Produto já cadastrado.");
            } else if (e.getMessage().contains("CAIXAFECHADO")) {
                JOptionPane.showMessageDialog(null, "Não há nenhum caixa aberto.");
            }
            return false;
        }
    }

    public static boolean verificaNomePessoa(PreparedStatement ps) {
        try {
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            if (e.getMessage().contains("NOMEPESSOA")) {
                return false;
            }
        }
        return true;
    }
}
