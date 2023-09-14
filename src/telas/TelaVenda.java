package telas;

import bd.Conexao;
import componentes.*;
import dao.DaoCondicaoPagamento;
import dao.DaoPessoa;
import dao.DaoProduto;
import dao.DaoServico;
import dao.DaoVeiculo;
import dao.DaoVenda;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_ENTER;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import pojo.*;
import renderizador.*;
import tabela.TabelaItemVenda;
import tabela.TableModelItemVenda;

public class TelaVenda extends TelaCadastro {

    int varAuxTabela = 0;

    private Icon iconImprimir = new ImageIcon(getClass().getResource("/icones/printer.png"));
    public JButton jbImprimir = new JButton("Imprimir", iconImprimir);

    public static TabelaItemVenda tabelaItemVenda = new TabelaItemVenda();
    public static TelaVenda tela;
    public ContaReceber contaReceber = new ContaReceber();
    public Venda venda = new Venda();
    public DaoVenda daoVenda = new DaoVenda(venda);
    public CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
    public DaoCondicaoPagamento daoCondicaoPagamento = new DaoCondicaoPagamento(condicaoPagamento);
    public ItemVenda itemVenda = new ItemVenda();
    public ParametrosConsulta parametrosConsultaProduto
            = new ParametrosConsulta("Consulta de Produto",
                    DaoProduto.SQLPESQUISAR2,
                    new String[]{"Código", "Nome", "Quantidade", "Fabricante", "Categoria", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "PRODUTO_NOME", String.class),
                        new FiltroPesquisa("Fabricante", "FABRICANTE_NOME", String.class),
                        new FiltroPesquisa("Categoria", "CATEGORIA_NOME", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new InteiroRender(),
                        new RenderizadorTexto(), new RenderizadorTexto(), new RenderizadorTexto()},
                    this, this, false, false, false
            );
    public ParametrosConsulta parametrosConsultaServico
            = new ParametrosConsulta("Consulta de Servico",
                    DaoServico.SQLPESQUISAR2,
                    new String[]{"Código", "Descrição", "Categoria", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Descrição", "SERVICO_NOME", String.class),
                        new FiltroPesquisa("Categoria", "CATEGORIA_NOME", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new RenderizadorTexto(), new RenderizadorTexto()},
                    this, this, false, false, false
            );
    public ParametrosConsulta parametrosConsultaClientes
            = new ParametrosConsulta("Consulta de Cliente",
                    DaoPessoa.SQLPESQUISARCLIENTES,
                    new String[]{"Código", "Nome", "CPF/CNPJ", "Cidade", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "NOMERAZAOSOCIAL", String.class),
                        new FiltroPesquisa("CPF/CNPJ", "CPFCNPJ", String.class),
                        new FiltroPesquisa("Cidade", "CIDADE", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new RenderizadorTexto(),
                        new RenderizadorTexto(), new RenderizadorTexto(), new RenderizadorTexto(),
                        new RenderizadorTexto()
                    },
                    this, this, false, false, false
            );
    public ParametrosConsulta parametrosConsultaCondPag
            = new ParametrosConsulta("Consulta de Condição de Pagamento",
                    DaoCondicaoPagamento.SQLPESQUISAR2,
                    new String[]{"Código", "Descrição", "Parcelas", "Carência", "Prazo", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Descrição", "DESCRICAO", String.class),
                        new FiltroPesquisa("Parcelas", "PARCELAS", String.class),
                        new FiltroPesquisa("Carência", "CARENCIA", String.class),
                        new FiltroPesquisa("Prazo", "PRAZO", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new InteiroRender(),
                        new InteiroRender(), new InteiroRender(), new RenderizadorTexto()},
                    this, this, false, false, false
            );
    public ParametrosConsulta parametrosConsultaFuncionarios
            = new ParametrosConsulta("Consulta de Funcionários",
                    DaoPessoa.SQLPESQUISARFUNCIONARIOS,
                    new String[]{"Código", "Nome", "CPF/CNPJ", "Cidade", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "NOMERAZAOSOCIAL", String.class),
                        new FiltroPesquisa("CPF/CNPJ", "CPFCNPJ", String.class),
                        new FiltroPesquisa("Cidade", "CIDADE", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new RenderizadorTexto(),
                        new RenderizadorTexto(), new RenderizadorTexto(), new RenderizadorTexto(),
                        new RenderizadorTexto()
                    },
                    this, this, false, false, false
            );

    public ParametrosConsulta parametrosConsultaVeiculos
            = new ParametrosConsulta("Consulta de Veículos",
                    DaoVeiculo.SQLPESQUISAR2,
                    new String[]{"Código", "Modelo", "Placa", "Fabricante", "Cor", "Ano", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Modelo", "MODELO_NOME", String.class),
                        new FiltroPesquisa("Placa", "PLACA", String.class),
                        new FiltroPesquisa("Fabricante", "FABRICANTE_NOME", String.class),
                        new FiltroPesquisa("Cor", "COR_NOME", String.class),
                        new FiltroPesquisa("Ano", "ANOFABRICACAO", String.class),
                        new FiltroPesquisa("Situação", "VEICULO_ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new RenderizadorTexto(),
                        new RenderizadorTexto(), new RenderizadorTexto(), new RenderizadorTexto(),
                        new RenderizadorTexto()
                    },
                    this, this, false, false, false
            );
    public MeuCampoInteger campoCodigo
            = new MeuCampoInteger(6, false, false, false, "Código");
    public MeuCampoData campoData
            = new MeuCampoData(10, true, "Data");
    public MeuJComboBox campoStatus
            = new MeuJComboBox(false, "Status", new String[][]{{"PA", "Pedido Aberto"}, {"PC", "Pedido Cancelado"}, {"OR", "Orçamento"}, {"OC", "Orçamento Cancelado"}, {"OF", "O.S. Finalizada"}, {"OK", "O.S. Cancelada"}, {"VF", "Venda Finalizada"}, {"VC", "Venda Cancelada"}});
    public MeuCampoBuscar campoCliente = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroPessoa.class, DaoPessoa.SQLCLIENTES, DaoPessoa.SQLINATIVOS, parametrosConsultaClientes, true, true, "Cliente", 30);
    public MeuCampoBuscar campoFuncionario = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroPessoa.class, DaoPessoa.SQLFUNCIONARIOS, DaoPessoa.SQLINATIVOS, parametrosConsultaFuncionarios, true, true, "Funcionário", 30);
    public MeuCampoBuscar campoVeiculo = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroVeiculo.class, DaoVeiculo.SQLCOMBOBOX, DaoPessoa.SQLINATIVOS, parametrosConsultaVeiculos, false, true, "Veículo", 30);
    private MeuJComboBox campoTipoItem = new MeuJComboBox(false, "Item", new String[][]{{"P", "Produto"}, {"P", "Serviço"}});
    public MeuCampoBuscar campoProdutoItemVenda = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroProduto.class, DaoProduto.SQLCOMBOBOX, DaoProduto.SQLINATIVOS, parametrosConsultaProduto, false, true, "Produtos", 40);
    public MeuCampoBuscar campoServicoItemVenda = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroServico.class, DaoServico.SQLCOMBOBOX, DaoServico.SQLINATIVOS, parametrosConsultaServico, false, true, "Serviços", 40);
    public static MeuCampoMonetario campoValorTotal = new MeuCampoMonetario(15, false, true, false, "Valor total");
    public static MeuCampoMonetario campoDesconto = new MeuCampoMonetario(15, true, true, false, "Valor desconto");
    public static MeuCampoMonetario campoValorLiquido = new MeuCampoMonetario(15, false, true, false, "Valor líquido");
    public static MeuCampoInteger campoQuantidadeItemVenda = new MeuCampoInteger(5, false, true, false, "Quantidade");
    public static MeuCampoMonetario campoValorUnitarioItemVenda = new MeuCampoMonetario(15, true, false, false, "Valor unitário");
    public BigDecimal auxiliarCampoValorUnitarioItemVenda;
    public static MeuCampoMonetario campoDescontoItemVenda = new MeuCampoMonetario(15, true, true, false, "Desconto");
    public static MeuCampoMonetario campoValorTotalItemVenda = new MeuCampoMonetario(15, false, true, false, "Valor total");
    public MeuCampoBuscar campoCondicaoPagamento = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroCondicaoPagamento.class, DaoCondicaoPagamento.SQLCOMBOBOX, DaoCondicaoPagamento.SQLINATIVOS, parametrosConsultaCondPag, true, true, "Cond. Pagamento", 15);
    private MeuCampoCheckBox crGerada = new MeuCampoCheckBox(false, false, "Conta gerada");

    private JPanel painelItemProduto = new JPanel();
    private JPanel painelItemServico = new JPanel();
    private JPanel painelInformacoesItem = new JPanel();
    private JPanel painelProduto = new JPanel();
    private JPanel painelServico = new JPanel();

    private JPanel painelValores = new JPanel();

    JLabel labelQuantidadeItemVenda = new JLabel("Qtde: ");
    JLabel labelValorUnitarioItemVenda = new JLabel("Valor unitário: ");
    JLabel labelDescontoItemVenda = new JLabel("Desconto: ");
    JLabel labelValorTotalItemVenda = new JLabel("Total: ");

    private Icon iconAdicionar = new ImageIcon(getClass().getResource("/icones/adicionar.png"));
    private Icon iconRemover = new ImageIcon(getClass().getResource("/icones/remover.png"));

    private MeuJButton botaoAdicionar = new MeuJButton(iconAdicionar, false, false, false);
    private MeuJButton botaoRemover = new MeuJButton(iconRemover, false, false, false);

    public TelaVenda() {
        super("Cadastro de Venda");
        adicionaScrollPane();
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/compra.png")));
        adicionaComponente(1, 0, 1, 1, campoCodigo, jpComponentes);
        adicionaComponente(2, 0, 1, 1, campoData, jpComponentes);
        adicionaComponente(3, 0, 1, 1, campoStatus, jpComponentes);
        adicionaComponente(4, 0, 1, 1, campoCliente, jpComponentes);
        adicionaComponente(5, 0, 1, 1, campoVeiculo, jpComponentes);
        adicionaComponente(6, 0, 1, 1, campoFuncionario, jpComponentes);

        criaPainelProdutoItem();
        criaPainelValores();

        adicionaComponente(10, 0, 1, 1, crGerada, jpComponentes);

        //tabelaItem
        adicionaComponente(11, 0, 1, 1, tabelaItemVenda, jpComponentes);
        //JPanel espacador = new JPanel();
        //espacador.setPreferredSize(new Dimension(20, 10));
        //adicionaComponente(1, 2, 1, 1, espacador, jpComponentes);

        itemVenda.setTipo(2);
        adicionaComponenteArray(campoTipoItem);
        adicionaComponenteArray(campoProdutoItemVenda);
        adicionaComponenteArray(campoServicoItemVenda);
        adicionaComponenteArray(campoQuantidadeItemVenda);
        adicionaComponenteArray(campoValorUnitarioItemVenda);
        adicionaComponenteArray(campoDescontoItemVenda);
        adicionaComponenteArray(campoValorTotalItemVenda);
        adicionaComponenteArray(botaoAdicionar);
        adicionaComponenteArray(botaoRemover);
        habilitaComponentes(false);

        jpBotoes.add(jbImprimir, 3); //3 é a posição para adicionar o botão
        jbImprimir.setEnabled(false);
        //pack();
        setSize(1350, 630);
        adicionaListeners();
        centralizaTela();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaVenda();
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

    public static void atualizaCamposTotal(JComponent quemChamou) {
//        BigDecimal valorLiquido = campoValorTotal.getValor().subtract(campoDesconto.getValor());
        BigDecimal quantidadeItem = BigDecimal.valueOf(new Double((int) campoQuantidadeItemVenda.getValor()));
        BigDecimal valorUnitarioItem = campoValorUnitarioItemVenda.getValor();
        BigDecimal descontoItem = campoDescontoItemVenda.getValor();
        BigDecimal valorTotalItem = quantidadeItem.multiply(valorUnitarioItem.subtract(descontoItem));
//        if (quemChamou != null && valorTotalItem.compareTo(BigDecimal.ZERO) == -1) {
//            JOptionPane.showMessageDialog(null, "Valor do item de venda negativo. Enquanto o valor estiver negativo não será possível confirmar a venda. Verifique!!!");
//            quemChamou.requestFocus();
//        } else if (quemChamou != null && valorLiquido.compareTo(BigDecimal.ZERO) == -1) {
//            JOptionPane.showMessageDialog(null, "Valor total da venda negativo. Enquanto o valor estiver negativo não será possível confirmar a venda. Verifique!!!");
//            quemChamou.requestFocus();
//        } else {
        campoValorTotalItemVenda.setValor(valorTotalItem);
        tabelaItemVenda.getTableModel().alteraValorUnitario(campoValorUnitarioItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
        tabelaItemVenda.getTableModel().alteraValorTotal(campoValorTotalItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
//        }

        List<ItemVenda> itens = tabelaItemVenda.getTableModel().getDados();
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (int i = 0; i < itens.size(); i++) {
            valorTotal = valorTotal.add(itens.get(i).getValorTotal());
        }
        campoValorTotal.setValor(valorTotal);
        campoValorLiquido.setValor(valorTotal.subtract(campoDesconto.getValor()));
        campoValorTotalItemVenda.setValor(valorTotalItem);

        //o IF abaixo valida se o campoValorLiquido ou o campoValorTotalItemVenda estão negativos. Se sim, o botao Confirmar é desabilitado para impedir o usuário de continuar.
        if (campoValorLiquido.getValor().compareTo(BigDecimal.ZERO) == -1 || campoValorTotalItemVenda.getValor().compareTo(BigDecimal.ZERO) == -1) {
            tela.jbConfirmar.setEnabled(false);
        } else {
            tela.jbConfirmar.setEnabled(true);
        }
    }

    public void setPersistencia() {
        venda.setId((int) campoCodigo.getValor());
        venda.setData(campoData.getValorDate());
        venda.setValorTotal(campoValorTotal.getValor());
        venda.setDesconto(campoDesconto.getValor());
        venda.setValorLiquido(campoValorLiquido.getValor());
        venda.setStatus((String) campoStatus.getValor());
        venda.setCrGerada(crGerada.getValor());
        venda.getPessoa().setId((int) campoCliente.getValor());
        venda.getPessoa1().setId((int) campoFuncionario.getValor());
        venda.getVeiculo().setId((int) campoVeiculo.getValor());
        venda.getCondicaoPagamento().setId((int) campoCondicaoPagamento.getValor());
        venda.setItensVenda(((TableModelItemVenda) tabelaItemVenda.getTabela().getModel()).getDados());
    }

    public void getPersistencia() {
        campoCodigo.setValor(venda.getId());
        campoData.setValor(venda.getData());
        campoValorTotal.setValor(venda.getValorTotal());
        campoDesconto.setValor(venda.getDesconto());
        campoValorLiquido.setValor(venda.getValorLiquido());
        campoStatus.setValor(venda.getStatus());
        crGerada.setValor(venda.isCrGerada());
        campoCliente.setValor(venda.getPessoa().getId());
        campoFuncionario.setValor(venda.getPessoa1().getId());
        campoVeiculo.setValor(venda.getVeiculo().getId());
        campoCondicaoPagamento.setValor(venda.getCondicaoPagamento().getId());
        ((TableModelItemVenda) tabelaItemVenda.getTabela().getModel()).setDados(venda.getItensVenda());
    }

    public void getTelaContaReceber() {
        TelaContaReceber telaContaReceber = new TelaContaReceber(contaReceber);
        TelaSistema.jdp.add(telaContaReceber);
        TelaSistema.jdp.moveToFront(this);
    }

    public void adicionaListeners() {
        campoProdutoItemVenda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    if (campoProdutoItemVenda.getValor() == "" || campoProdutoItemVenda.achou == false) {

                    } else {
                        itemVenda.setTipo(2);
                        tabelaItemVenda.getTableModel().alteraProduto(campoProdutoItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                        tabelaItemVenda.getTableModel().alteraTipo(itemVenda.getTipo(), tabelaItemVenda.getLinhaSelecionada());
                        Produto produto = tabelaItemVenda.getTableModel().getProduto(tabelaItemVenda.getLinhaSelecionada());
                        campoValorUnitarioItemVenda.setValor(produto.getPrecovenda());
                        atualizaCamposTotal(null);
                    }
                }
            }
        });
        campoServicoItemVenda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    if (campoServicoItemVenda.getValor() == "" || campoServicoItemVenda.achou == false) {

                    } else {
                        itemVenda.setTipo(1);
                        tabelaItemVenda.getTableModel().alteraServico(campoServicoItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                        tabelaItemVenda.getTableModel().alteraTipo(itemVenda.getTipo(), tabelaItemVenda.getLinhaSelecionada());
                        Servico servico = tabelaItemVenda.getTableModel().getServico(tabelaItemVenda.getLinhaSelecionada());
                        campoValorUnitarioItemVenda.setValor(servico.getValor());
                        atualizaCamposTotal(null);
                    }
                }
            }
        });
        campoProdutoItemVenda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (campoProdutoItemVenda.eVazio() || campoProdutoItemVenda.getValor() == "0") {
                            campoQuantidadeItemVenda.limpar();
                            campoValorUnitarioItemVenda.limpar();
                            campoDescontoItemVenda.limpar();
                            campoValorTotalItemVenda.limpar();
                            tabelaItemVenda.getTableModel().alteraProduto(0, tabelaItemVenda.getLinhaSelecionada());
                            tabelaItemVenda.getTableModel().alteraQuantidade(campoQuantidadeItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                            tabelaItemVenda.getTableModel().alteraDesconto(campoDescontoItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                        }
                        atualizaCamposTotal(null);
                    }
                });
            }
        });
        campoServicoItemVenda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (campoServicoItemVenda.eVazio() || campoServicoItemVenda.getValor() == "0") {
                            campoQuantidadeItemVenda.limpar();
                            campoValorUnitarioItemVenda.limpar();
                            campoDescontoItemVenda.limpar();
                            campoValorTotalItemVenda.limpar();
                            tabelaItemVenda.getTableModel().alteraServico(0, tabelaItemVenda.getLinhaSelecionada());
                            tabelaItemVenda.getTableModel().alteraQuantidade(campoQuantidadeItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                            tabelaItemVenda.getTableModel().alteraDesconto(campoDescontoItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                        }
                        atualizaCamposTotal(null);
                    }
                });
            }
        });

        campoQuantidadeItemVenda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        tabelaItemVenda.getTableModel().alteraQuantidade(campoQuantidadeItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                        atualizaCamposTotal(null);
                    }
                });
            }
        });

        campoQuantidadeItemVenda.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {

            }

            @Override
            public void focusLost(FocusEvent fe) {
                if (verificaQuantidadeItem() == true) {
                    campoQuantidadeItemVenda.requestFocus();
                }
            }
        });

        campoValorUnitarioItemVenda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        tabelaItemVenda.getTableModel().alteraValorUnitario(campoValorUnitarioItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                        atualizaCamposTotal(null);
                    }
                });
            }
        });
        campoDescontoItemVenda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        BigDecimal quantidadeItem = BigDecimal.valueOf(new Double((int) campoQuantidadeItemVenda.getValor()));
                        BigDecimal valorUnitarioItem = campoValorUnitarioItemVenda.getValor();
                        BigDecimal descontoItem = campoDescontoItemVenda.getValor();
                        BigDecimal valorTotalItem = quantidadeItem.multiply(valorUnitarioItem.subtract(descontoItem));
                        campoValorTotalItemVenda.setValor(valorTotalItem);
                        tabelaItemVenda.getTableModel().alteraValorUnitario(campoValorUnitarioItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                        tabelaItemVenda.getTableModel().alteraValorTotal(campoValorTotalItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                        tabelaItemVenda.getTableModel().alteraDesconto(campoDescontoItemVenda.getValor(), tabelaItemVenda.getLinhaSelecionada());
                        atualizaCamposTotal(campoDescontoItemVenda);
                    }
                });
            }
        });
        campoDescontoItemVenda.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {

            }

            @Override
            public void focusLost(FocusEvent fe) {
                BigDecimal quantidadeItem = BigDecimal.valueOf(new Double((int) campoQuantidadeItemVenda.getValor()));
                BigDecimal valorUnitarioItem = campoValorUnitarioItemVenda.getValor();
                BigDecimal descontoItem = campoDescontoItemVenda.getValor();
                BigDecimal valorTotalItem = quantidadeItem.multiply(valorUnitarioItem.subtract(descontoItem));
                if (valorTotalItem.compareTo(BigDecimal.ZERO) == -1) {
                    JOptionPane.showMessageDialog(null, "Valor do item de venda negativo. Enquanto o valor estiver negativo não será possível confirmar a compra. Verifique!!!");
                    campoDescontoItemVenda.requestFocus();
                }
            }
        });
        campoDesconto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        atualizaCamposTotal(campoDesconto);
                    }
                });
            }
        });
        campoDesconto.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {

            }

            @Override
            public void focusLost(FocusEvent fe) {
                BigDecimal valorLiquido = campoValorTotal.getValor().subtract(campoDesconto.getValor());
                if (valorLiquido.compareTo(BigDecimal.ZERO) == -1) {
                    JOptionPane.showMessageDialog(null, "Valor total da venda negativo. Enquanto o valor estiver negativo não será possível confirmar a compra. Verifique!!!");
                    campoDesconto.requestFocus();
                }
            }
        });

        tabelaItemVenda.getTabela().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting() && tabelaItemVenda.getTabela().getSelectedRow() >= 0) {
                    if (tabelaItemVenda.getTableModel().getValueAt(tabelaItemVenda.getLinhaSelecionada(), 0) == "") {
                        campoProdutoItemVenda.limpar();
                        campoServicoItemVenda.limpar();
                        campoQuantidadeItemVenda.limpar();
                        campoValorUnitarioItemVenda.limpar();
                        campoDescontoItemVenda.limpar();
                        campoValorTotalItemVenda.limpar();
                    } else if (tabelaItemVenda.getTableModel().getItemVenda(tabelaItemVenda.getTabela().getSelectedRow()).getTipo() == 2) {
                        campoTipoItem.setSelectedIndex(0);
                        ItemVenda itemVenda = tabelaItemVenda.getTableModel().getItemVenda(tabelaItemVenda.getTabela().getSelectedRow());
                        campoProdutoItemVenda.setValor(itemVenda.getProduto().getId());
                        campoQuantidadeItemVenda.setValor(itemVenda.getQuantidade());
                        //campoValorUnitarioItemVenda.setValor(itemVenda.getValorUnitario()); o valor desse campo é setado no método preencher do meuCampoBuscar. Se for igual ao valor registrado na tabela o manterá, se não atualiza para o novo preço
                        campoDescontoItemVenda.setValor(itemVenda.getDesconto());
                        campoValorTotalItemVenda.setValor(itemVenda.getValorTotal());
                        atualizaCamposTotal(null);
                    } else if ((tabelaItemVenda.getTableModel().getItemVenda(tabelaItemVenda.getTabela().getSelectedRow()).getTipo() == 1)) {
                        campoTipoItem.setSelectedIndex(1);
                        ItemVenda itemVenda = tabelaItemVenda.getTableModel().getItemVenda(tabelaItemVenda.getTabela().getSelectedRow());
                        campoServicoItemVenda.setValor(itemVenda.getServico().getId());
                        campoQuantidadeItemVenda.setValor(itemVenda.getQuantidade());
                        //campoValorUnitarioItemVenda.setValor(itemVenda.getValorUnitario()); o valor desse campo é setado no método preencher do meuCampoBuscar. Se for igual ao valor registrado na tabela o manterá, se não atualiza para o novo preço
                        campoDescontoItemVenda.setValor(itemVenda.getDesconto());
                        campoValorTotalItemVenda.setValor(itemVenda.getValorTotal());
                        atualizaCamposTotal(null);
                    }
                }
            }
        });

        campoTipoItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
