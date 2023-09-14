package tabela;

import bd.Conexao;
import componentes.MeuComponente;
import dao.DaoCaixa;
import dao.DaoMovimentoCaixa;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import pojo.Caixa;
import pojo.MovimentoCaixa;
import renderizador.InteiroRender;
import renderizador.MonetarioRender;
import renderizador.RenderizadorData;
import renderizador.RenderizadorHora;
import renderizador.RenderizadorInteiroCenter;
import renderizador.RenderizadorStatus;
import renderizador.RenderizadorTexto;

public class TabelaMovimentoCaixa extends JPanel implements MeuComponente {

    Preferences prefs = Preferences.userNodeForPackage(TabelaMovimentoCaixa.class);

    final String USER_PREF_TAMANHO_PAGINA = "tamanhoPagina";

    Caixa caixa = new Caixa();
    DaoCaixa daoCaixa = new DaoCaixa(caixa);
    public MovimentoCaixa movimentoCaixa = new MovimentoCaixa();

    private int tamanhoPagina = 10;

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
    String[] valoresLimitePaginas = {"10", "20", "30"};
    public JComboBox limiteRegistros = new JComboBox(valoresLimitePaginas);
    private JPanel painelPaginacao = new JPanel();
    private JPanel painelConteudo = new JPanel();

    private JScrollPane jsp = new JScrollPane();
    private TableModelMovimentoCaixa tmp = new TableModelMovimentoCaixa();
    private JTable tabela = new JTable(tmp) {
        @Override
        public Component prepareRenderer(TableCellRenderer renderer,
                int linha, int coluna) {
            Component c = super.prepareRenderer(renderer, linha, coluna);
            if (linha % 2 == 0) {
                c.setBackground(Color.LIGHT_GRAY);
            } else {
                c.setBackground(getBackground());
            }
            if (isCellSelected(linha, coluna)) {
                c.setBackground(Color.decode("#4286f4"));
            }
            if (tabela.getValueAt(linha, 2).equals("D")) {//verifica se é Débito, e então pinta as letras de verde, indicando uma entrada
                c.setForeground(Color.decode("#1f870a"));
                //c.setBackground((Color.decode("#91dc5a")));
            } else { //se não for Débito  Dé Crédito C, e então pinta as letras de vermelho, indicando uma saída
                c.setForeground(Color.decode("#D80027"));
                //c.setBackground(Color.decode("#D80027"));
            }
            return c;
        }
    };

    public TabelaMovimentoCaixa() {
        painelPaginacao.setLayout(new FlowLayout());
        painelConteudo.setLayout(new BorderLayout());

        jsp.setViewportView(tabela);
        jsp.setPreferredSize(new Dimension(820, 240));

        painelConteudo.add(jsp, BorderLayout.NORTH);
        painelPaginacao.add(jbPrimeiraPagina);
        painelPaginacao.add(jbAnteriorPagina);
        painelPaginacao.add(labelPaginaAtual);
        painelPaginacao.add(jbProximaPagina);
        painelPaginacao.add(jbUltimaPagina);
        painelPaginacao.add(labelTamanhoPagina);
        painelPaginacao.add(limiteRegistros);
        painelConteudo.add(painelPaginacao, BorderLayout.SOUTH);
        add(painelConteudo);
        adicionaListenersBotoes();

        //define o numero de itens exibidos por página de acordo com as prefs do usuário
        String teste = prefs.get(USER_PREF_TAMANHO_PAGINA, "value");
        limiteRegistros.setSelectedItem(teste);
        
        limiteRegistros.setEnabled(false);
        jbAnteriorPagina.setEnabled(false);
        jbProximaPagina.setEnabled(false);
        jbUltimaPagina.setEnabled(false);
        jbPrimeiraPagina.setEnabled(false);

        tabela.getColumnModel().getColumn(0).setMinWidth(60);
        tabela.getColumnModel().getColumn(0).setMaxWidth(60);
        tabela.getColumnModel().getColumn(0).setCellRenderer(new InteiroRender());
        tabela.getColumnModel().getColumn(1).setMinWidth(190);
        tabela.getColumnModel().getColumn(1).setMaxWidth(190);
        tabela.getColumnModel().getColumn(1).setCellRenderer(new RenderizadorTexto());
        tabela.getColumnModel().getColumn(2).setMinWidth(50);
        tabela.getColumnModel().getColumn(2).setMaxWidth(50);
        tabela.getColumnModel().getColumn(2).setCellRenderer(new RenderizadorStatus());
        tabela.getColumnModel().getColumn(3).setCellRenderer(new MonetarioRender());
        tabela.getColumnModel().getColumn(4).setMinWidth(80);
        tabela.getColumnModel().getColumn(4).setMaxWidth(80);
        tabela.getColumnModel().getColumn(4).setCellRenderer(new RenderizadorData());
        tabela.getColumnModel().getColumn(5).setMinWidth(50);
        tabela.getColumnModel().getColumn(5).setMaxWidth(50);
        tabela.getColumnModel().getColumn(5).setCellRenderer(new RenderizadorHora());
        tabela.getColumnModel().getColumn(6).setCellRenderer(new RenderizadorInteiroCenter());
        tabela.getColumnModel().getColumn(7).setCellRenderer(new RenderizadorInteiroCenter());
        
        tabela.setFocusable(false);
        tabela.setRowSelectionAllowed(false);

        System.out.println("\n\n\nVALOR DA PAGINA ATUAL: " + numeroPaginaAtual);
        System.out.println("\n\n\nNUMERO DE PÁGINAS: " + numeroPaginaInt);
    }

