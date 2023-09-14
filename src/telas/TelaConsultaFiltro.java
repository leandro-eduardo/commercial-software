package telas;

import bd.Conexao;
import com.toedter.calendar.JDateChooser;
import componentes.MeuCampoBuscar;
import componentes.MeuJComboBox;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import pojo.ParametrosConsulta;
import telas_relatorios.TelaRelatorioClientesFornecedoresFuncionarios;
import telas_relatorios.TelaRelatorioCompras;
import telas_relatorios.TelaRelatorioContasPagar;
import telas_relatorios.TelaRelatorioContasReceber;
import telas_relatorios.TelaRelatorioInadimplenciasCliente;
import telas_relatorios.TelaRelatorioIndicadoresPagamento;
import telas_relatorios.TelaRelatorioIndicadoresRecebimento;
import telas_relatorios.TelaRelatorioPagamentosRecebimentos;
import telas_relatorios.TelaRelatorioProduto;
import telas_relatorios.TelaRelatorioVendas;
import util.RestricaoCaracteres;
import util.RestricaoCaracteresPesquisar;
import util.RestricaoCaracteresPesquisarData;

public class TelaConsultaFiltro extends JInternalFrame implements MouseListener {

    JMenuItem menuItemEnviarSelecionados = new JMenuItem("Enviar selecionados");
    JMenuItem menuItemCancelar = new JMenuItem("Cancelar");
    private JPopupMenu jPopupMenu;

    TelaRelatorioClientesFornecedoresFuncionarios trc;
    //instâncias de paginação **
    private int tamanhoPagina = 10;

    Preferences prefs = Preferences.userNodeForPackage(TelaConsultaFiltro.class);
    final String USER_PREF_TAMANHO_PAGINA = "tamanhoPagina";

    private ArrayList guardaSqlPaginas = new ArrayList();
    public JButton jbProximaPagina = new JButton(">");
    public JButton jbUltimaPagina = new JButton(">>");
    private float numeroPaginaFloat;
    private int numeroPaginaInt;
    public int numeroPaginaAtual = 1;
    public JLabel labelPaginaAtual = new JLabel(String.valueOf(numeroPaginaAtual));
    public JButton jbAnteriorPagina = new JButton("<");
    public JButton jbPrimeiraPagina = new JButton("<<");
    private JLabel labelTamanhoPagina = new JLabel("Itens: ");
    String[] valoresLimitePaginas = {"3", "10", "20", "30"};
    public JComboBox limiteRegistros = new JComboBox(valoresLimitePaginas);
    private JPanel painelPaginacao = new JPanel();
    private JPanel painelConteudo = new JPanel();
    //**

    public static boolean clicouConsultar = false;

    public static Object chamador;
    public String guardaSql;
    public String guardaSql2;
    public String guardaSqlData;
    public ParametrosConsulta parametrosConsulta;
    private JPanel painel = new JPanel();
    //private JPanel painelPesquisar = new JPanel();
    private JPanel painelOrdenar = new JPanel();
    private JPanel painelData = new JPanel();
    private JPanel painelCheckBoxData = new JPanel();
    private JPanel painelPessoa = new JPanel();
    private JComboBox jcbPesquisar = new JComboBox();
    private JComboBox jcbOrdenar = new JComboBox();
    private JTextField textoPesquisar = new JTextField(20);
    private JDateChooser dataInicial = new JDateChooser();
    private JDateChooser dataFinal = new JDateChooser();
    private JCheckBox checkBoxData = new JCheckBox();
    private MeuJComboBox comboBoxContaGerada = new MeuJComboBox(true, "Listar apenas contas geradas?", new String[][]{{"N", "Não"}, {"S", "Sim"}});
    private JCheckBox checkBoxCliente = new JCheckBox();
    private JCheckBox checkBoxFornecedor = new JCheckBox();
    private JCheckBox checkBoxFuncionario = new JCheckBox();
    private JButton botaoListar = new JButton("Listar");
    private JButton botaoLimpar = new JButton("Limpar");
    private JButton botaoData = new JButton("Buscar");
    private JTable tabela;
    private JScrollPane jsp;
    private DefaultTableModel dtm;