//                removeComponente(painelItemProduto);
//                removeComponente(painelItemServico);
                jpComponentes.remove(painelItemProduto);
                jpComponentes.remove(painelItemServico);
                campoProdutoItemVenda.limpar();
                campoServicoItemVenda.limpar();
                //    itemVenda = tm.getItemServico(tabela.getSelectedRow());
                if (campoTipoItem.getSelectedIndex() == 0) { //produto

                    itemVenda.setTipo(2);
                    criaPainelProdutoItem();
                    campoValorUnitarioItemVenda.limpar();
                    campoDescontoItemVenda.limpar();
                    campoValorTotalItemVenda.limpar();
                    habilitaComponentes(true);
                } else if (campoTipoItem.getSelectedIndex() == 1) { //serviço

                    itemVenda.setTipo(1);
                    criaPainelServicoItem();
                    campoValorUnitarioItemVenda.limpar();
                    campoDescontoItemVenda.limpar();
                    campoValorTotalItemVenda.limpar();
                    habilitaComponentes(true);
                }
                setSize(1350, 630);
                //pack();
            }
        });

        botaoAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                campoProdutoItemVenda.limpar();
                campoServicoItemVenda.limpar();
                TabelaItemVenda.tmi.adicionaLinha();
                TabelaItemVenda.tabela.addRowSelectionInterval(TabelaItemVenda.tmi.getRowCount() - 1, TabelaItemVenda.tmi.getRowCount() - 1);
            }
        });
        botaoRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (TabelaItemVenda.tabela.getSelectedRow() >= 0) {
                    if (TabelaItemVenda.tmi.getRowCount() > 1) {
                        TabelaItemVenda.tmi.removeLinha(TabelaItemVenda.tabela.getSelectedRow());
                        TabelaItemVenda.tabela.addRowSelectionInterval(TabelaItemVenda.tmi.getRowCount() - 1, TabelaItemVenda.tmi.getRowCount() - 1);
                        TelaVenda.atualizaCamposTotal(null);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Selecione uma linha para poder excluí-la.");
                }
            }
        });

        jbImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirma == JOptionPane.YES_OPTION) {

                    //Imprimindo o relatório
                    //Caminho absoluto comentado para utilizar o caminho relativo que pega os arquivo da pasta src do projeto
                    //String caminhoRelatorio = System.getProperty("user.dir") + "/JasperReports/PedidoVenda.jasper";/*"C:\\Users\\leand\\JaspersoftWorkspace\\MyReports\\RelatorioProdutos.jasper";*/
                    //Caminho relativo
                    InputStream caminhoRelatorio = getClass().getResourceAsStream("/relatorios/PedidoVenda.jasper");
                    JasperPrint jasperPrint = null;

                    //Lista com os parametros para o relátorio
                    HashMap parametros = new HashMap<>();

                    //Passando parâmetros e convertendo os dados pra ser compativel                                            
                    parametros.put("IDVENDA", campoCodigo.getValor());

                    //Caminho relativo do sub relatório
                    URL caminhoSubRelatorio = getClass().getResource("/relatorios/subrelatorios/SubRelatorioItensPedidoVenda.jasper");
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
                    jv.setTitle("Impressão do Pedido de Venda");
                    jv.setVisible(true);
                }
            }
        ;
        });
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                limpar();
            }
        });

    }

    @Override
    public boolean validaComponentes() {
//        if (super.verificarCampos() == false) {  //Esta linha faz exatamente o mesmo do que a linha abaixo.
        if (!super.validaComponentes()) {
            return false;
        }
        List<ItemVenda> itens = ((TableModelItemVenda) tabelaItemVenda.getTabela().getModel()).getDados();
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getProduto().getId() == 0 && (itens.get(i).getServico().getId() == 0)) {
                JOptionPane.showMessageDialog(null, "Existe(m) item(ns) não informado(s).");
                return false;
            }
            if (itens.get(i).getQuantidade() <= 0) {
                JOptionPane.showMessageDialog(null, "Existe(m) item(ns) com quantidade inválida(s).");
                return false;
            }
            if (itens.get(i).getValorUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(null, "Existe(m) item(ns) com valor unitário invalido(s).");
                return false;
            }
            if (itens.get(i).getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(null, "Existe(m) item(ns) com valor total negativo.");
                return false;
            }
        }
        return true;
    }

    @Override
    public void habilitaBotoes() {
        super.habilitaBotoes();
        if ((campoStatus != null && campoStatus.getValor().equals("PC")) || (campoStatus != null && campoStatus.getValor().equals("OC")) || (campoStatus != null && campoStatus.getValor().equals("OK")) || (campoStatus != null && campoStatus.getValor().equals("VF")) || (campoStatus != null && campoStatus.getValor().equals("VC"))) {
            jbAlterar.setEnabled(false);
        }
        if ((campoStatus != null && campoStatus.getValor().equals("PC")) || (campoStatus != null && campoStatus.getValor().equals("OR")) || (campoStatus != null && campoStatus.getValor().equals("OC")) || (campoStatus != null && campoStatus.getValor().equals("OF")) || (campoStatus != null && campoStatus.getValor().equals("OK")) || (campoStatus != null && campoStatus.getValor().equals("VF")) || (campoStatus != null && campoStatus.getValor().equals("VC"))) {
            jbExcluir.setEnabled(false);
        }
        if (estadoTela == CONSULTANDO) {
            jbImprimir.setEnabled(true);
        }
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno = daoVenda.incluir();
        consultarBD(venda.getId());
        jbImprimir.setEnabled(true);
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoVenda.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoVenda.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        venda.setId(pk);
        daoVenda.consultar();
        getPersistencia();
        jbImprimir.setEnabled(true);
        super.consultarBD(pk);
    }

    @Override
    public void incluir() {
        super.incluir();
        campoData.setValor(new Date());
        jbImprimir.setEnabled(false);
        System.out.println("VALOR DO CAMPO TIPO ITEM NO INCLUIR ----> " + campoTipoItem.getSelectedIndex());
    }

    @Override
    public void alterar() {
        super.alterar();
        tabelaItemVenda.getTabela().addRowSelectionInterval(0, 0);
    }

    @Override
    public void confirmar() {
        if (verificaQuantidadeItem()) {
            jbConfirmar.setEnabled(false);
            return;
        } else {
            jbConfirmar.setEnabled(true);
        }
        super.confirmar();
    }

    @Override
    public void cancelar() {
        super.cancelar();
        jbImprimir.setEnabled(false);
        tabelaItemVenda.limparCancelar();
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsultaProduto = new ParametrosConsulta("Consulta de Venda",
                DaoVenda.SQLPESQUISAR,
                new String[]{"Código", "Cliente", "Data", "Valor", "Status", "CR Gerada"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                    new FiltroPesquisa("Cliente", "NOMERAZAOSOCIAL", String.class),
                    new FiltroPesquisa("Data", "DATA", Date.class),
                    new FiltroPesquisa("Valor", "VALORLIQUIDO", String.class),
                    new FiltroPesquisa("Status", "STATUS", String.class),
                    new FiltroPesquisa("CR Gerada", "CRGERADA", String.class),},
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new CellRendererData(),
                    new MonetarioRender(), new RenderizadorStatus(), new RenderizadorStatus()},
                this, this, true, false, true));
    }

    private void criaPainelProdutoItem() {
        //painelItemProduto
        painelItemProduto.setBorder(BorderFactory.createTitledBorder("Item Produto"));
        painelItemProduto.setLayout(new GridBagLayout());

        //painelProduto
        painelProduto.setLayout(new FlowLayout());
        painelProduto.add(campoTipoItem);
        painelProduto.add(campoProdutoItemVenda);
        painelProduto.add(labelQuantidadeItemVenda);
        painelProduto.add(campoQuantidadeItemVenda);
        adicionaComponente(1, 1, 1, 1, painelProduto, painelItemProduto);

        painelInformacoesItem.setLayout(new FlowLayout());
        painelInformacoesItem.add(labelValorUnitarioItemVenda);
        painelInformacoesItem.add(campoValorUnitarioItemVenda);
        painelInformacoesItem.add(labelDescontoItemVenda);
        painelInformacoesItem.add(campoDescontoItemVenda);
        painelInformacoesItem.add(labelValorTotalItemVenda);
        painelInformacoesItem.add(campoValorTotalItemVenda);
        painelInformacoesItem.add(botaoAdicionar);
        painelInformacoesItem.add(botaoRemover);
        adicionaComponente(2, 1, 1, 1, painelInformacoesItem, painelItemProduto);

        adicionaComponente(8, 0, 1, 1, painelItemProduto, jpComponentes);
    }

    private void criaPainelServicoItem() {
        //painelItemServico
        painelItemServico.setBorder(BorderFactory.createTitledBorder("Item Servico"));
        painelItemServico.setLayout(new GridBagLayout());

        //painelServico
        painelServico.setLayout(new FlowLayout());
        painelServico.add(campoTipoItem);
        painelServico.add(campoServicoItemVenda);
        painelServico.add(labelQuantidadeItemVenda);
        painelServico.add(campoQuantidadeItemVenda);
        adicionaComponente(1, 1, 1, 1, painelServico, painelItemServico);

        painelInformacoesItem.setLayout(new FlowLayout());
        painelInformacoesItem.add(labelValorUnitarioItemVenda);
        painelInformacoesItem.add(campoValorUnitarioItemVenda);
        painelInformacoesItem.add(labelDescontoItemVenda);
        painelInformacoesItem.add(campoDescontoItemVenda);
        painelInformacoesItem.add(labelValorTotalItemVenda);
        painelInformacoesItem.add(campoValorTotalItemVenda);
        painelInformacoesItem.add(botaoAdicionar);
        painelInformacoesItem.add(botaoRemover);
        adicionaComponente(2, 1, 1, 1, painelInformacoesItem, painelItemServico);

        adicionaComponente(8, 0, 1, 1, painelItemServico, jpComponentes);
    }

    private void criaPainelValores() {
        //painelValores
        painelValores.setBorder(BorderFactory.createTitledBorder("Valores"));
        painelValores.setLayout(new GridBagLayout());
        adicionaComponente(1, 1, 1, 1, campoCondicaoPagamento, painelValores);
        adicionaComponente(2, 1, 1, 1, campoValorTotal, painelValores);
        adicionaComponente(3, 1, 1, 1, campoDesconto, painelValores);
        adicionaComponente(4, 1, 1, 1, campoValorLiquido, painelValores);
        adicionaComponente(11, 1, 1, 1, painelValores, jpComponentes);
    }

    private boolean verificaQuantidadeItem() {
        try {
            if (campoTipoItem.getSelectedIndex() == 0) { //produto
                String tempSqlConsultar = "SELECT QUANTIDADE FROM PRODUTO WHERE ID = " + campoProdutoItemVenda.getValor();
                int varAuxiliarCodProduto = (int) campoProdutoItemVenda.getValor();
                ResultSet rs = Conexao.getConexao().createStatement().executeQuery(tempSqlConsultar);
                if (rs.next()) {
                    int varAuxiliarQuantidadeProdutoDigitado = (int) campoQuantidadeItemVenda.getValor();
                    campoQuantidadeItemVenda.setValor(rs.getString(1));
                    int varAuxiliarQuantidadeProdutoBancoDados = (int) campoQuantidadeItemVenda.getValor();
                    campoQuantidadeItemVenda.setValor(varAuxiliarQuantidadeProdutoDigitado);
                    if (varAuxiliarQuantidadeProdutoDigitado > varAuxiliarQuantidadeProdutoBancoDados) {
                        JOptionPane.showMessageDialog(null, "A quantidade em estoque disponível do produto com o código " + varAuxiliarCodProduto + " é " + varAuxiliarQuantidadeProdutoBancoDados + ". Ajuste a quantidade.");
                        jbConfirmar.setEnabled(false);
                        return true;
                    }
                    jbConfirmar.setEnabled(true);
                    return false;
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
}
