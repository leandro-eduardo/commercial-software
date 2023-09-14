package telas;

import bd.Conexao;
import componentes.MeuCampoBuscar;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import pojo.ParametrosConsulta;
import util.RestricaoCaracteres;
import util.RestricaoCaracteresPesquisar;
import util.RestricaoCaracteresPesquisarData;

public class TelaConsultaPagamento extends JInternalFrame implements MouseListener {

    public static Object chamador;
    public String guardaSql;
    public ParametrosConsulta parametrosConsulta;
    private JPanel painel = new JPanel();
    private JPanel painelPesquisar = new JPanel();
    private JPanel painelOrdenar = new JPanel();
    private JPanel painelBaixo = new JPanel();
    public JLabel labelValorContaPagar = new JLabel("");
    public JLabel labelSomaContas = new JLabel("");
    private JComboBox jcbPesquisar = new JComboBox();
    private JComboBox jcbOrdenar = new JComboBox();
    private JTextField textoPesquisar = new JTextField(20);
    private JButton botaoListar = new JButton("Listar");
    private JButton botaoLimpar = new JButton("Limpar");
    private JTable tabela;
    private JScrollPane jsp;
    private DefaultTableModel dtm;
    int somaLinhas = 0;
    
    JSeparator separator = new JSeparator(JSeparator.VERTICAL);     
    JSeparator separator1 = new JSeparator(JSeparator.VERTICAL);

