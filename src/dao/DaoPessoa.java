package dao;

import bd.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import pojo.Pessoa;
import util.MetodosGenericos;

public class DaoPessoa extends DaoPadrao {

    public static final String SQLINATIVOS = "SELECT * FROM PESSOA";
    public static final String SQLPESQUISAR = "SELECT PESSOA.ID, NOMERAZAOSOCIAL, CPFCNPJ, CIDADE.NOME || ' - ' || ESTADO.SIGLA AS CIDADE, PESSOA.ATIVO, ECLIENTE, EFORNECEDOR, EFUNCIONARIO FROM PESSOA, CIDADE, ESTADO WHERE CIDADE.ID = PESSOA.IDCIDADE AND CIDADE.IDESTADO = ESTADO.ID";
    public static final String SQLPESQUISARFORNECEDORES = "SELECT PESSOA.ID, NOMERAZAOSOCIAL, CPFCNPJ, CIDADE.NOME || ' - ' || ESTADO.SIGLA AS CIDADE, PESSOA.ATIVO FROM PESSOA, CIDADE, ESTADO WHERE CIDADE.ID = PESSOA.IDCIDADE AND CIDADE.IDESTADO = ESTADO.ID AND EFORNECEDOR = 'S'";
    public static final String SQLPESQUISARCLIENTES = "SELECT PESSOA.ID, NOMERAZAOSOCIAL, CPFCNPJ, CIDADE.NOME || ' - ' || ESTADO.SIGLA AS CIDADE, PESSOA.ATIVO FROM PESSOA, CIDADE, ESTADO WHERE CIDADE.ID = PESSOA.IDCIDADE AND CIDADE.IDESTADO = ESTADO.ID AND ECLIENTE = 'S'";
    public static final String SQLPESQUISARFUNCIONARIOS = "SELECT PESSOA.ID, NOMERAZAOSOCIAL, CPFCNPJ, CIDADE.NOME || ' - ' || ESTADO.SIGLA AS CIDADE, PESSOA.ATIVO FROM PESSOA, CIDADE, ESTADO WHERE CIDADE.ID = PESSOA.IDCIDADE AND CIDADE.IDESTADO = ESTADO.ID AND EFUNCIONARIO = 'S'";
    public static final String SQLFORNECEDORES = "SELECT ID, APELIDONOMEFANTASIA FROM PESSOA WHERE ATIVO = 'Ativo' AND EFORNECEDOR = 'S' ORDER BY NOMERAZAOSOCIAL";
    public static final String SQLCLIENTES = "SELECT ID, NOMERAZAOSOCIAL FROM PESSOA WHERE ATIVO = 'Ativo' AND ECLIENTE = 'S' ORDER BY NOMERAZAOSOCIAL";
    public static final String SQLFUNCIONARIOS = "SELECT ID, NOMERAZAOSOCIAL FROM PESSOA WHERE ATIVO = 'Ativo' AND EFUNCIONARIO = 'S' ORDER BY NOMERAZAOSOCIAL";
    public static final String SQLFUNCIONARIOSSEMCADASTRO = "SELECT PESSOA.ID, NOMERAZAOSOCIAL, CPFCNPJ, ATIVO FROM PESSOA WHERE ATIVO = 'Ativo' AND EFUNCIONARIO = 'S'\n" +
                                                            "AND NOT EXISTS (SELECT * FROM USUARIO WHERE PESSOA.ID = USUARIO.IDPESSOA)";
    public final String SQLCONSULTAR = "SELECT * FROM PESSOA WHERE ID = ?";
    private final String SQLINCLUIR = "INSERT INTO PESSOA VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQLALTERAR = "UPDATE PESSOA SET NOMERAZAOSOCIAL = ?, APELIDONOMEFANTASIA = ?, TIPO = ?, CPFCNPJ = ?, DATANASCIMENTO = ?, RGIE = ?, SEXO = ?, ENDERECO = ?, NUMERO = ?, COMPLEMENTO = ?, BAIRRO = ?, CEP = ?, IDCIDADE = ?, FONE1 = ?, FONE2 = ?, EMAIL = ?, SITE = ?, ECLIENTE = ?, EFORNECEDOR = ?, EFUNCIONARIO = ?, ATIVO = ?, OBSERVACAO = ? WHERE ID = ?";
    private final String SQLEXCLUIR = "DELETE FROM PESSOA WHERE ID = ?";
    private Pessoa pessoa;

