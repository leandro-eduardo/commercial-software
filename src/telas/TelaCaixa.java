package telas;

import bd.Conexao;
import componentes.*;
import dao.DaoCaixa;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import pojo.Caixa;
import pojo.ParametrosConsulta;
import renderizador.CellRendererData;
import renderizador.InteiroRender;
import renderizador.MonetarioRender;
import renderizador.RenderizadorStatus;
import tabela.TabelaMovimentoCaixa;
import tabela.TableModelMovimentoCaixa;

public class TelaCaixa extends TelaCadastro {

    public static TelaCaixa tela;

    private Integer caixaID = 0;

    JLabel labelCodigoStatusCaixa = new JLabel();
    JLabel labelIconStatusCaixa = new JLabel();

    JLabel labelSaldoAtual = new JLabel("Saldo Atual");
    JLabel labelSaldoCaixa = new JLabel();

    JLabel labelTotalEntradas = new JLabel();
    JLabel labelTotalSaidas = new JLabel();

    JPanel painelSaldoCaixa = new JPanel(new GridLayout(4, 1));
    JPanel painelSituacaoCaixa = new JPanel(new FlowLayout());
    public JPanel jpStatusCaixa = new JPanel();

    private Caixa caixa = new Caixa();
    private DaoCaixa daoCaixa = new DaoCaixa(caixa);

    public ParametrosConsulta parametrosConsulta;

    private Icon iconMovimentoExtra = new ImageIcon(getClass().getResource("/icones/transaction.png"));
    public JButton jbMovimentoExtra = new JButton("Mov. Extra", iconMovimentoExtra);
    private Icon iconImprimir = new ImageIcon(getClass().getResource("/icones/printer.png"));
    public JButton jbImprimir = new JButton("Imprimir", iconImprimir);

    public MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuJTextArea campoDescricao = new MeuJTextArea("Descrição", false, 300, 50);

    private JPanel painelStatus = new JPanel();
    public MeuCampoCheckBox campoAberto = new MeuCampoCheckBox(false, false, "Aberto");
    public MeuCampoDataHora campoData = new MeuCampoDataHora(10, false, "Abertura");
    public MeuCampoDataHora campoDataHrFechamento = new MeuCampoDataHora(10, false, "Fechamento");

    public MeuCampoMonetario campoSaldoInicial = new MeuCampoMonetario(15, true, false, true, "Saldo inicial");
    public MeuCampoMonetario campoSaldoFinal = new MeuCampoMonetario(15, true, false, true, "Saldo final");
    public MeuCampoMonetario campoSaldoAntesFechamento = new MeuCampoMonetario(15, false, true, false, "Saldo fechamento");
    public MeuCampoMonetario campoSaldo = new MeuCampoMonetario(15, true, true, false, "Saldo");
    public TabelaMovimentoCaixa tabelaMovimentosCaixa = new TabelaMovimentoCaixa();

    public TelaCaixa() {
        super("Tela de Caixa");
        jpStatusCaixa.setLayout(new GridLayout());
        jpStatusCaixa.setPreferredSize(new Dimension(40, 40));
        getContentPane().add("North", jpStatusCaixa);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/caixa.png")));

        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);

        adicionaComponente(2, 1, 1, 1, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoAberto, painelStatus);
        adicionaComponente(3, 1, 1, 1, campoData, jpComponentes);
        adicionaComponente(4, 1, 1, 1, campoDataHrFechamento, jpComponentes);
        adicionaComponente(5, 1, 1, 1, campoSaldoAntesFechamento, jpComponentes);
        adicionaComponente(6, 1, 1, 1, campoSaldoInicial, jpComponentes);
        adicionaComponente(7, 1, 1, 1, campoSaldoFinal, jpComponentes);
        adicionaComponente(8, 1, 1, 1, tabelaMovimentosCaixa, jpComponentes);
        painelSaldoCaixa.setPreferredSize(new Dimension(250, 170));
        //adicionaComponente(7, 3, 1, 1, campoSaldo, jpComponentes); //comentado pois a labelSaldoCaixa faz o papel de mostrar os valores de uma forma mais adequada ao usuário
        adicionaComponente(8, 3, 1, 1, painelSaldoCaixa, jpComponentes);

        adicionaListeners();
        adicionaScrollPane();
        habilitaComponentes(false);