    public TelaConsultaFiltro(ParametrosConsulta parametrosConsulta) {
        super(parametrosConsulta.getTituloConsulta(), true, true, true, true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/consultar.png")));
        this.parametrosConsulta = parametrosConsulta;
        TelaSistema.jdp.add(this);
        TelaSistema.jdp.moveToFront(this);
        TelaSistema.jdp.setSelectedFrame(this);
        dtm = new DefaultTableModel(new Object[][]{},
                parametrosConsulta.getColunas());
        tabela = new JTable(dtm) {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }

            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                //int rendererWidth = c.getPreferredSize().width;
                //TableColumn tableColumn = getColumnModel().getColumn(column);
                //tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                if (row % 2 == 0) {
                    c.setBackground(Color.LIGHT_GRAY);
                } else {
                    c.setBackground(Color.GRAY);
                }
                if (isCellSelected(row, column)) {
                    c.setBackground(new Color(66, 134, 244));
                }
                return c;
            }
        };
        jsp = new JScrollPane(tabela);
        //TAMANHO TABELA = java.awt.Dimension[width=1064,height=399]
        //jsp = new JScrollPane(tabela, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        painel.setLayout(new FlowLayout());
        painelConteudo.setLayout(new BorderLayout());
        //painelPesquisar.setLayout(new FlowLayout());
        painelPaginacao.setLayout(new FlowLayout());
        JLabel labelOrdenar = new JLabel("Ordenar: ");

        painelPaginacao.add(jbPrimeiraPagina);
        painelPaginacao.add(jbAnteriorPagina);
        painelPaginacao.add(labelPaginaAtual);
        painelPaginacao.add(jbProximaPagina);
        painelPaginacao.add(jbUltimaPagina);
        painelPaginacao.add(labelTamanhoPagina);
        painelPaginacao.add(limiteRegistros);

        painel.add(jcbPesquisar);
        jcbPesquisar.setToolTipText("Selecione um filtro e digite o que deseja para iniciar uma pesquisa.");
        painel.add(textoPesquisar);
        painel.add(botaoListar);
        painel.add(botaoLimpar);
        botaoListar.setToolTipText("Listar todos os cadastros ordenados pelo código.");

        painelOrdenar.add(labelOrdenar);
        painelOrdenar.add(jcbOrdenar);
        painel.add(painelOrdenar);
        textoPesquisar.setEnabled(false);

        if (prefs.get(USER_PREF_TAMANHO_PAGINA, "value").equals("value")) { //se a preferencia do usuário ainda não existir, define 10 como padrão para o tamanho das páginas e seleciona o combobox em no index, que é o valor 10
            limiteRegistros.setSelectedIndex(1);
        } else if (prefs.get(USER_PREF_TAMANHO_PAGINA, "value").equals("3")) { //define o combobox no index 0, que equivale a 3 itens por página
            limiteRegistros.setSelectedIndex(0);
        } else if (prefs.get(USER_PREF_TAMANHO_PAGINA, "value").equals("10")) { //define o combobox no index 1, que equivale a 10 itens por página
            limiteRegistros.setSelectedIndex(1);
        } else if (prefs.get(USER_PREF_TAMANHO_PAGINA, "value").equals("20")) { //define o combobox no index 2, que equivale a 20 itens por página
            limiteRegistros.setSelectedIndex(2);
        } else if (prefs.get(USER_PREF_TAMANHO_PAGINA, "value").equals("30")) { //define o combobox no index 3, que equivale a 30 itens por página
            limiteRegistros.setSelectedIndex(3);
        }

        limiteRegistros.setEnabled(false);
        jbAnteriorPagina.setEnabled(false);
        jbProximaPagina.setEnabled(false);
        jbUltimaPagina.setEnabled(false);
        jbPrimeiraPagina.setEnabled(false);

        painelConteudo.add(jsp);
        getContentPane().add("North", painel);
        getContentPane().add("South", painelConteudo);
        painelConteudo.add(painelPaginacao, BorderLayout.SOUTH);
        painelConteudo.setPreferredSize(new Dimension(0, 486)); //com esse tamanho a JTable mostra exatamente 20 itens por página
        //getContentPane().add("South", painelPaginacao);
        jsp.setPreferredSize(new Dimension(800, 400));
        if (dtm.getRowCount() <= 0) {
            jcbOrdenar.setEnabled(false);
        }
        //setSize(1100, 520);
        setSize(1100, 610);
        setVisible(true);
        preencherComboBox();
        centralizaTela();
        adicionaListeners();
        adicionaListenersBotoes();
        if (parametrosConsulta.isPodeHabilitarData() == true) {
            adicionaListenersData();
        }
        if (parametrosConsulta.isPodeHabilitarPessoa() == true) {
            adicionaListenersPessoa();
        }
        aplicaRender();
        tabela.addMouseListener(this);
        //tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        guardaSql2 = this.parametrosConsulta.getSql();
        System.out.println("VALOR DO GUARDA SQL2 = " + guardaSql2);

        //verifica se a tela é de consulta para relatório, e então adiciona o PopupMenu que é acionado clicando com o botão direito do Mouse para abrir o JMenu de enviar as linhas 
        //selecionadas
        if ("Consulta de Pessoa para Relatório".equals(parametrosConsulta.getTituloConsulta()) || "Consulta de Cidade para Relatório".equals(parametrosConsulta.getTituloConsulta())
                || "Consulta de Estado para Relatório".equals(parametrosConsulta.getTituloConsulta()) || "Consulta de Produto para Relatório".equals(parametrosConsulta.getTituloConsulta())
                || "Consulta de Conta a Pagar para Relatório".equals(parametrosConsulta.getTituloConsulta()) || "Consulta de Conta a Receber para Relatório".equals(parametrosConsulta.getTituloConsulta())
                || "Consulta de Venda para Relatório".equals(parametrosConsulta.getTituloConsulta()) || "Consulta de Compra para Relatório".equals(parametrosConsulta.getTituloConsulta())
                || "Consulta de Pagamentos para Relatório".equals(parametrosConsulta.getTituloConsulta()) || "Consulta de Recebimentos para Relatório".equals(parametrosConsulta.getTituloConsulta())
                || "Consulta de Cliente para Relatório".equals(parametrosConsulta.getTituloConsulta()) || "Consulta de Fornecedor para Relatório".equals(parametrosConsulta.getTituloConsulta())
                || "Consulta de Funcionário para Relatório".equals(parametrosConsulta.getTituloConsulta()) || "Consulta de Fabricante para Relatório".equals(parametrosConsulta.getTituloConsulta())
                || "Consulta de Categoria para Relatório".equals(parametrosConsulta.getTituloConsulta())) {
            jPopupMenu = new JPopupMenu();
            jPopupMenu.add(menuItemEnviarSelecionados);
            jPopupMenu.add(menuItemCancelar);
            tabela.setComponentPopupMenu(jPopupMenu);
        }

    }

    private void aplicaRender() {
        TableColumnModel tableModel = tabela.getColumnModel();
        tableModel.getColumn(0).setMaxWidth(60);  //tamanho da coluna 0 (geralmente onde está o campo código) em todas as telas de consulta.
        if ("Consulta de Caixa".equals(parametrosConsulta.getTituloConsulta())) {
            tableModel.getColumn(1).setMinWidth(80);
            tableModel.getColumn(1).setMaxWidth(80);
        }
        tableModel.getColumn(1).setMinWidth(410); //tamanho da coluna 1 (geralmente onde está os maiores campos) em todas as telas de consulta.
        tableModel.getColumn(1).setMaxWidth(410); //tamanho da coluna 1 (geralmente onde está os maiores campos) em todas as telas de consulta.
        if ("Consulta de Veículo".equals(parametrosConsulta.getTituloConsulta()) || "Consulta de Veículos".equals(parametrosConsulta.getTituloConsulta())) {
            tableModel.getColumn(4).setMinWidth(180);
            tableModel.getColumn(4).setMaxWidth(180);
        }
        if ("Consulta de Movimentos".equals(parametrosConsulta.getTituloConsulta())) {
            tableModel.getColumn(2).setMinWidth(50);
            tableModel.getColumn(2).setMaxWidth(50);
        }

        for (int i = 0; i < dtm.getColumnCount(); i++) {
            if (parametrosConsulta.getRenderizadores()[i] != null) {
                tableModel.getColumn(i).setCellRenderer(parametrosConsulta.getRenderizadores()[i]);
            }
        }
    }

    private void avancarPagina() {
        numeroPaginaAtual++;
        labelPaginaAtual.setText(String.valueOf(numeroPaginaAtual) + "/" + numeroPaginaInt);
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        for (int i = 0; i < numeroPaginaInt; i++) {
            if (numeroPaginaAtual == i + 1) {
                List<Object[]> dados = Conexao.pesquisar((String) guardaSqlPaginas.get(numeroPaginaAtual - 1));
                for (int x = 0; x < dados.size(); x++) {
                    dtm.addRow(dados.get(x));
                }
            }
        }
    }

    private void voltarPagina() {
        numeroPaginaAtual--;
        labelPaginaAtual.setText(String.valueOf(numeroPaginaAtual) + "/" + numeroPaginaInt);
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        for (int i = 0; i < numeroPaginaInt; i++) {
            if (numeroPaginaAtual == i + 1) {
                List<Object[]> dados = Conexao.pesquisar((String) guardaSqlPaginas.get(numeroPaginaAtual - 1));
                for (int x = 0; x < dados.size(); x++) {
                    dtm.addRow(dados.get(x));
                }
            }
        }
    }

    public void gerarPaginas(String sql) { //método gera as paginas necessárias na tabela, além dos sql's que serão utilizados em cada uma delas.
        String tempSql = sql;
        if (prefs.get(USER_PREF_TAMANHO_PAGINA, "value").equals("value")) { //se a preferencia do usuário ainda não existir, define 10 como padrão para o tamanho das páginas
            tamanhoPagina = 10;
        } else { //se não, pega o valor da preferencia do usuário
            tamanhoPagina = Integer.parseInt(prefs.get(USER_PREF_TAMANHO_PAGINA, "value"));
        }
        numeroPaginaFloat = numeroRegistros(tempSql) / (float) tamanhoPagina;
        numeroPaginaFloat = (float) Math.ceil(numeroPaginaFloat);
        numeroPaginaInt = (int) numeroPaginaFloat;

        if (numeroPaginaInt == 0) {
            //return para sair do método pois não existem dados
            return;
        }
        //inicio do procedimento que gera os SQLS com os valores dinamicamente
        int valorSkip = 0;
        for (int i = 0; i < numeroPaginaInt; i++) {
            String sqlPaginas = "SELECT FIRST ";
            sqlPaginas = sqlPaginas + tamanhoPagina + " SKIP ";
            sqlPaginas = sqlPaginas + valorSkip + " * FROM (" + tempSql + ")";
            valorSkip += Integer.valueOf((String) limiteRegistros.getSelectedItem());
            guardaSqlPaginas.add(i, sqlPaginas);

//            System.out.println("\nsqlPaginas " + i + ": " + sqlPaginas);
//            System.out.println("valorSkip " + i + ": " + valorSkip);
        }
        System.out.println("\nPRINTANDO OS SQLS \n" + guardaSqlPaginas);
        //fim do procedimento que gera os SQLS com os valores dinamicamente 

        ultimaPagina();
        limiteRegistros.setEnabled(true);
        verificaBotoesLimitePagina();
    }

    public void ultimaPagina() { //método que vai sempre para a última página da tabela quando realizada uma consulta
        numeroPaginaAtual = numeroPaginaInt;
        labelPaginaAtual.setText(String.valueOf(numeroPaginaAtual) + "/" + numeroPaginaInt);
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        List<Object[]> dados = Conexao.pesquisar((String) guardaSqlPaginas.get(numeroPaginaInt - 1));
        for (int x = 0; x < dados.size(); x++) {
            dtm.addRow(dados.get(x));
        }
    }

    public void primeiraPagina() { //método que vai sempre para a última página da tabela quando realizada uma consulta
        numeroPaginaAtual = numeroPaginaInt - numeroPaginaInt + 1;
        labelPaginaAtual.setText(String.valueOf(numeroPaginaAtual) + "/" + numeroPaginaInt);
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        List<Object[]> dados = Conexao.pesquisar((String) guardaSqlPaginas.get(numeroPaginaAtual - 1));
        for (int x = 0; x < dados.size(); x++) {
            dtm.addRow(dados.get(x));
        }
    }

    private int numeroRegistros(String sql) {
        try {
            int numeroRegistros = 0;
            String tempSql = sql;
            String sqlRegistros = "SELECT COUNT(ID) FROM (" + tempSql + ")";
            PreparedStatement ps = Conexao.getConexao().prepareStatement(sqlRegistros);
            // ps.setInt(1, caixa.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                numeroRegistros = rs.getInt(1);
            }
            return numeroRegistros;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter os registros.");
            e.printStackTrace();
            return 0;
        }
    }

    public void verificaBotoesLimitePagina() {
        if (numeroPaginaAtual == numeroPaginaInt) {
            jbProximaPagina.setEnabled(false);
            jbUltimaPagina.setEnabled(false);
        }
        if (numeroPaginaAtual == 1) {
            jbAnteriorPagina.setEnabled(false);
            jbPrimeiraPagina.setEnabled(false);
        }
        if (numeroPaginaAtual != 1) {
            jbAnteriorPagina.setEnabled(true);
            jbPrimeiraPagina.setEnabled(true);
        }
        if (numeroPaginaAtual != numeroPaginaInt) {
            jbProximaPagina.setEnabled(true);
            jbUltimaPagina.setEnabled(true);
        }
    }

    public void adicionaListenersBotoes() {

        jbUltimaPagina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ultimaPagina();
                verificaBotoesLimitePagina();
            }
        });

        jbPrimeiraPagina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                primeiraPagina();
                verificaBotoesLimitePagina();
            }
        });

        jbProximaPagina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                avancarPagina();
                verificaBotoesLimitePagina();
            }
        });

        jbAnteriorPagina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                voltarPagina();
                verificaBotoesLimitePagina();
            }
        });

        limiteRegistros.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if (limiteRegistros.getSelectedItem() == "3") {
                    tamanhoPagina = 3;
                    prefs.putInt(USER_PREF_TAMANHO_PAGINA, tamanhoPagina);
                    gerarPaginas(guardaSql2);
                }
                if (limiteRegistros.getSelectedItem() == "10") {
                    tamanhoPagina = 10;
                    prefs.putInt(USER_PREF_TAMANHO_PAGINA, tamanhoPagina);
                    gerarPaginas(guardaSql2);
                }
                if (limiteRegistros.getSelectedItem() == "20") {
                    tamanhoPagina = 20;
                    prefs.putInt(USER_PREF_TAMANHO_PAGINA, tamanhoPagina);
                    gerarPaginas(guardaSql2);
                }
                if (limiteRegistros.getSelectedItem() == "30") {
                    tamanhoPagina = 30;
                    prefs.putInt(USER_PREF_TAMANHO_PAGINA, tamanhoPagina);
                    gerarPaginas(guardaSql2);
                }
            }
        });
    }

    private void adicionaListenersPessoa() {
        getContentPane().add("West", painelPessoa);
        JLabel labelCliente = new JLabel("Cliente");
        JLabel labelFornecedor = new JLabel("Fornecedor");
        JLabel labelFuncionario = new JLabel("Funcionario");

        JPanel espacador1 = new JPanel();
        espacador1.setPreferredSize(new Dimension(300, 10));
        painelPessoa.add(espacador1);
        painelPessoa.add(labelCliente);
        painelPessoa.add(checkBoxCliente);
        painelPessoa.add(labelFornecedor);
        painelPessoa.add(checkBoxFornecedor);
        painelPessoa.add(labelFuncionario);
        painelPessoa.add(checkBoxFuncionario);

        checkBoxCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (checkBoxCliente.isSelected()) {
                    preencherDadosPesquisar();
                }
            }
        });
        checkBoxFornecedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (checkBoxFornecedor.isSelected()) {
                    preencherDadosPesquisar();
                }
            }
        });
        checkBoxFuncionario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (checkBoxFuncionario.isSelected()) {
                    preencherDadosPesquisar();
                }
            }
        });
        setSize(1100, 610);
    }

    private void adicionaListenersData() {
        getContentPane().add("East", painelData);
        getContentPane().add("West", painelCheckBoxData);

        JPanel espacador1 = new JPanel();
        espacador1.setPreferredSize(new Dimension(65, 10));
        painelCheckBoxData.add(espacador1);
        JLabel labelCheckBoxData = new JLabel("Exibir filtro de data:");
        painelCheckBoxData.add(labelCheckBoxData);
        painelCheckBoxData.add(checkBoxData);

        JPanel espacador = new JPanel();
        espacador.setPreferredSize(new Dimension(308, 10));
        JLabel labelDataInicial = new JLabel("Data inicial:");
        painelData.add(labelDataInicial);
        painelData.add(dataInicial);
        JLabel labelDataFinal = new JLabel("Data final:");
        painelData.add(labelDataFinal);
        painelData.add(dataFinal);
        painelData.add(botaoData);
        painelData.add(espacador);
        painelData.setVisible(false);
        setSize(1100, 610);

        checkBoxData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (checkBoxData.isSelected()) {
                    jcbOrdenar.setEnabled(false);
                    painelData.setVisible(true);
                    textoPesquisar.setText("");
                    textoPesquisar.setEnabled(false);
                    limiteRegistros.setEnabled(false);
                    setSize(1100, 610);
                } else {
                    jcbOrdenar.setEnabled(true);
                    painelData.setVisible(false);
                    setSize(1100, 610);
                }
            }
        });

        dataInicial.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                botaoData.setEnabled(true);
            }

            @Override
            public void focusLost(FocusEvent fe) {

            }
        });

        botaoData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                jcbOrdenar.setEnabled(false);
                preencherDadosData();
                labelPaginaAtual.setText(String.valueOf(numeroPaginaAtual) + "/" + numeroPaginaInt);
                if (dtm.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Nenhum resultado encontrado.");
                }
                if (dtm.getRowCount() > 0) {

                }
            }
        });
        dataInicial.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    preencherDadosData();
                    if (dtm.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Nenhum resultado encontrado.");
                    }
                    if (dtm.getRowCount() > 0) {
                        jcbOrdenar.setEnabled(true);
                    }
                }
            }
        });
    }