    public TelaConsultaPagamento(ParametrosConsulta parametrosConsulta) {
        super(parametrosConsulta.getTituloConsulta(), true, true, true, true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/consultar.png")));
        this.parametrosConsulta = parametrosConsulta;
        TelaSistema.jdp.add(this);
        TelaSistema.jdp.moveToFront(this);
        TelaSistema.jdp.setSelectedFrame(this);
        dtm = new DefaultTableModel(new Object[][]{},
                parametrosConsulta.getColunas());
        tabela = new JTable(dtm) {
            public boolean isCellEditable(int i, int i1) {
                return false;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                int somaLinhas = 0;
                if (rowIndex % 2 == 0) {
                    c.setBackground(Color.LIGHT_GRAY);
                } else {
                    c.setBackground(Color.GRAY);
                }
                //procedimento que converte a data obtida da tabela e compara com a data atual do sistema
                SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
                String aux = (String) tabela.getValueAt(rowIndex, 4);
                String replace = aux.replace("-", "/");
                Date dataVencimento = null;
                try {
                    dataVencimento = formato.parse(replace);
                } catch (ParseException ex) {
                    Logger.getLogger(TelaConsultaPagamento.class.getName()).log(Level.SEVERE, null, ex);
                }
		Date dataAtual = null;
		java.util.Date dataHoje = new java.util.Date();
		String aux1 = formato.format(dataHoje);
                try {
                    dataAtual = formato.parse(aux1);
                } catch (ParseException ex) {
                    Logger.getLogger(TelaConsultaPagamento.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (dataVencimento.before(dataAtual)) { //verfifica se a data é anterior a data atual, e então pinta as letras de vermelho indicando que está vencida
                    c.setForeground(Color.decode("#d60404"));
                    //c.setBackground(Color.decode("#fc3535"));
                } else if (dataVencimento.equals(dataAtual)) { //verfifica se a data é igual a data atual, e então pinta as letras de amarelo indicando que vence no dia em questão
                    //c.setBackground(Color.decode("#fce641"));
                    c.setForeground(Color.decode("#ffea63"));
                } else { //se não se encaixar em nenhuma condicão acima, pinta de preto para padronizar
                    c.setForeground(Color.black);
                }
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    somaLinhas += 1;
                }
                labelSomaContas.setText("Número de contas: " + somaLinhas);
                somaValorPagar();
                if (isCellSelected(rowIndex, vColIndex)) {
                    c.setBackground(new Color(66, 134, 244));
                }
                return c;
            }
        };
        jsp = new JScrollPane(tabela);
        painel.setLayout(new FlowLayout());
        painelPesquisar.setLayout(new FlowLayout());
        painelBaixo.setLayout(new FlowLayout(FlowLayout.RIGHT));
        painelBaixo.setPreferredSize(new Dimension(getWidth(), 25));
        JLabel labelOrdenar = new JLabel("Ordenar: ");

         JPanel espacador3 = new JPanel();
        espacador3.setPreferredSize(new Dimension(10, 10));
        painelBaixo.add(espacador3);
        getContentPane().add("West", espacador3);
        painel.add(jcbPesquisar);
        jcbPesquisar.setToolTipText("Selecione um filtro e digite o que deseja para iniciar uma pesquisa.");
        painel.add(textoPesquisar);
        painel.add(botaoListar);
        painel.add(botaoLimpar);
        botaoListar.setToolTipText("Listar todas as contas ordenadas pela data de vencimento.");

        painelOrdenar.add(labelOrdenar);
        painelOrdenar.add(jcbOrdenar);
        painel.add(painelOrdenar);
        textoPesquisar.setEnabled(false);

        getContentPane().add("Center", painelPesquisar);
        getContentPane().add("North", painel);
        getContentPane().add("Center", jsp);
        JPanel espacador1 = new JPanel();
        espacador1.setPreferredSize(new Dimension(10, 10));
        getContentPane().add("East", espacador1);
        getContentPane().add("South", painelBaixo);
  
        separator.setPreferredSize(new Dimension(3, 20));
        separator1.setPreferredSize(new Dimension(3, 20));
       
        painelBaixo.add(separator);
        painelBaixo.add(labelSomaContas);
        painelBaixo.add(separator1);       
        painelBaixo.add(labelValorContaPagar);    
        JPanel espacador2 = new JPanel();
        espacador2.setPreferredSize(new Dimension(10, 10));
        painelBaixo.add(espacador2);
        separator.setVisible(false);
        separator1.setVisible(false);
        jsp.setPreferredSize(new Dimension(800, 400));
        if (dtm.getRowCount() <= 0) {
            jcbOrdenar.setEnabled(false);
        }
        setSize(1000, 520);
        setVisible(true);
        preencherComboBox();
        centralizaTela();
        adicionaListeners();
        aplicaRender();
        tabela.addMouseListener(this);
    }

    private void aplicaRender() {
        TableColumnModel tableModel = tabela.getColumnModel();
        tableModel.getColumn(0).setMaxWidth(60);  //tamanho da coluna 0 (geralmente onde está o campo código) em todas as telas de consulta.
        tableModel.getColumn(1).setMinWidth(380); //tamanho da coluna 1 (geralmente onde está os maiores campos) em todas as telas de consulta.
        tableModel.getColumn(1).setMaxWidth(380); //tamanho da coluna 1 (geralmente onde está os maiores campos) em todas as telas de consulta.
        tableModel.getColumn(2).setMaxWidth(75);
        tableModel.getColumn(5).setMaxWidth(60);
        tableModel.getColumn(7).setMinWidth(100);
        for (int i = 0; i < dtm.getColumnCount(); i++) {
            if (parametrosConsulta.getRenderizadores()[i] != null) {
                tableModel.getColumn(i).setCellRenderer(parametrosConsulta.getRenderizadores()[i]);
            }
        }
    }

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
                                separator.setVisible(false);
                                separator1.setVisible(false);
                                labelSomaContas.setText("");
                                labelValorContaPagar.setText("");
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
                botaoListar.setEnabled(false);
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
            }
        });
        jcbPesquisar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (jcbPesquisar.getSelectedIndex() == 0) {
                    textoPesquisar.setEnabled(false);
                } else {
                    textoPesquisar.setEnabled(true);
                }
            }
        });
        botaoListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                jcbPesquisar.setSelectedIndex(0);
                preencherDadosListar();
                if (dtm.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Não existem dados cadastrados.");
                }
                if (dtm.getRowCount() > 0) { 
                    separator.setVisible(true);
                    separator1.setVisible(true);
                    jcbOrdenar.setEnabled(true);
                }
            }
        });

        botaoLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                while (dtm.getRowCount() > 0) {
                    dtm.removeRow(0);
                }
                jcbOrdenar.setEnabled(false);
                jcbPesquisar.setSelectedIndex(0);
                jcbOrdenar.setSelectedIndex(0);
                textoPesquisar.setText("");
                separator.setVisible(false);
                separator1.setVisible(false);
                labelSomaContas.setText("");
                labelValorContaPagar.setText("");
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
                ((TelaCadastro) chamador).consultarBD(pk);
            } else if (chamador instanceof MeuCampoBuscar) {
                ((MeuCampoBuscar) chamador).setValorDuploClique(pk);
            }
            dispose();
            TelaSistema.jdp.remove(this);
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
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
            if (tipo == Date.class && jcbPesquisar.getSelectedIndex() == 5) {
                tempSql = tempSql +
                        "CAST(" +
                        "LPAD(EXTRACT(DAY FROM VENCIMENTO), 2, '0') || '/' || " +
                        "LPAD(EXTRACT(MONTH FROM VENCIMENTO), 2, '0') || '/' || " +
                        "LPAD(EXTRACT(YEAR FROM VENCIMENTO), 4, '0') " +
                        "AS VARCHAR(10)) " +
                        "LIKE '%";
                tempSql = tempSql + textoPesquisar.getText().toUpperCase()
                        + "%'";
            } else if (tipo == Date.class && jcbPesquisar.getSelectedIndex() == 4) {
                tempSql = tempSql +
                        "CAST(" +
                        "LPAD(EXTRACT(DAY FROM CONTAPAGAR_DATA), 2, '0') || '/' || " +
                        "LPAD(EXTRACT(MONTH FROM CONTAPAGAR_DATA), 2, '0') || '/' || " +
                        "LPAD(EXTRACT(YEAR FROM CONTAPAGAR_DATA), 4, '0') " +
                        "AS VARCHAR(10)) " +
                        "LIKE '%";
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

        guardaSql = tempSql;

        if (jcbOrdenar.getSelectedIndex() > 0) {
            Class tipo = parametrosConsulta.getFiltros()[jcbOrdenar.getSelectedIndex() - 1].tipo;
            tempSql = guardaSql + " order by ";
            tempSql = tempSql + parametrosConsulta.getFiltros()[jcbOrdenar.getSelectedIndex() - 1].nomeAtributo;
            System.out.println("" + tempSql);
        }

        List<Object[]> dados = Conexao.pesquisar(tempSql);
        for (int i = 0; i < dados.size(); i++) {
            dtm.addRow(dados.get(i));
        }
        System.out.println("" + tempSql);
    }

    private void preencherDadosListar() {
        String tempSql = parametrosConsulta.getSql();
        guardaSql = tempSql;
        tempSql = parametrosConsulta.getSql() + " ORDER BY PARCELACONTAPAGAR.VENCIMENTO";

        System.out.println("" + tempSql);
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        List<Object[]> dados = Conexao.pesquisar(tempSql);
        for (int i = 0; i < dados.size(); i++) {
            dtm.addRow(dados.get(i));
        }
        System.out.println("" + tempSql);
    }
    
    private void somaValorPagar() {
        BigDecimal soma = BigDecimal.ZERO;
        for (int i = 0; i < dtm.getRowCount(); i++) {
            BigDecimal valorContaTabela = new BigDecimal((String)tabela.getValueAt(i, 7));
            //System.out.println("VALLLRRR " + valorContaTabela);
            soma = soma.add(valorContaTabela);
            //System.out.println("SOMAAA " + soma);
        }
        labelValorContaPagar.setText("Valor total a pagar: " + NumberFormat.getCurrencyInstance().format(soma));
    }
           
}

