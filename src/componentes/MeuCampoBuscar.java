package componentes;

import bd.Conexao;
import java.awt.*;
import java.awt.event.*;
import static java.awt.event.KeyEvent.VK_ENTER;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import pojo.ParametrosConsulta;
import pojo.Produto;
import pojo.Servico;
import telas.TelaCompra;
import static telas.TelaCompra.*;
import telas.TelaConsultaFiltro;
import telas.TelaConsultaPagamento;
import telas.TelaConsultaRecebimento;
import telas.TelaContaPagar;
import telas.TelaContaReceber;
import telas.TelaPagamento;
import telas.TelaRecebimento;
import telas.TelaVenda;
import static telas.TelaVenda.campoValorUnitarioItemVenda;
import static telas.TelaVenda.tabelaItemVenda;

public class MeuCampoBuscar extends JPanel implements MeuComponente {

    public boolean achou = false;
    private MeuCampoInteger codigo = new MeuCampoInteger(6, false, true, false, null);
    private JLabel texto = new JLabel();
    private Icon icon = new ImageIcon(getClass().getResource("/icones/search.png"));
    private JButton jb = new JButton(icon);
    private Icon iconGetTela = new ImageIcon(getClass().getResource("/icones/clipboard-with-pencil.png"));
    private JButton jbGetTela = new JButton(iconGetTela);
    private boolean podeHabilitar;
    private List<Integer> pks;
    private String sql;
    private String sqlInativos;
    private boolean obrigatorio;
    private String nome;
    private ParametrosConsulta parametrosConsulta;

    public MeuCampoBuscar(final Class tela, final Class tela2, String sql, String sqlInativos, ParametrosConsulta parametrosConsulta, boolean obrigatorio, boolean podeHabilitar, String nome, Integer tamanhoTexto) {
        this.obrigatorio = obrigatorio;
        this.sql = sql;
        this.sqlInativos = sqlInativos;
        this.parametrosConsulta = parametrosConsulta;
        this.nome = nome;
        this.podeHabilitar = podeHabilitar;
        parametrosConsulta.setChamador(this);
        setLayout(new FlowLayout());
        add(codigo);
        add(texto);
        //texto.setColumns(tamanhoTexto);
        //texto.setEditable(false);
        //texto.setFocusable(false);
        add(jb);
        jb.setPreferredSize(new Dimension(30, 24));
        jb.setToolTipText("Clique para iniciar uma pesquisa de " + getDica() + ".");
        add(jbGetTela);
        jbGetTela.setPreferredSize(new Dimension(30, 24));
        jbGetTela.setToolTipText("Clique para iniciar um novo cadastro de " + getDica() + ".");
        achou = false;

        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (tela == TelaConsultaPagamento.class) { //if que verifica se o botão que clicou possui o nome Conta, que é o nome do campo que está na tela de pagamento e recebimento.
                    System.out.println("ENTREEEEEI no 1");
                    new TelaConsultaPagamento(parametrosConsulta);
                } else if (tela == TelaConsultaRecebimento.class) {
                    System.out.println("ENTREEEEEI no 2");
                    new TelaConsultaRecebimento(parametrosConsulta);
                } else {
                    new TelaConsultaFiltro(parametrosConsulta);
                }
            }
        });

        jbGetTela.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    Method metodoGetTela = tela2.getMethod("getTela");
                    metodoGetTela.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        codigo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    if (codigo.getValor().equals(0)) {
                        achou = false;
                        JOptionPane.showMessageDialog(null, "Nenhum resultado encontrado para o código " + codigo.getValor() + ". Verifique os cadastros clicando no ícone de pesquisa.");
                        codigo.setValor("");
                        texto.setText("");
                        codigo.requestFocus();
                    } else if (verificaDuplicidadeContasGeradas() == true) {
                        codigo.requestFocus();
                        codigo.setText("");
                    } else if (verificaStatus() == true) {
                        codigo.requestFocus();
                        codigo.setText("");
                    } else if (verificaDuplicidadeUsuarios() == true) {
                        codigo.requestFocus();
                        codigo.setText("");
                    } else if (verificaDuplicidadeContasGeradas() == false) {
                        preencher();
                    }
                }
            }
        });

        codigo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (codigo.eVazio()) {
                            getTexto().setText("");
                            TelaContaPagar.campoDesconto.limpar();
                            TelaContaReceber.campoDesconto.limpar();
                        }
                    }
                });
            }
        });

    }