    public DaoPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public boolean incluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLINCLUIR);
            pessoa.setId(pegaGenerator("GEN_PESSOA_ID"));
            ps.setInt(1, pessoa.getId());
            ps.setString(2, pessoa.getNomerazaosocial());
            ps.setString(3, pessoa.getApelidonomefantasia());
            ps.setString(4, pessoa.getTipo());
            ps.setString(5, pessoa.getCpfcnpj());
            if (pessoa.getDatanascimento() == null) {
                ps.setNull(6, java.sql.Types.DATE);
            } else {
                ps.setDate(6, MetodosGenericos.dataParaBanco(pessoa.getDatanascimento()));
            }
             if (pessoa.getRgie().isEmpty()) {
                ps.setNull(7, java.sql.Types.VARCHAR);
            } else {
                ps.setString(7, pessoa.getRgie());
            }
            ps.setString(8, pessoa.getSexo());
            ps.setString(9, pessoa.getEndereco());
            ps.setString(10, pessoa.getNumero());
            ps.setString(11, pessoa.getComplemento());
            ps.setString(12, pessoa.getBairro());
            ps.setString(13, pessoa.getCep());
            ps.setString(14, pessoa.getFone1());
            ps.setString(15, pessoa.getFone2());
            ps.setString(16, pessoa.getEmail());
            ps.setString(17, pessoa.getSite());
            ps.setString(18, (pessoa.isEcliente() ? "S" : "N"));
            ps.setString(19, (pessoa.isEfornecedor() ? "S" : "N"));
            ps.setString(20, (pessoa.isEfuncionario() ? "S" : "N"));
            ps.setString(21, (pessoa.isAtivo() ? "Ativo" : "Inativo"));
            ps.setString(22, (pessoa.getObservacao()));
            ps.setInt(23, pessoa.getCidade().getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível incluir a pessoa.");
            return false;
        }
    }

    public boolean alterar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLALTERAR);
            ps.setString(1, pessoa.getNomerazaosocial());
            ps.setString(2, pessoa.getApelidonomefantasia());
            ps.setString(3, pessoa.getTipo());
            ps.setString(4, pessoa.getCpfcnpj());
            if (pessoa.getDatanascimento() == null) {
                ps.setNull(5, java.sql.Types.DATE);
            } else {
                ps.setDate(5, MetodosGenericos.dataParaBanco(pessoa.getDatanascimento()));
            }
            if (pessoa.getRgie().isEmpty()) {
                ps.setNull(6, java.sql.Types.VARCHAR);
            } else {
                ps.setString(6, pessoa.getRgie());
            }
            ps.setString(7, pessoa.getSexo());
            ps.setString(8, pessoa.getEndereco());
            ps.setString(9, pessoa.getNumero());
            ps.setString(10, pessoa.getComplemento());
            ps.setString(11, pessoa.getBairro());
            ps.setString(12, pessoa.getCep());
            ps.setInt(13, pessoa.getCidade().getId());
            ps.setString(14, pessoa.getFone1());
            ps.setString(15, pessoa.getFone2());
            ps.setString(16, pessoa.getEmail());
            ps.setString(17, pessoa.getSite());
            ps.setString(18, (pessoa.isEcliente() ? "S" : "N"));
            ps.setString(19, (pessoa.isEfornecedor() ? "S" : "N"));
            ps.setString(20, (pessoa.isEfuncionario() ? "S" : "N"));
            ps.setString(21, (pessoa.isAtivo() ? "Ativo" : "Inativo"));
            ps.setString(22, (pessoa.getObservacao()));
            ps.setInt(23, pessoa.getId());
            return Conexao.executaUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível alterar a pessoa.");
            return false;
        }
    }

    public boolean excluir() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLEXCLUIR);
            ps.setInt(1, pessoa.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível excluir a pessoa.");
            return false;
        }
    }

    public boolean consultar() {
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQLCONSULTAR);
            ps.setInt(1, pessoa.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pessoa.setId(rs.getInt(1));
                pessoa.setNomerazaosocial(rs.getString(2));
                pessoa.setApelidonomefantasia(rs.getString(3));
                pessoa.setTipo(rs.getString(4));
                pessoa.setCpfcnpj(rs.getString(5));
                pessoa.setDatanascimento(rs.getDate(6));
                pessoa.setRgie(rs.getString(7));
                pessoa.setSexo(rs.getString(8));
                pessoa.setEndereco(rs.getString(9));
                pessoa.setNumero(rs.getString(10));
                pessoa.setComplemento(rs.getString(11));
                pessoa.setBairro(rs.getString(12));
                pessoa.setCep(rs.getString(13));
                pessoa.setFone1(rs.getString(14));
                pessoa.setFone2(rs.getString(15));
                pessoa.setEmail(rs.getString(16));
                pessoa.setSite(rs.getString(17));
                pessoa.setEcliente(rs.getString(18).equals("S"));
                pessoa.setEfornecedor(rs.getString(19).equals("S"));
                pessoa.setEfuncionario(rs.getString(20).equals("S"));
                pessoa.setAtivo(rs.getString(21).equals("Ativo"));
                pessoa.setObservacao(rs.getString(22));
                pessoa.getCidade().setId(rs.getInt(23));
            } else {
                JOptionPane.showMessageDialog(null, "Pessoa não encontrada.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível consultar a pessoa.");
            return false;
        }
    }
}
