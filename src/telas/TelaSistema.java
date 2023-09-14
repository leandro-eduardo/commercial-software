package telas;

import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import telas_relatorios.TelaRelatorioClientesFornecedoresFuncionarios;
import telas_relatorios.TelaRelatorioCompras;
import telas_relatorios.TelaRelatorioContasPagar;
import telas_relatorios.TelaRelatorioContasReceber;
import telas_relatorios.TelaRelatorioInadimplenciasCliente;
import telas_relatorios.TelaRelatorioIndicadoresPagamento;
import telas_relatorios.TelaRelatorioIndicadoresRecebimento;
import telas_relatorios.TelaRelatorioPagamentosRecebimentos;
import telas_relatorios.TelaRelatorioProduto;
import telas_relatorios.TelaRelatorioProdutosMaisVendidos;
import telas_relatorios.TelaRelatorioVendas;

public class TelaSistema extends JFrame implements ActionListener {

    public static JDesktopPane jdp = new JDesktopPane() { //gerencia as janelas internas do sistema
        private Image imagem = new ImageIcon(getClass().getResource("/imagens/background.jpg")).getImage();

        @Override
        public void paintComponent(Graphics g) {
            //super.paintComponents(g); estava sobrepondo a JMenuBar
            setBackground(Color.white);
            Dimension dimension = this.getSize();
            int x = (int) (dimension.getWidth() - imagem.getWidth(this)) / 2;
            int y = (int) (dimension.getHeight() - imagem.getHeight(this)) / 2;
            g.drawImage(imagem, x, y, imagem.getWidth(this), imagem.getHeight(this), this);
        }
    };
    public JMenuBar jmb = new JMenuBar();
    public JMenu jmCadastros = new JMenu("Cadastros");
    private Icon iconCadastro = new ImageIcon(getClass().getResource("/icones/cadastro.png"));
    public JMenu jmMovimentos = new JMenu("Movimentos");
    private Icon iconMovimento = new ImageIcon(getClass().getResource("/icones/movimento.png"));
    public JMenu jmRelatorios = new JMenu("Relatórios");
    private Icon iconRelatorio = new ImageIcon(getClass().getResource("/icones/relatorio.png"));
    public JMenu jmOpcoes = new JMenu("Opções");
    public JPanel painelInferior = new JPanel();
    public JLabel labelUsuarioLogado = new JLabel();
    public JLabel labelHoras = new JLabel();