//   @Override
//   public void addFocusListener(FocusListener fl) { //Utilizava antes de passar o KeyListener (new KeyAdapter) - (ANTIGO)
//       codigo.addFocusListener(fl);
//   }
    @Override
    public void addKeyListener(KeyListener kl) {
        codigo.addKeyListener(kl);
    }

    public void preencher() {
        try {
            System.out.println("TO NO PREENCHER NORMAL");
            String tempSql = "SELECT * from (" + sql + ") WHERE ID = " + codigo.getValor();
            System.out.println("TEMPSQL NO PREENCHER: " + tempSql);
            ResultSet rs = Conexao.getConexao().createStatement().executeQuery(tempSql);
            if (rs.next()) {
                codigo.setText(rs.getString(1));
                texto.setText(rs.getString(2));
                if (getDica() == "Compra" || getDica() == "Venda" || getDica() == "Caixa") {
                    //Pegar data do banco e formatar
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatoBr = new SimpleDateFormat("dd/MM/yyyy");
                    Date data = formato.parse(texto.getText());
                    String dataFormatada = formatoBr.format(data);
                    texto.setText(dataFormatada);
                    if (getDica() == "Caixa") {
                        texto.setText((dataFormatada) + " - " + rs.getString(3));
                    }
                }
                achou = true;
                if (getDica().equals("Produto") /* parametrosConsulta.getTelaChamadora() == TelaCompra.class*/) {
                    tabelaItemCompra.getTableModel().alteraProduto(TelaCompra.tela.campoProdutoItemCompra.getValor(), tabelaItemCompra.getLinhaSelecionada());
                    Produto produto = tabelaItemCompra.getTableModel().getProduto(tabelaItemCompra.getLinhaSelecionada());
                    System.out.println("VALOR LLLL " + tabelaItemCompra.getTableModel().getValueAt(tabelaItemCompra.getLinhaSelecionada(), 2));
                    if (tabelaItemCompra.getTableModel().getValueAt(tabelaItemCompra.getLinhaSelecionada(), 2) == produto.getPrecocompra()) {
                        campoValorUnitarioItemCompra.setValor(produto.getPrecocompra());
                    } else {
                        campoValorUnitarioItemCompra.setValor(tabelaItemCompra.getTableModel().getValueAt(tabelaItemCompra.getLinhaSelecionada(), 2));
                    }

                    TelaCompra.atualizaCamposTotal(null);
                }
                if (getDica().equals("Produtos") /*|| parametrosConsulta.getTelaChamadora() == TelaVenda.class*/) {
                    tabelaItemVenda.getTableModel().alteraProduto(TelaVenda.tela.campoProdutoItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                    tabelaItemVenda.getTableModel().alteraTipo(TelaVenda.tela.itemVenda.getTipo(), tabelaItemVenda.getLinhaSelecionada());
                    Produto produto = tabelaItemVenda.getTableModel().getProduto(tabelaItemVenda.getLinhaSelecionada());
                    if (tabelaItemVenda.getTableModel().getValueAt(tabelaItemVenda.getLinhaSelecionada(), 2) == produto.getPrecovenda()) {
                        campoValorUnitarioItemVenda.setValor(produto.getPrecovenda());
                    } else {
                        campoValorUnitarioItemVenda.setValor(tabelaItemVenda.getTableModel().getValueAt(tabelaItemVenda.getLinhaSelecionada(), 2));
                    }

                    TelaVenda.atualizaCamposTotal(null);
                }
                if (getDica().equals("Serviços") /*|| parametrosConsulta.getTelaChamadora() == TelaVenda.class*/) {
                    tabelaItemVenda.getTableModel().alteraServico(TelaVenda.tela.campoServicoItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                    tabelaItemVenda.getTableModel().alteraTipo(TelaVenda.tela.itemVenda.getTipo(), tabelaItemVenda.getLinhaSelecionada());
                    Servico servico = tabelaItemVenda.getTableModel().getServico(tabelaItemVenda.getLinhaSelecionada());
                    if (tabelaItemVenda.getTableModel().getValueAt(tabelaItemVenda.getLinhaSelecionada(), 2) == servico.getValor()) {
                        campoValorUnitarioItemVenda.setValor(servico.getValor());
                    } else {
                        campoValorUnitarioItemVenda.setValor(tabelaItemVenda.getTableModel().getValueAt(tabelaItemVenda.getLinhaSelecionada(), 2));
                    }

                    TelaVenda.atualizaCamposTotal(null);
                }
            } else if (codigo.getValor().equals(0) /*&& achou == true*/) {

            } else {
                achou = false;
                if (verificaInativos() == true) {
                    codigo.requestFocus();
                    codigo.setText("");
                } else {
                    System.out.println("VALOR CLICOU CONSULTAR " + TelaConsultaFiltro.clicouConsultar);
                    if (TelaConsultaFiltro.clicouConsultar == true) {
                        System.out.println("HERE COMES TROUBLE " + parametrosConsulta.getChamador());
                        System.out.println("GEYTDAYDSYATDYTSAFDTASDSADYSAFDTSYAFDA");
                    } else {
                        JOptionPane.showMessageDialog(null, "Nenhum resultado encontrado para o código " + codigo.getValor() + ". Verifique os cadastros clicando no ícone de pesquisa.");
                        codigo.setValor("");
                        texto.setText("");
                        codigo.requestFocus();
                    }
                    TelaConsultaFiltro.clicouConsultar = false;
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher os campos.");
            e.printStackTrace();
            return;
        }
    }

    public void preencherDuploClique() {
        try {
            System.out.println("TO NO PREENCHER DUPLO CLIQUE");
            String tempSql = "SELECT * from (" + sql + ") WHERE ID = " + codigo.getValor();
            System.out.println("TEMPSQL NO PREENCHER: " + tempSql);
            ResultSet rs = Conexao.getConexao().createStatement().executeQuery(tempSql);
            if (rs.next()) {
                codigo.setText(rs.getString(1));
                texto.setText(rs.getString(2));
                achou = true;
            } else {
                achou = false;
                JOptionPane.showMessageDialog(null, "Nenhum resultado encontrado para o código " + codigo.getValor() + ". Verifique os cadastros clicando no ícone de pesquisa.");
                codigo.setValor("");
                texto.setText("");
                codigo.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher os campos.");
            e.printStackTrace();
            return;
        }
    }

    @Override
    public boolean eObrigatorio() {
        return obrigatorio;
    }

    @Override
    public boolean eValido() {
        return true;
    }

    @Override
    public boolean eVazio() {
        return codigo.getText().trim().equals("") || texto.getText().trim().equals("");
    }

    @Override
    public void limpar() {
        codigo.setText("");
        texto.setText("");
    }

    @Override
    public void habilitar(boolean status) {
        codigo.setEnabled(podeHabilitar && status);
        texto.setEnabled(podeHabilitar && status);
        jb.setEnabled(podeHabilitar && status);
        jbGetTela.setEnabled(podeHabilitar && status);
    }

    public void editar(boolean status) {
        codigo.setEditable(podeHabilitar && status);
        //texto.setEditable(podeHabilitar && status);
        jb.setEnabled(podeHabilitar && status);
        jbGetTela.setEnabled(podeHabilitar && status);
    }

    public Object getValor() {
        return codigo.getValor();
    }

    public void setValor(Object valor) {
        codigo.setValor(valor);
        preencher();
        System.out.println("ENTREI SET VALOR NORMAL");
        System.out.println("GET CHAMADOR: " + parametrosConsulta.getChamador().getClass());
        System.out.println("GET TELACHAMADORA: " + parametrosConsulta.getTelaChamadora());
        if (parametrosConsulta.getChamador().getClass() == MeuCampoBuscar.class && parametrosConsulta.getTelaChamadora() == TelaContaPagar.class) {
            codigo.setValor(codigo.getValor());
        } else if (parametrosConsulta.getChamador().getClass() == MeuCampoBuscar.class && parametrosConsulta.getTelaChamadora() == TelaContaReceber.class) {
            codigo.setValor(codigo.getValor());
        }
    }

    public void setValorDuploClique(Object valor) {
        codigo.setValor(valor);
        preencher();
        if (parametrosConsulta.getChamador().getClass() == MeuCampoBuscar.class && parametrosConsulta.getTelaChamadora() == TelaContaPagar.class && getDica().equals("Compra")) {
            TelaContaPagar.tela.preencherDados();
            TelaContaPagar.campoDescricao.setValor("DOC. DE COMPRA REFERÊNCIA " + codigo.getValor());
        } else if (parametrosConsulta.getChamador().getClass() == MeuCampoBuscar.class && parametrosConsulta.getTelaChamadora() == TelaContaReceber.class && getDica().equals("Venda")) {
            TelaContaReceber.tela.preencherDados();
            TelaContaReceber.campoDescricao.setValor("DOC. DE VENDA REFERÊNCIA " + codigo.getValor());
        } else if (parametrosConsulta.getChamador().getClass() == MeuCampoBuscar.class && parametrosConsulta.getTituloConsulta() == "Contas a Pagar - Efetuar Pagamento") {
            TelaPagamento.tela.preencherDados();
        } else if (parametrosConsulta.getChamador().getClass() == MeuCampoBuscar.class && parametrosConsulta.getTituloConsulta() == "Contas a Receber - Efetuar Recebimento") {
            TelaRecebimento.tela.preencherDados();
        }
    }

    @Override
    public String getDica() {
        return nome;
    }

    public boolean verificaDuplicidadeContasGeradas() {
        try {
            if (getDica() == "Compra") {
                String tempSqlConsultar = "SELECT CONTAPAGAR.ID, CONTAPAGAR.IDCOMPRA, CONTAPAGAR.DATA FROM CONTAPAGAR"
                        + " LEFT JOIN COMPRA ON CONTAPAGAR.IDCOMPRA = COMPRA.ID WHERE CONTAPAGAR.IDCOMPRA = " + codigo.getValor();
                int varAuxiliarCodCompra = (int) codigo.getValor();
                System.out.println("TEMPSQL NO VERIFICADUPLICIDADECPGERADAS: " + tempSqlConsultar);
                ResultSet rs = Conexao.getConexao().createStatement().executeQuery(tempSqlConsultar);
                if (rs.next()) {
                    codigo.setText(rs.getString(1));
                    int varAuxiliarCodContaPagar = (int) codigo.getValor();
                    codigo.setText("");
                    JOptionPane.showMessageDialog(null, "Já foi gerada uma conta a pagar para a compra com o código " + varAuxiliarCodCompra
                            + "\nna conta a pagar de referência " + varAuxiliarCodContaPagar + ". Verifique as contas que podem"
                            + "\nser geradas clicando no ícone de pesquisa.");
                    return true;
                } else {
                    return false;
                }
            }
            if (getDica() == "Venda") {
                String tempSqlConsultar = "SELECT CONTARECEBER.ID, CONTARECEBER.IDVENDA, CONTARECEBER.DATA FROM CONTARECEBER"
                        + " LEFT JOIN VENDA ON CONTARECEBER.IDVENDA = VENDA.ID WHERE CONTARECEBER.IDVENDA = " + codigo.getValor();
                int varAuxiliarCodVenda = (int) codigo.getValor();
                System.out.println("TEMPSQL NO VERIFICADUPLICIDADECRGERADAS: " + tempSqlConsultar);
                ResultSet rs = Conexao.getConexao().createStatement().executeQuery(tempSqlConsultar);
                if (rs.next()) {
                    codigo.setText(rs.getString(1));
                    int varAuxiliarCodContaReceber = (int) codigo.getValor();
                    codigo.setText("");
                    JOptionPane.showMessageDialog(null, "Já foi gerada uma conta a receber para a venda com o código " + varAuxiliarCodVenda
                            + "\nna conta a receber de referência " + varAuxiliarCodContaReceber + ". Verifique as contas que podem"
                            + "\nser geradas clicando no ícone de pesquisa.");
                    return true;
                } else {
                    return false;
                }
            } else if (getDica() == "Conta") {
                String tempSqlConsultar = "SELECT PAGAMENTO.ID, PARCELACONTAPAGAR.ID, PAGAMENTO.VALOR FROM PAGAMENTO\n"
                        + "LEFT JOIN PARCELACONTAPAGAR ON PAGAMENTO.IDPARCELACONTAPAGAR = PARCELACONTAPAGAR.ID\n"
                        + "WHERE PARCELACONTAPAGAR.ID = " + codigo.getValor() + " AND PARCELACONTAPAGAR.VALORPENDENTE = 0";
                int varAuxiliarCodParcelaContaPagar = (int) codigo.getValor();
                System.out.println("TEMPSQL NO VERIFICADUPLICIDADECRGERADAS: " + tempSqlConsultar);
                ResultSet rs = Conexao.getConexao().createStatement().executeQuery(tempSqlConsultar);
                if (rs.next()) {
                    codigo.setText(rs.getString(1));
                    int varAuxiliarCodPagamento = (int) codigo.getValor();
                    codigo.setText("");
                    String varAuxiliarValorPagamento = (rs.getString(3));
                    BigDecimal formato = new BigDecimal((String) varAuxiliarValorPagamento);
                    JOptionPane.showMessageDialog(null, "Pagamento já realizado para a conta " + varAuxiliarCodParcelaContaPagar + " no valor " + NumberFormat.getCurrencyInstance().format(formato) + "."
                            + " O pagamento gerado possui o identificador " + varAuxiliarCodPagamento + ". " + "Verifique as contas que devem ser"
                            + "\npagas clicando no ícone de pesquisa ou consulte os pagamentos já efetuados através do botão consultar.");
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher os campos.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean verificaInativos() {
        try {
            if (getDica().equals("Fornecedor")) {
                String tempSqlConsultarFornecedor = sqlInativos + " WHERE ID = " + codigo.getValor() + " AND EFORNECEDOR = 'S' AND ATIVO = 'Inativo'";
                System.out.println("TEMPSQLCONSULTAR NO PREENCHER FORNCEDOR: " + tempSqlConsultarFornecedor);
                ResultSet rs = Conexao.getConexao().createStatement().executeQuery(tempSqlConsultarFornecedor);
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "O cadastro de " + getDica() + " com o código " + codigo.getValor() + " está inativo."
                            + "\n Verifique os cadastros.");
                    return true;
                } else {
                    return false;
                }
            } else if (getDica().equals("Compra")) {
                return false;
            } else if (getDica().equals("Venda")) {
                return false;
            } else if (getDica().equals("Conta")) {
                return false;
            } else if (getDica().equals("Caixa")) {
                return false;
            } else {
                String tempSqlConsultar = sqlInativos + " WHERE ID = " + codigo.getValor() + " AND ATIVO = 'Inativo'";
                System.out.println("TEMPSQLCONSULTAR NO PREENCHER: " + tempSqlConsultar);
                ResultSet rs = Conexao.getConexao().createStatement().executeQuery(tempSqlConsultar);
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "O cadastro de " + getDica() + " com o código " + codigo.getValor() + " está inativo."
                            + "\n Verifique os cadastros.");
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher os campos.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean verificaStatus() {
        try {
            if (getDica() == "Compra") {
                String tempSqlConsultar = "SELECT STATUS FROM COMPRA WHERE ID = " + codigo.getValor() + "AND STATUS <> 'CF'";
                int varAuxiliarCodCompra = (int) codigo.getValor();
                ResultSet rs = Conexao.getConexao().createStatement().executeQuery(tempSqlConsultar);
                if (rs.next()) {
                    texto.setText(rs.getString(1));
                    String varAuxiliarCompraStatus = (String) texto.getText();
                    texto.setText("");
                    JOptionPane.showMessageDialog(null, "A compra com o código " + varAuxiliarCodCompra + " está com o status " + varAuxiliarCompraStatus + ". "
                            + "\nPara gerar uma conta a pagar o status deve ser CF (Compra Finalizada)."
                            + "\nVerifique as contas que podem ser geradas clicando no ícone de pesquisa.");
                    return true;
                } else {
                    return false;
                }
            }
            if (getDica() == "Venda") {
                String tempSqlConsultar = "SELECT STATUS FROM VENDA WHERE ID = " + codigo.getValor() + "AND STATUS <> 'VF'";
                int varAuxiliarCodVenda = (int) codigo.getValor();
                ResultSet rs = Conexao.getConexao().createStatement().executeQuery(tempSqlConsultar);
                if (rs.next()) {
                    texto.setText(rs.getString(1));
                    String varAuxiliarVendaStatus = (String) texto.getText();
                    texto.setText("");
                    JOptionPane.showMessageDialog(null, "A venda com o código " + varAuxiliarCodVenda + " está com o status " + varAuxiliarVendaStatus + ". "
                            + "\nPara gerar uma conta a receber o status deve ser VF (Venda Finalizada)."
                            + "\nVerifique as contas que podem ser geradas clicando no ícone de pesquisa.");
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher os campos.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean verificaDuplicidadeUsuarios() {
        try {
            if (getDica() == "Pessoa") {
                String tempSqlConsultar = "SELECT USUARIO FROM USUARIO\n"
                        + "INNER JOIN PESSOA ON PESSOA.ID = USUARIO.IDPESSOA\n"
                        + "WHERE PESSOA.ID = " + codigo.getValor();
                System.out.println("PRINT TEMPSQLCONSULTAR PESSOAUSER = " + tempSqlConsultar);
                int varAuxiliarCodPessoa = (int) codigo.getValor();
                ResultSet rs = Conexao.getConexao().createStatement().executeQuery(tempSqlConsultar);
                if (rs.next()) {
                    texto.setText(rs.getString(1));
                    String varAxuliarUsuario = texto.getText();
                    texto.setText("");
                    JOptionPane.showMessageDialog(null, "Já existe um usuário para o funcionário " + varAuxiliarCodPessoa + "."
                            + "\nUsuário já criado: " + varAxuliarUsuario);
                    texto.setText("");
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher os campos.");
            e.printStackTrace();
            return false;
        }
    }

    public JLabel getTexto() {
        return texto;
    }

}