    @Override
    public boolean eObrigatorio() {
        return false;
    }

    @Override
    public boolean eValido() {
        return true;
    }

    @Override
    public boolean eVazio() {
        return tmp.getRowCount() == 0;
    }

    @Override
    public void limpar() {
        tmp.limparDados();
    }

    @Override
    public void habilitar(boolean status) {
        tabela.setEnabled(status);
    }

    public Object getValor() {
        return tmp.getDados();
    }

    public void setValor(Object valor) {
        tmp.setDados((List<MovimentoCaixa>) valor);
    }

    @Override
    public String getDica() {
        return "Movimento(s)";
    }

    public TableModelMovimentoCaixa getTableModel() {
        return tmp;
    }

    public int getLinhaSelecionada() {
        return tabela.getSelectedRow();
    }

    public JTable getTabela() {
        return tabela;
    }

    private void avancarPagina() {
        numeroPaginaAtual++;
        labelPaginaAtual.setText(String.valueOf(numeroPaginaAtual) + "/" + numeroPaginaInt);
        System.out.println("\n\n\nVALOR DA PAGINA ATUAL: " + numeroPaginaAtual);
        System.out.println("\n\n\nNUMERO DE PÁGINAS: " + numeroPaginaInt);

        for (int i = 0; i < numeroPaginaInt; i++) {
            if (numeroPaginaAtual == i + 1) { // i + 1 pois para a página atual ser igual ao i ele deve estar somado com 1, sendo que o index do arrayList começa em zero.
                DaoMovimentoCaixa.consultarItensPaginados(caixa, (String) guardaSqlPaginas.get(numeroPaginaAtual - 1));
                System.out.println("\nVALOR GUARDA SQL " + i + " = " + (String) guardaSqlPaginas.get(numeroPaginaAtual - 1));
                List<Integer> controleItensPaginados = new ArrayList();
                for (int x = 0; x < caixa.getItensCaixa().size(); x++) {
                    controleItensPaginados.add(caixa.getItensCaixa().get(x).getId());
                }
                System.out.println("\nITENS PAGINADOS = " + controleItensPaginados);
                tmp.setDados(caixa.getItensCaixa());
            }
        }
    }

    private void voltarPagina() {
        numeroPaginaAtual--;
        labelPaginaAtual.setText(String.valueOf(numeroPaginaAtual) + "/" + numeroPaginaInt);
        System.out.println("\n\n\nVALOR DA PAGINA ATUAL: " + numeroPaginaAtual);
        System.out.println("\n\n\nNUMERO DE PÁGINAS: " + numeroPaginaInt);
        for (int i = 0; i < numeroPaginaInt; i++) {
            if (numeroPaginaAtual == i + 1) {
                DaoMovimentoCaixa.consultarItensPaginados(caixa, (String) guardaSqlPaginas.get(numeroPaginaAtual - 1));
                System.out.println("VALOR GUARDA SQL " + i + " = " + (String) guardaSqlPaginas.get(numeroPaginaAtual - 1));
                List<Integer> controleItensPaginados = new ArrayList();
                for (int x = 0; x < caixa.getItensCaixa().size(); x++) {
                    controleItensPaginados.add(caixa.getItensCaixa().get(x).getId());
                }
                System.out.println("ITENS PAGINADOS = " + controleItensPaginados);
                tmp.setDados(caixa.getItensCaixa());
            }
        }
    }