    private Icon iconPais = new ImageIcon(getClass().getResource("/icones/pais.png"));
    public JMenuItem jmiPais = new JMenuItem("País", iconPais);
    private Icon iconEstado = new ImageIcon(getClass().getResource("/icones/estado.png"));
    public JMenuItem jmiEstado = new JMenuItem("Estado", iconEstado);
    private Icon iconCidade = new ImageIcon(getClass().getResource("/icones/cidade.png"));
    public JMenuItem jmiCidade = new JMenuItem("Cidade", iconCidade);
    private Icon iconPessoa = new ImageIcon(getClass().getResource("/icones/pessoa.png"));
    public JMenuItem jmiPessoa = new JMenuItem("Pessoa", iconPessoa);
    private Icon iconCor = new ImageIcon(getClass().getResource("/icones/cor2.png"));
    public JMenuItem jmiCor = new JMenuItem("Cor", iconCor);
    private Icon iconCondicaoPagamento = new ImageIcon(getClass().getResource("/icones/condicaopagamento.png"));
    public JMenuItem jmiCondicaoPagamento = new JMenuItem("Condição de Pagamento", iconCondicaoPagamento);
    private Icon iconFabricante = new ImageIcon(getClass().getResource("/icones/fabricante.png"));
    public JMenuItem jmiFabricante = new JMenuItem("Fabricante", iconFabricante);
    private Icon iconModelo = new ImageIcon(getClass().getResource("/icones/modelo.png"));
    public JMenuItem jmiModelo = new JMenuItem("Modelo", iconModelo);
    private Icon iconVeiculo = new ImageIcon(getClass().getResource("/icones/veiculo.png"));
    public JMenuItem jmiVeiculo = new JMenuItem("Veículo", iconVeiculo);
    private Icon iconCategoria = new ImageIcon(getClass().getResource("/icones/categoria.png"));
    public JMenuItem jmiCategoria = new JMenuItem("Categoria", iconCategoria);
    private Icon iconServico = new ImageIcon(getClass().getResource("/icones/servico.png"));
    public JMenuItem jmiServico = new JMenuItem("Serviço", iconServico);
    private Icon iconProduto = new ImageIcon(getClass().getResource("/icones/produto.png"));
    public JMenuItem jmiProduto = new JMenuItem("Produto", iconProduto);
    private Icon iconUsuario = new ImageIcon(getClass().getResource("/icones/man-user.png"));
    public JMenuItem jmiUsuario = new JMenuItem("Usuário", iconUsuario);
    private Icon iconCompra = new ImageIcon(getClass().getResource("/icones/compra.png"));
    public JMenuItem jmiCompra = new JMenuItem("Compra", iconCompra);
    private Icon iconContaPagar = new ImageIcon(getClass().getResource("/icones/contapagar.png"));
    public JMenuItem jmiContaPagar = new JMenuItem("Conta a Pagar", iconContaPagar);
    private Icon iconPagamento = new ImageIcon(getClass().getResource("/icones/pagamento.png"));
    public JMenuItem jmiPagamento = new JMenuItem("Pagamento", iconPagamento);
    private Icon iconRecebimento = new ImageIcon(getClass().getResource("/icones/recebimento.png"));
    public JMenuItem jmiRecebimento = new JMenuItem("Recebimento", iconRecebimento);
    private Icon iconVenda = new ImageIcon(getClass().getResource("/icones/compra.png"));
    public JMenuItem jmiVenda = new JMenuItem("Venda", iconVenda);
    private Icon iconContaReceber = new ImageIcon(getClass().getResource("/icones/contapagar.png"));
    public JMenuItem jmiContaReceber = new JMenuItem("Conta a Receber", iconContaReceber);
    private Icon iconMovimentoCaixa = new ImageIcon(getClass().getResource("/icones/caixa.png"));
    public JMenuItem jmiMovimentoCaixa = new JMenuItem("Caixa", iconMovimentoCaixa);
    private Icon iconSair = new ImageIcon(getClass().getResource("/icones/001-exit-1.png"));
    public JMenuItem jmiSair = new JMenuItem("Sair", iconSair);
    private Icon iconOpcoes = new ImageIcon(getClass().getResource("/icones/003-settings.png"));

    public JMenuItem jmiRelatorioPessoa = new JMenuItem("Relatório de Clientes/Fornecedores/Funcionários");
    public JMenuItem jmiRelatorioProdutos = new JMenuItem("Relatório de Produtos");
    public JMenuItem jmiRelatorioProdutosMaisVendidos = new JMenuItem("Relatório dos Produtos Mais Vendidos");
    public JMenuItem jmiRelatorioVendas = new JMenuItem("Relatório de Vendas");
    public JMenuItem jmiRelatorioCompras = new JMenuItem("Relatório de Compras");
    public JMenuItem jmiRelatorioContasPagar = new JMenuItem("Relatório de Contas a Pagar");
    public JMenuItem jmiRelatorioContasReceber = new JMenuItem("Relatório de Contas a Receber");
    public JMenuItem jmiRelatorioPagamentosRecebimentos = new JMenuItem("Relatório de Pagamentos e Recebimentos");
    public JMenuItem jmiRelatorioInadimplencia = new JMenuItem("Relatório Inadimplências por Cliente");
    public JMenuItem jmiRelatorioIndicadoresPagamentos = new JMenuItem("Indicadores de Pagamentos");
    public JMenuItem jmiRelatorioIndicadoresRecebimentos = new JMenuItem("Indicadores de Recebimentos");