        campoSaldoFinal.setEnabled(false);
        campoData.setEnabled(false);
        campoDataHrFechamento.setEnabled(false);
        jbIncluir.setText("Abrir Caixa");
        jbAlterar.setText("Fechar Caixa");
        removeBotao(jbExcluir);
        jpBotoes.add(jbMovimentoExtra, 2); //2 é a posição para adicionar o botão
        jpBotoes.add(jbImprimir, 3); //3 é a posição para adicionar o botão
        jbImprimir.setEnabled(false);
        jbImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirma == JOptionPane.YES_OPTION) {

                    //Imprimindo o relatório
                    //Caminho absoluto comentado para utilizar o caminho relativo que pega os arquivo da pasta src do projeto
                    //String caminhoRelatorio = System.getProperty("user.dir") + "/JasperReports/RelatorioFechamentoCaixa.jasper";/*"C:\\Users\\leand\\JaspersoftWorkspace\\MyReports\\RelatorioProdutos.jasper";*/
                    //Caminho relativo
                    InputStream caminhoRelatorio = getClass().getResourceAsStream("/relatorios/RelatorioFechamentoCaixa.jasper");
                    JasperPrint jasperPrint = null;

                    //Lista com os parametros para o relátorio
                    HashMap parametros = new HashMap<>();

                    //Passando parâmetros e convertendo os dados pra ser compativel                                            
                    parametros.put("IDCAIXA", campoCodigo.getValor());

                    //Caminho relativo do sub relatório
                    URL caminhoSubRelatorio = getClass().getResource("/relatorios/subrelatorios/SubRelatorioMovimentosCaixa.jasper");
                    parametros.put("SUBREPORT_DIR", caminhoSubRelatorio);

                    try {
                        jasperPrint = JasperFillManager.fillReport(caminhoRelatorio, parametros, Conexao.getConexao());
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(rootPane, "Erro ao gerar o relatório - " + e);
                    }

                    //Relatório finalmente é exibido na tela para o usuário
                    JasperViewer jv = new JasperViewer(jasperPrint, false);
                    URL iconReportURL = getClass().getResource("/icones/relatorio.png");
                    ImageIcon icon = new ImageIcon(iconReportURL);
                    jv.setIconImage(icon.getImage());
                    jv.setTitle("Movimentos do Caixa");
                    jv.setVisible(true);
                }
            }
        ;
        });

        criarPainelSaldoCaixa();

        if (verificaCaixaAberto()) {
            pegaIDCaixaAberto();
            consultarBD((int) campoCodigo.getValor());
            //getPersistencia();
        }
        setSize(1350, 630);
        //pack();
        setMaximumSize(new Dimension(getWidth(), getHeight()));
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();
    }

    public TelaCaixa(Caixa caixa) {
        this();
        this.caixa = caixa;
        getPersistencia();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCaixa();
            tela.addInternalFrameListener(new InternalFrameAdapter() { //Adiciona um listener para verificar quando a tela for fechada, fazendo assim a remoção da mesma junto ao JDesktopPane da TelaSistema e setando a variável tela = null para permitir que a tela possa ser aberta novamente em outro momento. Basicamente o mesmo controle efetuado pela tela de pesquisa, porém de uma forma um pouco diferente.
                @Override
                public void internalFrameClosed(InternalFrameEvent e) {
                    TelaSistema.jdp.remove(tela);
                    tela = null;
                }
            });
            TelaSistema.jdp.add(tela);
        }
        //Depois do teste acima, independentemente dela já existir ou não, ela é selecionada e movida para frente
        TelaSistema.jdp.setSelectedFrame(tela);
        TelaSistema.jdp.moveToFront(tela);
        return tela;
    }

    public void setPersistencia() {
        caixa.setId((Integer) campoCodigo.getValor());
        caixa.setData(campoData.getValorHora());
        caixa.setDataHrFechamento(campoDataHrFechamento.getValorHora());
        caixa.setSaldoInicial(campoSaldoInicial.getValor());
        caixa.setSaldoFinal(campoSaldoFinal.getValor());
        if (estadoTela == INCLUINDO) {
            BigDecimal zero = BigDecimal.ZERO;
            caixa.setSaldoAntesFechamento(zero);
        } else if (estadoTela == ALTERANDO) {
            caixa.setSaldoAntesFechamento(campoSaldo.getValor());
        }
        caixa.setSaldo(campoSaldo.getValor());
        caixa.setAberto(campoAberto.getValor());
        caixa.setItensCaixa(((TableModelMovimentoCaixa) tabelaMovimentosCaixa.getTabela().getModel()).getDados());
    }

    public void getPersistencia() {
        campoCodigo.setValor(caixa.getId());
        campoData.setValor(caixa.getData());
        if (verificaCaixaFechado()) {
            campoDataHrFechamento.setValor(caixa.getDataHrFechamento());
            campoSaldoAntesFechamento.setValor(caixa.getSaldoAntesFechamento());
        }
        campoSaldoInicial.setValor(caixa.getSaldoInicial());
        campoSaldoFinal.setValor(caixa.getSaldoFinal());
        campoSaldo.setValor(caixa.getSaldo());
        campoAberto.setValor(caixa.isAberto());
        ((TableModelMovimentoCaixa) tabelaMovimentosCaixa.getTabela().getModel()).setDados(caixa.getItensCaixa());
    }

    public void calcular() {
        BigDecimal saldoInicial = campoSaldoInicial.getValor();
        BigDecimal saldo = campoSaldoFinal.getValor().subtract(saldoInicial);
        campoSaldo.setValor(saldo);
    }

    public void adicionaListeners() {
        jbMovimentoExtra.addActionListener(this);

    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        getPersistencia();
        boolean retorno = daoCaixa.incluir();
        consultarBD(caixa.getId());
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoCaixa.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoCaixa.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        caixa.setId(pk);
        daoCaixa.consultar();
        getPersistencia();
        jbImprimir.setEnabled(true);
        tabelaMovimentosCaixa.limiteRegistros.setEnabled(true);
        tabelaMovimentosCaixa.gerarPaginas(caixa.getId());

        criarPainelSaldoCaixa();
        criarLabelIDStatusCaixa();
    }

    @Override
    public void incluir() {
        super.incluir();
        jbImprimir.setEnabled(false);
        campoData.setEnabled(false);
        campoDataHrFechamento.setEnabled(false);
        campoSaldo.limpar();
        BigDecimal saldo = campoSaldo.getValor();
        labelSaldoCaixa.setText(NumberFormat.getCurrencyInstance().format(saldo));
        labelSaldoAtual.setHorizontalAlignment(SwingConstants.CENTER);
        labelTotalEntradas.setText("( + )  Total Entradas:  R$ 0,00");
        labelTotalSaidas.setText("( - )  Total Saídas:      R$ 0,00");
        labelTotalEntradas.setHorizontalAlignment(SwingConstants.CENTER);
        labelTotalSaidas.setHorizontalAlignment(SwingConstants.CENTER);
        campoAberto.setSelected(true);
        campoSaldoFinal.setEnabled(false);
        jpStatusCaixa.remove(painelSituacaoCaixa);
        jpStatusCaixa.repaint();
        labelSaldoCaixa.setForeground(Color.decode("#000000"));
        tabelaMovimentosCaixa.limiteRegistros.setEnabled(false);
        campoSaldoInicial.requestFocus();
    }

    @Override
    public void alterar() {
        super.alterar();
        campoData.setEnabled(false);
        campoDataHrFechamento.setEnabled(false);
        campoSaldoInicial.setEnabled(false);
        campoSaldo.setEnabled(false);
        campoAberto.setSelected(false);
        campoSaldoFinal.requestFocus();
    }

    @Override
    public void excluir() {
        //método não faz nada  
    }

    public void movimentoExtra() {
        if (!caixa.isAberto()) {
            JOptionPane.showMessageDialog(null, "Caixa selecionado está fechado.");
            return;
        }
        TelaMovimentoCaixa.getTela();
    }

    @Override
    public void cancelar() {
        if (estadoTela == ALTERANDO) {
            campoSaldoFinal.setEnabled(false);
            campoAberto.setSelected(true);
            estadoTela = PADRAO;
            habilitaBotoes();
        } else {
            super.cancelar();
            jbImprimir.setEnabled(false);
            jpStatusCaixa.remove(painelSituacaoCaixa);
            jpStatusCaixa.repaint();
            campoSaldo.limpar();
            labelSaldoAtual.setHorizontalAlignment(SwingConstants.CENTER);
            BigDecimal saldo = BigDecimal.ZERO;
            labelSaldoCaixa.setText(NumberFormat.getCurrencyInstance().format(saldo));
            labelSaldoCaixa.setForeground(Color.decode("#000000"));
            labelTotalEntradas.setText("( + )  Total Entradas:  R$ 0,00");
            labelTotalSaidas.setText("( - )  Total Saídas:      R$ 0,00");
            labelTotalEntradas.setHorizontalAlignment(SwingConstants.CENTER);
            labelTotalSaidas.setHorizontalAlignment(SwingConstants.CENTER);

            tabelaMovimentosCaixa.numeroPaginaAtual = 1;
            tabelaMovimentosCaixa.labelPaginaAtual.setText(String.valueOf(tabelaMovimentosCaixa.numeroPaginaAtual));
            tabelaMovimentosCaixa.jbAnteriorPagina.setEnabled(false);
            tabelaMovimentosCaixa.jbProximaPagina.setEnabled(false);
            tabelaMovimentosCaixa.limiteRegistros.setEnabled(false);
            //tabelaMovimentosCaixa.limiteRegistros.setSelectedItem(tabelaMovimentosCaixa.prefs.get(tabelaMovimentosCaixa.USER_PREF_TAMANHO_PAGINA, "value"));        
            setSize(1350, 630);
            //pack();
        }

    }

    @Override
    public void limpar() {
        super.limpar();
    }

    @Override
    public void confirmar() {
        if (estadoTela == ALTERANDO) {
            BigDecimal valorComparacao = campoSaldoFinal.getValor();
            BigDecimal valorDiferenca = campoSaldo.getValor().subtract(campoSaldoFinal.getValor());

            if (valorComparacao.compareTo(BigDecimal.ZERO) == 0 && (valorDiferenca.compareTo(BigDecimal.ZERO) != 0)) {
                int opcao = JOptionPane.showConfirmDialog(null, "Saldo final igual a zero e o caixa será fechado com diferença. Deseja realmente finalizar o caixa?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if ((opcao != JOptionPane.OK_OPTION) || (!alterarBD())) {
                    return;
                }
                InserirMovimentoSaldoFinal();
//                fecharCaixa();
                consultarBD(caixa.getId());
                super.confirmar();
                return;
            }
            if (valorComparacao.compareTo(BigDecimal.ZERO) == 0) {
                int opcao = JOptionPane.showConfirmDialog(null, "Saldo final igual a zero. Deseja realmente finalizar o caixa?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if ((opcao != JOptionPane.OK_OPTION) || (!alterarBD())) {
                    return;
                }
                InserirMovimentoSaldoFinal();
                //              fecharCaixa();
                consultarBD(caixa.getId());
                super.confirmar();
                return;
            }
            if (valorDiferenca.compareTo(BigDecimal.ZERO) != 0) { //verfica se o valor do caixa fecha com diferença
                int opcao = JOptionPane.showConfirmDialog(null, "O caixa será fechado com diferença. Deseja realmente finalizar o caixa?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if ((opcao != JOptionPane.OK_OPTION) || (!alterarBD())) {
                    return;
                }
                InserirMovimentoSaldoFinal();
                //   fecharCaixa();
                consultarBD(caixa.getId());
                super.confirmar();
            } else {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja realmente finalizar o caixa?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if ((opcao != JOptionPane.OK_OPTION) || (!alterarBD())) {
                    return;
                } else {
                    InserirMovimentoSaldoFinal();
                    //    fecharCaixa();
                    consultarBD(caixa.getId());
                    super.confirmar();
                }
            }
        } else if (estadoTela == INCLUINDO) {
            super.confirmar();
        }
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Caixa",
                DaoCaixa.SQLCONSULTARTUDO,
                new String[]{"Código", "Data", "Saldo Inicial", "Saldo Final", "Saldo", "Situação"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                    new FiltroPesquisa("Data", "Data", Date.class),
                    new FiltroPesquisa("Saldo Inicial", "SALDOINICIAL", String.class),
                    new FiltroPesquisa("Saldo Final", "SALDOFINAL", String.class),
                    new FiltroPesquisa("Saldo", "SALDO", String.class),
                    new FiltroPesquisa("Situação", "ABERTO", String.class)
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new CellRendererData(),
                    new MonetarioRender(), new MonetarioRender(), new MonetarioRender(), new RenderizadorStatus()},
                this, this, false, false, false)
        );
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == jbIncluir) {
            if (verificaCaixaAberto()) {
                JOptionPane.showMessageDialog(null, "Já existe um caixa aberto com o código " + caixaID + ".");
            } else {
                incluir();
            }
        } else if (ae.getSource() == jbAlterar) {
            if (verificaCaixaFechado()) {
                JOptionPane.showMessageDialog(null, "O caixa com o código " + campoCodigo.getValor() + " está fechado.");
            } else {
                alterar();
            }
        } else if (ae.getSource() == jbExcluir) {
            excluir();
        } else if (ae.getSource() == jbConsultar) {
            consultar();
        } else if (ae.getSource() == jbConfirmar) {
            confirmar();
        } else if (ae.getSource() == jbCancelar) {
            cancelar();
        } else if (ae.getSource() == jbMovimentoExtra) {
            movimentoExtra();
        }
    }

    private boolean verificaCaixaAberto() {
        String SQL_VERIFICA_CAIXA = "SELECT ID FROM CAIXA WHERE ID = (SELECT MAX(ID) FROM CAIXA) and SITUACAO = 'Aberto'";
        try {
            ResultSet rs = Conexao.getConexao().createStatement().executeQuery(SQL_VERIFICA_CAIXA);
            if (rs.next()) {
                caixaID = rs.getInt(1);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher os campos.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean verificaCaixaFechado() {
        String SQL_VERIFICA_CAIXA = "SELECT ID FROM CAIXA WHERE ID = " + campoCodigo.getValor() + " and SITUACAO = 'Fechado'";
        Integer caixaID = 0;
        System.out.println(SQL_VERIFICA_CAIXA);
        try {
            ResultSet rs = Conexao.getConexao().createStatement().executeQuery(SQL_VERIFICA_CAIXA);
            if (rs.next()) {
                caixaID = rs.getInt(1);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher os campos.");
            e.printStackTrace();
            return false;
        }
    }

    public void pegaIDCaixaAberto() {
        String SQL_VERIFICA_CAIXA = "SELECT ID FROM CAIXA WHERE ID = (SELECT MAX(ID) FROM CAIXA) and SITUACAO = 'Aberto'";
        try {
            ResultSet rs = Conexao.getConexao().createStatement().executeQuery(SQL_VERIFICA_CAIXA);
            if (rs.next()) {
                campoCodigo.setValor(rs.getInt(1));
            } else {
                JOptionPane.showMessageDialog(null, "Não há nenhum caixa aberto.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter o ID do Caixa.");
            e.printStackTrace();
        }
    }

    private boolean InserirMovimentoSaldoFinal() {
        String SQL_INSERE_MOVIMENTO_FINAL = "INSERT INTO MOVIMENTOCAIXA VALUES ('0', 'MOV. FECHAMENTO DE CAIXA', 'C', ?, CURRENT_DATE, CURRENT_TIME, ?, NULL, NULL)";
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_INSERE_MOVIMENTO_FINAL);
            ps.setBigDecimal(1, campoSaldoFinal.getValor());
            ps.setInt(2, (int) campoCodigo.getValor());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar inserir um movimento final no caixa (TelaCaixa).");
            return false;
        }
    }

    public boolean fecharCaixa() {
        Caixa caixa = new Caixa();
        DaoCaixa daoCaixa = new DaoCaixa(caixa);
        String SQL_ATUALIZAR_SALDO = "UPDATE CAIXA SET SALDOFINAL = ?, SITUACAO = ? WHERE ID = ?";
//        BigDecimal auxiliarSaldo = BigDecimal.ZERO;
//
//        caixa.setId((int) campoCodigo.getValor());
//        daoCaixa.consultar();
//        caixa.setAberto(false);
//        auxiliarSaldo = caixa.getSaldo().subtract(campoSaldoFinal.getValor());
//        if (auxiliarSaldo.compareTo(BigDecimal.ZERO) == -1) { 
//            JOptionPane.showMessageDialog(null, "Caixa fechado com valor negativo.");
//        }
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_ATUALIZAR_SALDO);
            ps.setBigDecimal(1, campoSaldoFinal.getValor());
            ps.setString(2, (caixa.isAberto() ? "Aberto" : "Fechado"));
            ps.setInt(3, (int) campoCodigo.getValor());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar fechar o caixa (TelaCaixa).");
            return false;
        }
    }

    private void criarPainelSaldoCaixa() {
        BigDecimal saldo = caixa.getSaldo();
        labelSaldoCaixa.setText(NumberFormat.getCurrencyInstance().format(saldo));
        labelSaldoAtual.setFont(new Font("Tahoma", Font.BOLD, 20));
        labelSaldoAtual.setHorizontalAlignment(SwingConstants.CENTER);
        labelSaldoCaixa.setFont(new Font("Tahoma", Font.ITALIC, 18));
        labelSaldoCaixa.setHorizontalAlignment(SwingConstants.CENTER);
        labelTotalEntradas.setFont(new Font("Tahoma", Font.BOLD, 12));
        labelTotalSaidas.setFont(new Font("Tahoma", Font.BOLD, 12));
        labelSaldoAtual.setForeground(Color.decode("#000000"));
        labelTotalEntradas.setForeground(Color.decode("#000000"));
        labelTotalSaidas.setForeground(Color.decode("#000000"));
        painelSaldoCaixa.setBorder(BorderFactory.createTitledBorder("Resumo do caixa"));
        somaTotalEntradasSaidas(caixa.getId());
        somaTotalMovimentos();
        painelSaldoCaixa.add(labelSaldoAtual);
        painelSaldoCaixa.add(labelSaldoCaixa);
        painelSaldoCaixa.add(labelTotalEntradas);
        painelSaldoCaixa.add(labelTotalSaidas);
        switch (saldo.compareTo(BigDecimal.ZERO)) {
            case -1:
                labelSaldoCaixa.setForeground(Color.decode("#D80027"));
                break;
            case 0:
                labelSaldoCaixa.setForeground(Color.decode("#000000"));
                break;
            default:
                labelSaldoCaixa.setForeground(Color.decode("#1f870a"));
                break;
        }
        setSize(1350, 630);
        //pack();
    }

    private void somaTotalEntradasSaidas(int idCaixa) {
        Double auxTotalEntradas = 0.0;
        Double auxTotalSaidas = 0.0;
        BigDecimal totalEntradas = BigDecimal.ZERO;
        BigDecimal totalSaidas = BigDecimal.ZERO;
        try {
            //adicionado AND DESCRICAO <> 'MOV. ABERTURA DE CAIXA' para não somar no totalSaidas o valor do fechamento de caixa. Caso seja necessário somar basta retirar
            String sqlEntradas = "SELECT SUM(VALOR) FROM MOVIMENTOCAIXA WHERE IDCAIXA = ? AND DEBITOCREDITO = 'D' AND DESCRICAO <> 'MOV. ABERTURA DE CAIXA'";
            caixa.setId(idCaixa);
            PreparedStatement ps = Conexao.getConexao().prepareStatement(sqlEntradas);
            ps.setInt(1, caixa.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                auxTotalEntradas = rs.getDouble(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar as entradas do caixa.");
            e.printStackTrace();
        }

        try {
            //adicionado AND DESCRICAO <> 'MOV. FECHAMENTO DE CAIXA' para não somar no totalSaidas o valor do fechamento de caixa. Caso seja necessário somar basta retirar
            String sqlSaidas = "SELECT SUM(VALOR) FROM MOVIMENTOCAIXA WHERE IDCAIXA = ? AND DEBITOCREDITO = 'C' AND DESCRICAO <>'MOV. FECHAMENTO DE CAIXA'";
            caixa.setId(idCaixa);
            PreparedStatement ps = Conexao.getConexao().prepareStatement(sqlSaidas);
            ps.setInt(1, caixa.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                auxTotalSaidas = rs.getDouble(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar as saídas do caixa.");
            e.printStackTrace();
        }
        totalEntradas = BigDecimal.valueOf(auxTotalEntradas);
        totalSaidas = BigDecimal.valueOf(auxTotalSaidas);
        labelTotalEntradas.setForeground(Color.decode("#1f870a"));
        labelTotalEntradas.setText("( + )  Total Entradas:  " + NumberFormat.getCurrencyInstance().format(totalEntradas));
        labelTotalSaidas.setForeground(Color.decode("#D80027"));
        labelTotalSaidas.setText("( - )  Total Saídas:      " + NumberFormat.getCurrencyInstance().format(totalSaidas));
        labelTotalEntradas.setHorizontalAlignment(SwingConstants.CENTER);
        labelTotalSaidas.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void somaTotalMovimentos() {
        BigDecimal saldo = BigDecimal.ZERO;
        for (int i = 0; i < tabelaMovimentosCaixa.getTableModel().getRowCount(); i++) {
            if (tabelaMovimentosCaixa.getTableModel().getValueAt(i, 2).equals("D")) {
                BigDecimal valorContaTabela = (BigDecimal) tabelaMovimentosCaixa.getTableModel().getValueAt(i, 3);
                saldo = saldo.add(valorContaTabela);
            } else if (tabelaMovimentosCaixa.getTableModel().getValueAt(i, 2).equals("C")) {
                BigDecimal valorContaTabela = (BigDecimal) tabelaMovimentosCaixa.getTableModel().getValueAt(i, 3);
                saldo = saldo.subtract(valorContaTabela);
            }
        }
        System.out.println("Valor total a pagar: " + saldo);
        System.out.println("Valor total a pagar: " + NumberFormat.getCurrencyInstance().format(saldo));
    }

//    private void somaTotalEntradasSaidas() {
//        BigDecimal somaEntradas = BigDecimal.ZERO;
//        BigDecimal somaSaidas = BigDecimal.ZERO;
//        for (int i = 0; i < tabelaMovimentosCaixa.getTableModel().getRowCount(); i++) {
//            if (tabelaMovimentosCaixa.getTableModel().getValueAt(i, 2).equals("D")) {
//                BigDecimal valorTabelaMovimentos = (BigDecimal) tabelaMovimentosCaixa.getTableModel().getValueAt(i, 3);
//                somaEntradas = somaEntradas.add(valorTabelaMovimentos);
//            } else {
//                BigDecimal valorTabelaMovimentos = (BigDecimal)tabelaMovimentosCaixa.getTableModel().getValueAt(i, 3);
//                somaSaidas = somaSaidas.add(valorTabelaMovimentos);
//            }
//        }
//        labelTotalEntradas.setForeground(Color.decode("#1f870a"));
//        labelTotalEntradas.setText("( + )  Total Entradas:  " + NumberFormat.getCurrencyInstance().format(somaEntradas));
//        labelTotalSaidas.setForeground(Color.decode("#D80027"));
//        labelTotalSaidas.setText("( - )  Total Saídas:      " + NumberFormat.getCurrencyInstance().format(somaSaidas));
//        labelTotalEntradas.setHorizontalAlignment(SwingConstants.CENTER);
//        labelTotalSaidas.setHorizontalAlignment(SwingConstants.CENTER);
//    }
    private void criarLabelIDStatusCaixa() {
        painelSituacaoCaixa.remove(labelCodigoStatusCaixa);
        painelSituacaoCaixa.remove(labelIconStatusCaixa);
        jpStatusCaixa.remove(painelSituacaoCaixa);
        if (campoAberto.getValor() == true) { //aberto
            ImageIcon imageIconAberto = new ImageIcon(getClass().getResource("/icones/caixaAberto24px.png"));
            labelCodigoStatusCaixa = new JLabel(caixa.getId() + " - Caixa Aberto ");
            labelIconStatusCaixa = new JLabel(imageIconAberto);
            labelCodigoStatusCaixa.setForeground(Color.decode("#1f870a"));
        } else if (campoAberto.getValor() == false) { //fechado
            ImageIcon imageIconFechado = new ImageIcon(getClass().getResource("/icones/caixaFechado24px.png"));
            labelCodigoStatusCaixa = new JLabel(caixa.getId() + " - Caixa Fechado ");
            labelIconStatusCaixa = new JLabel(imageIconFechado);
            labelCodigoStatusCaixa.setForeground(Color.decode("#D80027"));
        }
        labelCodigoStatusCaixa.setFont(new Font("Tahoma", Font.ITALIC, 22));
        painelSituacaoCaixa.add(labelCodigoStatusCaixa);
        painelSituacaoCaixa.add(labelIconStatusCaixa);
        adicionaComponente(2, 1, 1, 1, painelSituacaoCaixa, jpStatusCaixa);
        setSize(1350, 630);
        //pack();
    }
}