    public void gerarPaginas(int idCaixa) { //método gera as paginas necessárias na tabela, além dos sql's que serão utilizados em cada uma delas.
        if (prefs.get(USER_PREF_TAMANHO_PAGINA, "value").equals("value")) { //se a preferencia do usuário ainda não existir, define 10 como padrão para o tamanho das páginas
            tamanhoPagina = 10;
        } else { //se não, pega o valor da preferencia do usuário
            tamanhoPagina = Integer.parseInt(prefs.get(USER_PREF_TAMANHO_PAGINA, "value"));
        }
        System.out.println("VALOR DO CAIXA = " + idCaixa);
        System.out.println("VALOR DO COMBOBOX = " + limiteRegistros.getSelectedItem());
        numeroPaginaFloat = numeroRegistros(idCaixa) / (float) tamanhoPagina;
        numeroPaginaFloat = (float) Math.ceil(numeroPaginaFloat);
        numeroPaginaInt = (int) numeroPaginaFloat;
        System.out.println("PAGINAS: " + numeroPaginaInt);

        //inicio do procedimento que gera os SQLS com os valores dinamicamente
        int valorSkip = 0;
        for (int i = 0; i < numeroPaginaInt; i++) {
            String sqlPaginas = "SELECT FIRST ";
            sqlPaginas = sqlPaginas + tamanhoPagina + " SKIP ";
            sqlPaginas = sqlPaginas + valorSkip + " ID, DESCRICAO, DEBITOCREDITO, VALOR, DATA, HORATRANSACAO, IDPAGAMENTO, IDRECEBIMENTO FROM MOVIMENTOCAIXA WHERE IDCAIXA = ?";
            valorSkip += Integer.valueOf((String) limiteRegistros.getSelectedItem());
            guardaSqlPaginas.add(i, sqlPaginas);

//            System.out.println("\nsqlPaginas " + i + ": " + sqlPaginas);
//            System.out.println("valorSkip " + i + ": " + valorSkip);
        }
        System.out.println("\nPRINTANDO OS SQLS \n" + guardaSqlPaginas);
        //fim do procedimento que gera os SQLS com os valores dinamicamente 

        ultimaPagina();

        caixa.setId(idCaixa);
        verificaBotoesLimitePagina();
        System.out.println("\nVALOR DE REGISTROS = " + numeroRegistros(idCaixa));
    }

    public void ultimaPagina() { //método que vai sempre para a última página da tabela quando realizada uma consulta
        numeroPaginaAtual = numeroPaginaInt;
        labelPaginaAtual.setText(String.valueOf(numeroPaginaAtual) + "/" + numeroPaginaInt);
        DaoMovimentoCaixa.consultarItensPaginados(caixa, (String) guardaSqlPaginas.get(numeroPaginaInt - 1));
        List<Integer> controleItensPaginados = new ArrayList();
        for (int x = 0; x < caixa.getItensCaixa().size(); x++) {
            controleItensPaginados.add(caixa.getItensCaixa().get(x).getId());
        }
        System.out.println("\nITENS PAGINADOS = " + controleItensPaginados);
        tmp.setDados(caixa.getItensCaixa());
    }    
    
    public void primeiraPagina() { //método que vai sempre para a última página da tabela quando realizada uma consulta
        numeroPaginaAtual = numeroPaginaInt - numeroPaginaInt + 1;
        labelPaginaAtual.setText(String.valueOf(numeroPaginaAtual) + "/" + numeroPaginaInt);
        DaoMovimentoCaixa.consultarItensPaginados(caixa, (String) guardaSqlPaginas.get(numeroPaginaAtual - 1));
        List<Integer> controleItensPaginados = new ArrayList();
        for (int x = 0; x < caixa.getItensCaixa().size(); x++) {
            controleItensPaginados.add(caixa.getItensCaixa().get(x).getId());
        }
        System.out.println("\nITENS PAGINADOS = " + controleItensPaginados);
        tmp.setDados(caixa.getItensCaixa());
    }

    private int numeroRegistros(int idCaixa) {
        try {
            int numeroRegistros = 0;
            String sqlRegistros = "SELECT COUNT(ID) FROM MOVIMENTOCAIXA WHERE IDCAIXA = ?";
            caixa.setId(idCaixa);
            PreparedStatement ps = Conexao.getConexao().prepareStatement(sqlRegistros);
            ps.setInt(1, caixa.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                numeroRegistros = rs.getInt(1);
            }
            return numeroRegistros;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível consultar os movimentos paginados do caixa.");
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
                System.out.println("Estado do item: " + ie.getStateChange());
                if (caixa.getId() != 0) {
                    if (limiteRegistros.getSelectedItem() == "10") {
                        tamanhoPagina = 10;
                        prefs.putInt(USER_PREF_TAMANHO_PAGINA, tamanhoPagina);
                        gerarPaginas(caixa.getId());
                    }
                    if (limiteRegistros.getSelectedItem() == "20") {
                        tamanhoPagina = 20;
                        prefs.putInt(USER_PREF_TAMANHO_PAGINA, tamanhoPagina);
                        gerarPaginas(caixa.getId());
                    }
                    if (limiteRegistros.getSelectedItem() == "30") {
                        tamanhoPagina = 30;
                        prefs.putInt(USER_PREF_TAMANHO_PAGINA, tamanhoPagina);
                        gerarPaginas(caixa.getId());
                    }
                }
            }
        });
    }
}