    public TelaSistema() {
        //coloca uma figura na barra de título da janela
        URL url = this.getClass().getResource("/icones/logo64px.png");
        Image imagemTitulo = Toolkit.getDefaultToolkit().getImage(url);
        this.setIconImage(imagemTitulo);
        //setSize(800, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("LSE Software - V1.0 - 2018");
        getContentPane().add(jdp);
        jmb.setLayout(new GridBagLayout()); // Setei o layout como GridBag
        GridBagConstraints esquerda = new GridBagConstraints(); // Constraint para menus a esquerda
        esquerda.anchor = GridBagConstraints.WEST;
        GridBagConstraints direita = new GridBagConstraints(); // Constraint para menu a direita e com espaçamento
        direita.anchor = GridBagConstraints.EAST;
        direita.weightx = 1.0;
        setJMenuBar(jmb);
        jmb.add(jmCadastros, esquerda);
        jmCadastros.setIcon(iconCadastro);
        jmb.add(jmMovimentos, esquerda);
        jmMovimentos.setIcon(iconMovimento);
        jmb.add(jmRelatorios);
        jmRelatorios.setIcon(iconRelatorio);
        jmb.add(jmOpcoes, direita);
        jmOpcoes.setIcon(iconOpcoes);

        adicionaJMenuItem(jmCadastros, jmiPais);
        adicionaJMenuItem(jmCadastros, jmiEstado);
        adicionaJMenuItem(jmCadastros, jmiCidade);
        adicionaJMenuItem(jmCadastros, jmiPessoa);
        adicionaJMenuItem(jmCadastros, jmiCor);
        adicionaJMenuItem(jmCadastros, jmiCondicaoPagamento);
        adicionaJMenuItem(jmCadastros, jmiFabricante);
        adicionaJMenuItem(jmCadastros, jmiModelo);
        adicionaJMenuItem(jmCadastros, jmiVeiculo);
        adicionaJMenuItem(jmCadastros, jmiCategoria);
        adicionaJMenuItem(jmCadastros, jmiServico);
        adicionaJMenuItem(jmCadastros, jmiProduto);
        adicionaJMenuItem(jmCadastros, jmiUsuario);
        adicionaJMenuItem(jmMovimentos, jmiCompra);
        adicionaJMenuItem(jmMovimentos, jmiVenda);
        adicionaJMenuItem(jmMovimentos, jmiContaPagar);
        adicionaJMenuItem(jmMovimentos, jmiContaReceber);
        adicionaJMenuItem(jmMovimentos, jmiPagamento);
        adicionaJMenuItem(jmMovimentos, jmiRecebimento);
        adicionaJMenuItem(jmMovimentos, jmiMovimentoCaixa);
        //CRIAR UM MENU COM MENU + ITEMMENU
        JMenu jmRelatoriosBasicos = new JMenu("Relatórios Básicos");
        JMenu jmRelatoriosFinanceiros = new JMenu("Relatórios Financeiros");
        JMenu jmRelatoriosGerenciais = new JMenu("Relatórios Gerenciais");
        adicionaJMenuItem(jmRelatorios, jmRelatoriosBasicos);
        adicionaJMenuItem(jmRelatorios, jmRelatoriosGerenciais);
        adicionaJMenuItem(jmRelatorios, jmRelatoriosFinanceiros);

        adicionaJMenuItem(jmRelatoriosBasicos, jmiRelatorioPessoa);
        adicionaJMenuItem(jmRelatoriosBasicos, jmiRelatorioProdutos);

        adicionaJMenuItem(jmRelatoriosGerenciais, jmiRelatorioProdutosMaisVendidos);
        adicionaJMenuItem(jmRelatoriosGerenciais, jmiRelatorioInadimplencia);
        adicionaJMenuItem(jmRelatoriosGerenciais, jmiRelatorioIndicadoresPagamentos);
        adicionaJMenuItem(jmRelatoriosGerenciais, jmiRelatorioIndicadoresRecebimentos);

        adicionaJMenuItem(jmRelatoriosFinanceiros, jmiRelatorioCompras);
        adicionaJMenuItem(jmRelatoriosFinanceiros, jmiRelatorioVendas);
        adicionaJMenuItem(jmRelatoriosFinanceiros, jmiRelatorioContasPagar);
        adicionaJMenuItem(jmRelatoriosFinanceiros, jmiRelatorioContasReceber);
        adicionaJMenuItem(jmRelatoriosFinanceiros, jmiRelatorioPagamentosRecebimentos);

        adicionaJMenuItem(jmOpcoes, jmiSair);

        painelInferior.setBackground(new Color(87, 170, 241));
        jdp.add(painelInferior);
        getContentPane().add("South", painelInferior);
        painelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT));
        String usuario = System.getProperty("login", "");
        labelUsuarioLogado.setText("Usuário: " + usuario + " - ");
        painelInferior.add(labelUsuarioLogado, LEFT_ALIGNMENT);
        JLabel label = new JLabel();
        Date dataSistema = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        label.setText(formato.format(dataSistema) + " - ");
        painelInferior.add(label);
        Timer timer = new Timer(1000, new horas());
        timer.start();
        painelInferior.add(labelHoras);
        JPanel espacador1 = new JPanel();
        espacador1.setPreferredSize(new Dimension(25, 10));
        //painelInferior.add(espacador1);
        label.setFont(new Font("Dialog", Font.PLAIN, 18));
        labelHoras.setFont(new Font("Dialog", Font.PLAIN, 18));
        labelUsuarioLogado.setFont(new Font("Dialog", Font.PLAIN, 18));
        adicionaListeners();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        //Aplicando o Look and Feel
        try {
            UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel()); //top
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Atualizando o JMenuBar jmb para aplicar o Look and Feel nele também, pois o L&F é aplicado fora do método main para que a tela de login possa ser totalmente personalizada
        //Com o Look and Feel direto na tela de login as personalizações ficam limitadas.
        SwingUtilities.updateComponentTreeUI(jmb);
        //setUndecorated(false); //setUndecorated(true) = full Screen
        setVisible(true);
    }

    @Override
    public void setDefaultCloseOperation(int i) {
        super.setDefaultCloseOperation(i); //To change body of generated methods, choose Tools | Templates.
    }

    public void adicionaListeners() {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Deseja realmente fechar o sistema?", "Confirmação",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        jmiSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (JOptionPane.showConfirmDialog(null, "Deseja realmente fechar o sistema?", "Confirmação",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }

        });
    }

    private void adicionaJMenuItem(JMenu menu, JMenuItem item) {
        menu.add(item);
        item.addActionListener(this);
    }

    public void getTela(final Class tela) {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        try {
            Method metodoGetTela = tela.getMethod("getTela");
            metodoGetTela.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == jmiPais) {
            getTela(TelaCadastroPais.class);
        } else if (ae.getSource() == jmiEstado) {
            getTela(TelaCadastroEstado.class);
        } else if (ae.getSource() == jmiCidade) {
            getTela(TelaCadastroCidade.class);
        } else if (ae.getSource() == jmiServico) {
            getTela(TelaCadastroServico.class);
        } else if (ae.getSource() == jmiPessoa) {
            getTela(TelaCadastroPessoa.class);
        } else if (ae.getSource() == jmiCor) {
            getTela(TelaCadastroCor.class);
        } else if (ae.getSource() == jmiCondicaoPagamento) {
            getTela(TelaCadastroCondicaoPagamento.class);
        } else if (ae.getSource() == jmiFabricante) {
            getTela(TelaCadastroFabricante.class);
        } else if (ae.getSource() == jmiModelo) {
            getTela(TelaCadastroModelo.class);
        } else if (ae.getSource() == jmiVeiculo) {
            getTela(TelaCadastroVeiculo.class);
        } else if (ae.getSource() == jmiCategoria) {
            getTela(TelaCadastroCategoria.class);
        } else if (ae.getSource() == jmiServico) {
            getTela(TelaCadastroServico.class);
        } else if (ae.getSource() == jmiProduto) {
            getTela(TelaCadastroProduto.class);
        } else if (ae.getSource() == jmiUsuario) {
            getTela(TelaCadastroUsuario.class);
        } else if (ae.getSource() == jmiCompra) {
            getTela(TelaCompra.class);
        } else if (ae.getSource() == jmiContaPagar) {
            getTela(TelaContaPagar.class);
        } else if (ae.getSource() == jmiPagamento) {
            getTela(TelaPagamento.class);
        } else if (ae.getSource() == jmiRecebimento) {
            getTela(TelaRecebimento.class);
        } else if (ae.getSource() == jmiMovimentoCaixa) {
            getTela(TelaCaixa.class);
        } else if (ae.getSource() == jmiVenda) {
            getTela(TelaVenda.class);
        } else if (ae.getSource() == jmiContaReceber) {
            getTela(TelaContaReceber.class);
        } else if (ae.getSource() == jmiRelatorioPessoa) {
            TelaRelatorioClientesFornecedoresFuncionarios telaRelatorioClientes = new TelaRelatorioClientesFornecedoresFuncionarios();
            jdp.add(telaRelatorioClientes);
            telaRelatorioClientes.setVisible(true);
        } else if (ae.getSource() == jmiRelatorioProdutos) {
            TelaRelatorioProduto telaRelatorioProduto = new TelaRelatorioProduto();
            jdp.add(telaRelatorioProduto);
            telaRelatorioProduto.setVisible(true);
        } else if (ae.getSource() == jmiRelatorioProdutosMaisVendidos) {
            TelaRelatorioProdutosMaisVendidos telaRelatorioProdutosMaisVendidos = new TelaRelatorioProdutosMaisVendidos();
            jdp.add(telaRelatorioProdutosMaisVendidos);
            telaRelatorioProdutosMaisVendidos.setVisible(true);
        } else if (ae.getSource() == jmiRelatorioCompras) {
            TelaRelatorioCompras telaRelatorioCompras = new TelaRelatorioCompras();
            jdp.add(telaRelatorioCompras);
            telaRelatorioCompras.setVisible(true);
        } else if (ae.getSource() == jmiRelatorioVendas) {
            TelaRelatorioVendas telaRelatorioVendas = new TelaRelatorioVendas();
            jdp.add(telaRelatorioVendas);
            telaRelatorioVendas.setVisible(true);
        } else if (ae.getSource() == jmiRelatorioContasPagar) {
            TelaRelatorioContasPagar telaRelatorioContasPagar = new TelaRelatorioContasPagar();
            jdp.add(telaRelatorioContasPagar);
            telaRelatorioContasPagar.setVisible(true);
        } else if (ae.getSource() == jmiRelatorioContasReceber) {
            TelaRelatorioContasReceber telaRelatorioContasReceber = new TelaRelatorioContasReceber();
            jdp.add(telaRelatorioContasReceber);
            telaRelatorioContasReceber.setVisible(true);
        } else if (ae.getSource() == jmiRelatorioPagamentosRecebimentos) {
            TelaRelatorioPagamentosRecebimentos telaRelatorioPagamentosRecebimentos = new TelaRelatorioPagamentosRecebimentos();
            jdp.add(telaRelatorioPagamentosRecebimentos);
            telaRelatorioPagamentosRecebimentos.setVisible(true);
        } else if (ae.getSource() == jmiRelatorioInadimplencia) {
            TelaRelatorioInadimplenciasCliente telaRelatorioInadimplenciasCliente = new TelaRelatorioInadimplenciasCliente();
            jdp.add(telaRelatorioInadimplenciasCliente);
            telaRelatorioInadimplenciasCliente.setVisible(true);
        } else if (ae.getSource() == jmiRelatorioIndicadoresPagamentos) {
            TelaRelatorioIndicadoresPagamento telaRelatorioIndicadoresPagamento = new TelaRelatorioIndicadoresPagamento();
            jdp.add(telaRelatorioIndicadoresPagamento);
            telaRelatorioIndicadoresPagamento.setVisible(true);
        } else if (ae.getSource() == jmiRelatorioIndicadoresRecebimentos) {
            TelaRelatorioIndicadoresRecebimento telaRelatorioIndicadoresRecebimento = new TelaRelatorioIndicadoresRecebimento();
            jdp.add(telaRelatorioIndicadoresRecebimento);
            telaRelatorioIndicadoresRecebimento.setVisible(true);
        }
    }

    class horas implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Calendar now = Calendar.getInstance();
            labelHoras.setText(String.format("%1$tH:%1tM:%1$tS", now));
        }
    }

}