//    private void adicionaListenersContaPagar() {
//        painel.add(painelComboBoxContaGerada);
//        JLabel labelContaPagar = new JLabel("Exibir apenas contas geradas: ");
//
//        painelComboBoxContaGerada.add(labelContaPagar);
//        painelComboBoxContaGerada.add(comboBoxContaGerada);
//
////        comboBoxContaGerada.addActionListener(new ActionListener() {
////            @Override
////            public void actionPerformed(ActionEvent ae) {
////                if (checkBoxContaPagarGerada.isSelected()) {
////                    preencherDadosPesquisar();
////                }
////                if (dtm.getRowCount() > 0) {
////                    jcbOrdenar.setEnabled(true);
////                }
////            }
////        });
//        setSize(1100, 610);
//    }
    private void adicionaListeners() {

        textoPesquisar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (textoPesquisar.getText().isEmpty()) {
                            while (dtm.getRowCount() > 0) {
                                dtm.removeRow(0);
                            }
                            jcbOrdenar.setEnabled(false);
                            return;
                        }
                        if (dtm.getRowCount() >= 0) {
                            jcbOrdenar.setEnabled(true);
                        }
                        preencherDadosPesquisar();
                    }
                });
            }
        });
        textoPesquisar.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                botaoData.setEnabled(true);
                botaoListar.setEnabled(false);
                comboBoxContaGerada.setEnabled(false);
                if (jcbPesquisar.getSelectedItem().equals("Código")) {
                    textoPesquisar.setDocument(new RestricaoCaracteresPesquisar(20));
                } else if (jcbPesquisar.getSelectedItem().equals("Data")) {
                    textoPesquisar.setDocument(new RestricaoCaracteresPesquisarData(10));
                } else {
                    textoPesquisar.setDocument(new RestricaoCaracteres(20));
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                botaoListar.setEnabled(true);
                comboBoxContaGerada.setEnabled(true);
            }
        });
        jcbPesquisar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (jcbPesquisar.getSelectedIndex() == 0) {
                    textoPesquisar.setEnabled(false);
                } else {
                    jcbOrdenar.setSelectedIndex(0);
                    jcbOrdenar.setEnabled(false);
                    limiteRegistros.setEnabled(false);
                    jbAnteriorPagina.setEnabled(false);
                    jbProximaPagina.setEnabled(false);
                    jbUltimaPagina.setEnabled(false);
                    jbPrimeiraPagina.setEnabled(false);
                    numeroPaginaInt = 1;
                    numeroPaginaAtual = 1;
                    labelPaginaAtual.setText(String.valueOf(numeroPaginaAtual) + "/" + numeroPaginaInt);
                    textoPesquisar.setEnabled(true);
                }
            }
        });
        botaoListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                checkBoxData.setSelected(false);
                //checkBoxContaPagarGerada.setSelected(false);
                jcbPesquisar.setSelectedIndex(0);
                jcbOrdenar.setSelectedIndex(0);
                checkBoxCliente.setSelected(false);
                checkBoxFornecedor.setSelected(false);
                checkBoxFuncionario.setSelected(false);
                jcbOrdenar.setEnabled(true);
                painelData.setVisible(false);
                preencherDadosListar();
                setSize(1100, 610);
                if (dtm.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Não existem dados cadastrados.");
                    //telaChamadora.pesquisaSemDados();
                }
                if (dtm.getRowCount() > 0) {
                    jcbOrdenar.setEnabled(true);
                }
            }
        });

        botaoLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                while (dtm.getRowCount() > 0) {
                    dtm.removeRow(0);
                    jcbOrdenar.setEnabled(false);
                    jcbPesquisar.setSelectedIndex(0);
                    jcbOrdenar.setSelectedIndex(0);
                    textoPesquisar.setText("");
                    checkBoxCliente.setSelected(false);
                    checkBoxFornecedor.setSelected(false);
                    checkBoxFuncionario.setSelected(false);

                    limiteRegistros.setEnabled(false);
                    jbAnteriorPagina.setEnabled(false);
                    jbProximaPagina.setEnabled(false);
                    jbUltimaPagina.setEnabled(false);
                    jbPrimeiraPagina.setEnabled(false);
                    numeroPaginaInt = 1;
                    numeroPaginaAtual = 1;
                    labelPaginaAtual.setText(String.valueOf(numeroPaginaAtual) + "/" + numeroPaginaInt);
                    botaoLimpar.requestFocus();
                    checkBoxData.setSelected(false);
                    jcbOrdenar.setEnabled(true);
                    painelData.setVisible(false);
                    setSize(1100, 610);
                }
                //checkBoxContaPagarGerada.setSelected(false);
            }
        });

        jcbPesquisar.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    textoPesquisar.requestFocus();
                }
            }
        });

        jcbOrdenar.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    preencherDadosPesquisar();
                }
            }
        });

        menuItemEnviarSelecionados.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int linhaSelecionada = tabela.getSelectedRow();
                    String codigo = (String) dtm.getValueAt(linhaSelecionada, 0);
                    int pk = Integer.parseInt(codigo);
                    chamador = parametrosConsulta.getChamador();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Selecione pelo menos uma linha.");
                    return;
                }
                ArrayList<String> guardaCodigos = new ArrayList<>();
                int[] selectedRowIndexes = tabela.getSelectedRows();
                for (int i = 0; i < selectedRowIndexes.length; i++) {
                    if (tabela.getSelectedRowCount() != 0) {
                        guardaCodigos.add((String) dtm.getValueAt(selectedRowIndexes[i], 0));
                    }
                }
                String listaCodigos = String.join(",", guardaCodigos);

                if (null != parametrosConsulta.getTituloConsulta()) {
                    switch (parametrosConsulta.getTituloConsulta()) {
                        case "Consulta de Cliente para Relatório":
                            if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioClientesFornecedoresFuncionarios.class) {
                                if (((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            } else if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioContasReceber.class) {
                                if (((TelaRelatorioContasReceber) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioContasReceber) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioContasReceber) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioContasReceber) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioContasReceber) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            } else if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioVendas.class) {
                                if (((TelaRelatorioVendas) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioVendas) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioVendas) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioVendas) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioVendas) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            } else if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioPagamentosRecebimentos.class) {
                                if (((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            } else if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioInadimplenciasCliente.class) {
                                if (((TelaRelatorioInadimplenciasCliente) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioInadimplenciasCliente) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioInadimplenciasCliente) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioInadimplenciasCliente) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioInadimplenciasCliente) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            } else if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioIndicadoresRecebimento.class) {
                                if (((TelaRelatorioIndicadoresRecebimento) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioIndicadoresRecebimento) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioIndicadoresRecebimento) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioIndicadoresRecebimento) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioIndicadoresRecebimento) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            }
                            break;
                        case "Consulta de Fornecedor para Relatório":
                            if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioClientesFornecedoresFuncionarios.class) {
                                if (((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            } else if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioContasPagar.class) {
                                if (((TelaRelatorioContasPagar) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioContasPagar) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioContasPagar) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioContasPagar) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioContasPagar) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            } else if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioCompras.class) {
                                if (((TelaRelatorioCompras) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioCompras) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioCompras) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioCompras) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioCompras) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            } else if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioPagamentosRecebimentos.class) {
                                if (((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            } else if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioIndicadoresPagamento.class) {
                                if (((TelaRelatorioIndicadoresPagamento) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioIndicadoresPagamento) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioIndicadoresPagamento) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioIndicadoresPagamento) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioIndicadoresPagamento) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            }
                            break;
                        case "Consulta de Funcionário para Relatório":
                            if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioClientesFornecedoresFuncionarios.class) {
                                if (((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.getText().trim().equals("")) {
                                    ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.getText();
                                    ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            }
                            break;
                        case "Consulta de Cidade para Relatório":
                            if (((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldCidade.getText().trim().equals("")) {
                                ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldCidade.setText(listaCodigos);
                                guardaCodigos.clear();
                                listaCodigos = "";
                            } else {
                                String guardaValoresCodigo = ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldCidade.getText();
                                ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldCidade.setText("");
                                guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldCidade.setText(ordenaCodigos(guardaValoresCodigo));
                            }
                            break;
                        case "Consulta de Estado para Relatório":
                            if (((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldEstado.getText().trim().equals("")) {
                                ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldEstado.setText(listaCodigos);
                                guardaCodigos.clear();
                                listaCodigos = "";
                            } else {
                                String guardaValoresCodigo = ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldEstado.getText();
                                ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldEstado.setText("");
                                guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldEstado.setText(ordenaCodigos(guardaValoresCodigo));
                            }
                            break;
                        case "Consulta de Produto para Relatório":
                            if (((TelaRelatorioProduto) chamador).jTextFieldCodigo.getText().trim().equals("")) {
                                ((TelaRelatorioProduto) chamador).jTextFieldCodigo.setText(listaCodigos);
                                guardaCodigos.clear();
                                listaCodigos = "";
                            } else {
                                String guardaValoresCodigo = ((TelaRelatorioProduto) chamador).jTextFieldCodigo.getText();
                                ((TelaRelatorioProduto) chamador).jTextFieldCodigo.setText("");
                                guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                ((TelaRelatorioProduto) chamador).jTextFieldCodigo.setText(ordenaCodigos(guardaValoresCodigo));
                            }
                            break;
                        case "Consulta de Fabricante para Relatório":
                            if (((TelaRelatorioProduto) chamador).jTextFieldFabricante.getText().trim().equals("")) {
                                ((TelaRelatorioProduto) chamador).jTextFieldFabricante.setText(listaCodigos);
                                guardaCodigos.clear();
                                listaCodigos = "";
                            } else {
                                String guardaValoresCodigo = ((TelaRelatorioProduto) chamador).jTextFieldFabricante.getText();
                                ((TelaRelatorioProduto) chamador).jTextFieldFabricante.setText("");
                                guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                ((TelaRelatorioProduto) chamador).jTextFieldFabricante.setText(ordenaCodigos(guardaValoresCodigo));
                            }
                            break;
                        case "Consulta de Categoria para Relatório":
                            if (((TelaRelatorioProduto) chamador).jTextFieldCategoria.getText().trim().equals("")) {
                                ((TelaRelatorioProduto) chamador).jTextFieldCategoria.setText(listaCodigos);
                                guardaCodigos.clear();
                                listaCodigos = "";
                            } else {
                                String guardaValoresCodigo = ((TelaRelatorioProduto) chamador).jTextFieldCategoria.getText();
                                ((TelaRelatorioProduto) chamador).jTextFieldCategoria.setText("");
                                guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                ((TelaRelatorioProduto) chamador).jTextFieldCategoria.setText(ordenaCodigos(guardaValoresCodigo));
                            }
                            break;
                        case "Consulta de Conta a Pagar para Relatório":
                            if (((TelaRelatorioContasPagar) chamador).jTextFieldCodigo.getText().trim().equals("")) {
                                ((TelaRelatorioContasPagar) chamador).jTextFieldCodigo.setText(listaCodigos);
                                guardaCodigos.clear();
                                listaCodigos = "";
                            } else {
                                String guardaValoresCodigo = ((TelaRelatorioContasPagar) chamador).jTextFieldCodigo.getText();
                                ((TelaRelatorioContasPagar) chamador).jTextFieldCodigo.setText("");
                                guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                ((TelaRelatorioContasPagar) chamador).jTextFieldCodigo.setText(ordenaCodigos(guardaValoresCodigo));
                            }
                            break;
                        case "Consulta de Conta a Receber para Relatório":
                            if (((TelaRelatorioContasReceber) chamador).jTextFieldCodigo.getText().trim().equals("")) {
                                ((TelaRelatorioContasReceber) chamador).jTextFieldCodigo.setText(listaCodigos);
                                guardaCodigos.clear();
                                listaCodigos = "";
                            } else {
                                String guardaValoresCodigo = ((TelaRelatorioContasReceber) chamador).jTextFieldCodigo.getText();
                                ((TelaRelatorioContasReceber) chamador).jTextFieldCodigo.setText("");
                                guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                ((TelaRelatorioContasReceber) chamador).jTextFieldCodigo.setText(ordenaCodigos(guardaValoresCodigo));
                            }
                            break;
                        case "Consulta de Venda para Relatório":
                            if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioVendas.class) {
                                if (((TelaRelatorioVendas) chamador).jTextFieldCodigo.getText().trim().equals("")) {
                                    ((TelaRelatorioVendas) chamador).jTextFieldCodigo.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioVendas) chamador).jTextFieldCodigo.getText();
                                    ((TelaRelatorioVendas) chamador).jTextFieldCodigo.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioVendas) chamador).jTextFieldCodigo.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            }
                            break;
                        case "Consulta de Compra para Relatório":
                            if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioCompras.class) {
                                if (((TelaRelatorioCompras) chamador).jTextFieldCodigo.getText().trim().equals("")) {
                                    ((TelaRelatorioCompras) chamador).jTextFieldCodigo.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioCompras) chamador).jTextFieldCodigo.getText();
                                    ((TelaRelatorioCompras) chamador).jTextFieldCodigo.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioCompras) chamador).jTextFieldCodigo.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            }
                            break;
                        case "Consulta de Pagamentos para Relatório":
                            System.out.println("COLEI AKI MEN");
                            System.out.println("TELA CAHMADORA.GETCLASS() = " + parametrosConsulta.getTelaChamadora().getClass());
                            if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioPagamentosRecebimentos.class) {
                                if (((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldCodigo.getText().trim().equals("")) {
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldCodigo.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldCodigo.getText();
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldCodigo.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldCodigo.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            }
                            break;
                        case "Consulta de Recebimentos para Relatório":
                            if (parametrosConsulta.getTelaChamadora().getClass() == TelaRelatorioPagamentosRecebimentos.class) {
                                if (((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldCodigo.getText().trim().equals("")) {
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldCodigo.setText(listaCodigos);
                                    guardaCodigos.clear();
                                    listaCodigos = "";
                                } else {
                                    String guardaValoresCodigo = ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldCodigo.getText();
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldCodigo.setText("");
                                    guardaValoresCodigo = guardaValoresCodigo + "," + listaCodigos;
                                    ((TelaRelatorioPagamentosRecebimentos) chamador).jTextFieldCodigo.setText(ordenaCodigos(guardaValoresCodigo));
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    public String ordenaCodigos(String guardaValoresCodigo) {
        //Transforma a string guardaValoresCodigo em um ArrayList de String para poder inserir no HashSet
        List<String> listaCodigosString = new ArrayList<>(Arrays.asList(guardaValoresCodigo.split(",")));
        //Transforma o ArrayList de String em ArrayList de Integer para poder ordenar por ordem numérica no HashSet
        List<Integer> listaCodigosInteger = new ArrayList<>(listaCodigosString.size());
        listaCodigosString.forEach((myInt) -> {
            listaCodigosInteger.add(Integer.valueOf(myInt));
        });
        Set<Integer> hs = new HashSet<>(listaCodigosInteger);
        //Adiciona o ArrayList de Integer criado a cima no HashSet pois ele não permite valores duplicados.
        hs.addAll(listaCodigosInteger);
        //Limpa o ArrayList de Integer listaCodigosInteger
        listaCodigosInteger.clear();
        //Adiciona o HashSet hs no ArrayList de Integer listaCodigosInteger com os valores já tratados
        listaCodigosInteger.addAll(hs);
        //Transforma o ArrayList de Integer em ArrayList de String novamente para poder ser passado para o jTextField
        List<String> novaListaCodigosString = new ArrayList<>(listaCodigosInteger.size());
        listaCodigosInteger.forEach((myInt) -> {
            novaListaCodigosString.add(String.valueOf(myInt));
        });
        //Por fim, converte o novo ArrayList de String em uma String separada por vírgula para poder ser utilizada no jTextField
        String novaListaSemDuplicatas = String.join(",", novaListaCodigosString);
        return novaListaSemDuplicatas;
    }

    public void enviaValoresSelecionados(String tituloConsulta, JInternalFrame telaChamadora, JTextField campo) {
        ArrayList<String> guardaCodigos = new ArrayList<>();
        int[] selectedRowIndexes = tabela.getSelectedRows();
        for (int i = 0; i < selectedRowIndexes.length; i++) {
            if (tabela.getSelectedRowCount() != 0) {
                guardaCodigos.add((String) dtm.getValueAt(selectedRowIndexes[i], 0));
            }
        }
        String listaCodigos = String.join(", ", guardaCodigos);
        if (((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.getText().equals("")) {
            ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText(listaCodigos);
            System.out.println("\n\nCLIENTES1: " + listaCodigos);
            guardaCodigos.clear();
            listaCodigos = "";
        } else {
            String guardaValoresCodigo = ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.getText();
            ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText("");
            guardaValoresCodigo = guardaValoresCodigo + ", " + listaCodigos;
            ((TelaRelatorioClientesFornecedoresFuncionarios) chamador).jTextFieldPessoa.setText(guardaValoresCodigo);
            System.out.println("\n\nCLIENTES2: " + listaCodigos);
        }
    }

    public void centralizaTela() {
        Dimension tamanhoTela = getSize();
        Dimension tamanhoJDesktopPane = TelaSistema.jdp.getSize();
        int x = (tamanhoJDesktopPane.width - tamanhoTela.width) / 2;
        int y = (tamanhoJDesktopPane.height - tamanhoTela.height) / 2;
        setLocation(x, y);
    }

    @Override
    public void mouseClicked(MouseEvent me) {

        if (me.getClickCount() == 2) {
            int linhaSelecionada = tabela.getSelectedRow();
            String codigo = (String) dtm.getValueAt(linhaSelecionada, 0);
            int pk = Integer.parseInt(codigo);
            chamador = parametrosConsulta.getChamador();
            if (chamador instanceof TelaCadastro) {
                clicouConsultar = true;
                ((TelaCadastro) chamador).consultarBD(pk);
                dispose();
                TelaSistema.jdp.remove(this);
            } else if (chamador instanceof MeuCampoBuscar) {
                ((MeuCampoBuscar) chamador).setValorDuploClique(pk);
                dispose();
                TelaSistema.jdp.remove(this);
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent me
    ) {
    }

    @Override
    public void mouseReleased(MouseEvent me
    ) {
    }

    @Override
    public void mouseEntered(MouseEvent me
    ) {
    }

    @Override
    public void mouseExited(MouseEvent me
    ) {
    }

    private void preencherComboBox() {
        jcbPesquisar.addItem("Selecione...");
        jcbOrdenar.addItem("Selecione...");
        for (int i = 0; i < parametrosConsulta.getFiltros().length; i++) {
            jcbPesquisar.addItem(parametrosConsulta.getFiltros()[i].nomeFiltro);
            jcbOrdenar.addItem(parametrosConsulta.getFiltros()[i].nomeFiltro);
        }
    }

    private void preencherDadosPesquisar() {
        String tempSql = parametrosConsulta.getSql();
        if (jcbPesquisar.getSelectedIndex() > 0) {
            tempSql = "SELECT * FROM (" + parametrosConsulta.getSql() + ") where ";
            Class tipo = parametrosConsulta.getFiltros()[jcbPesquisar.getSelectedIndex() - 1].tipo;
            if (tipo == Date.class) {
                tempSql = tempSql
                        + "       CAST("
                        + "             LPAD(EXTRACT(DAY FROM data), 2, '0') || '/' || "
                        + "             LPAD(EXTRACT(MONTH FROM data), 2, '0') || '/' || "
                        + "             LPAD(EXTRACT(YEAR FROM data), 4, '0') "
                        + "             AS VARCHAR(10)) "
                        + "       LIKE '%";
                tempSql = tempSql + textoPesquisar.getText().toUpperCase()
                        + "%'";
            } else if (tipo == String.class) {
                tempSql = tempSql + "UPPER("
                        + parametrosConsulta.getFiltros()[jcbPesquisar.getSelectedIndex() - 1].nomeAtributo
                        + ") LIKE '%";
                tempSql = tempSql + textoPesquisar.getText().toUpperCase()
                        + "%'";
            } else {
                JOptionPane.showMessageDialog(null, "Não existe tipo definido.");
            }
            guardaSql = tempSql;
            System.out.println("" + tempSql);
        }
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }

        if (checkBoxCliente.isSelected()) {
            tempSql = "SELECT * FROM (" + tempSql + ") WHERE ECLIENTE = 'S'";
            System.out.println("" + tempSql);
        }
        if (checkBoxFornecedor.isSelected()) {
            tempSql = "SELECT * FROM (" + tempSql + ") WHERE EFORNECEDOR = 'S'";
            System.out.println("" + tempSql);
        }
        if (checkBoxFuncionario.isSelected()) {
            tempSql = "SELECT * FROM (" + tempSql + ") WHERE EFUNCIONARIO = 'S'";
            System.out.println("" + tempSql);
        }
        if (comboBoxContaGerada.getSelectedItem() == "Sim") {
            tempSql = "SELECT * FROM (" + tempSql + ") WHERE CPGERADA = 'S'";
            System.out.println("" + tempSql);
        }
        if (!checkBoxCliente.isSelected() && !checkBoxFornecedor.isSelected() && !checkBoxFuncionario.isSelected()) {
            while (dtm.getRowCount() > 0) {
                dtm.removeRow(0);
            }
        }

        guardaSql = tempSql;

        guardaSql2 = tempSql;

        if (jcbOrdenar.getSelectedIndex() > 0) {
            Class tipo = parametrosConsulta.getFiltros()[jcbOrdenar.getSelectedIndex() - 1].tipo;
            tempSql = guardaSql + " order by ";
            tempSql = tempSql + parametrosConsulta.getFiltros()[jcbOrdenar.getSelectedIndex() - 1].nomeAtributo;
            System.out.println("" + tempSql);
        }

        guardaSql2 = tempSql;
//        List<Object[]> dados = Conexao.pesquisar(tempSql);
//        for (int i = 0; i < dados.size(); i++) {
//            dtm.addRow(dados.get(i));
//        }
        System.out.println("" + tempSql);
        gerarPaginas(tempSql);
    }

    private void preencherDadosListar() {
        String tempSql = parametrosConsulta.getSql();
        guardaSql = tempSql;
        if (comboBoxContaGerada.getSelectedItem() == "Sim") {
            tempSql = "SELECT * FROM (" + tempSql + ") WHERE CPGERADA = 'S'";
            System.out.println("" + tempSql);
            tempSql = tempSql + " ORDER BY ID";
        } else {
            tempSql = guardaSql + " ORDER BY ID";
        }

        guardaSql2 = tempSql;

        System.out.println("tempSQL no preencerDadosListar = " + tempSql);
//        while (dtm.getRowCount() > 0) {
//            dtm.removeRow(0);
//        }
//        List<Object[]> dados = Conexao.pesquisar(tempSql);
//        for (int i = 0; i < dados.size(); i++) {
//            dtm.addRow(dados.get(i));
//        }
        System.out.println("" + tempSql);
        gerarPaginas(tempSql);
    }

    private void preencherDadosData() {
        String tempSql = parametrosConsulta.getSql();
        tempSql = "SELECT * FROM (" + parametrosConsulta.getSql() + ") where ";
        tempSql = tempSql + "DATA BETWEEN '";
        String dateInicial = new SimpleDateFormat("dd/MM/yyyy").format(dataInicial.getDate());
        String replaceDataInicial = dateInicial.trim().replace("/", ".");
        tempSql = tempSql + replaceDataInicial + "' AND '";
        String dateFinal = new SimpleDateFormat("dd/MM/yyyy").format(dataFinal.getDate());
        String replaceDataFinal = dateFinal.trim().replace("/", ".");
        tempSql = tempSql + replaceDataFinal + "'";

        System.out.println("TESTE DATA" + tempSql);
        guardaSqlData = tempSql;
        if (comboBoxContaGerada.getSelectedItem() == "Sim") {
            tempSql = "SELECT * FROM (" + tempSql + ") WHERE CPGERADA = 'S'";
            System.out.println("" + tempSql);
            tempSql = tempSql;
        } else {
            tempSql = guardaSqlData;
        }

        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        List<Object[]> dados = Conexao.pesquisar(tempSql);
        System.out.println("DADOS = " + dados);
        for (int i = 0; i < dados.size(); i++) {
            dtm.addRow(dados.get(i));
        }
        System.out.println("" + tempSql);
        gerarPaginas(tempSql);
    }

}
