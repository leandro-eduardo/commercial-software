package telas;

import componentes.*;
import dao.DaoCompra;
import dao.DaoCondicaoPagamento;
import dao.DaoPessoa;
import dao.DaoProduto;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_ENTER;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.*;
import renderizador.*;
import tabela.TabelaItemCompra;
import tabela.TableModelItemCompra;

public class TelaCompra extends TelaCadastro {
    public static MeuCampoMonetario campoValorTotal
            = new MeuCampoMonetario(15, false, true, false, "Valor total");
    public static MeuCampoMonetario campoDesconto
            = new MeuCampoMonetario(15, true, true, false, "Valor desconto");
    public static MeuCampoMonetario campoValorLiquido
            = new MeuCampoMonetario(15, false, true, false, "Valor líquido");
    public static MeuCampoInteger campoQuantidadeItemCompra
            = new MeuCampoInteger(5, false, true, false, "Quantidade");
    public static MeuCampoMonetario campoValorUnitarioItemCompra
            = new MeuCampoMonetario(15, true, false, false, "Valor unitário");
    public static MeuCampoMonetario campoDescontoItemCompra
            = new MeuCampoMonetario(15, true, true, false, "Desconto");
    public static MeuCampoMonetario campoValorTotalItemCompra
            = new MeuCampoMonetario(15, false, true, false, "Valor total");
    public static TabelaItemCompra tabelaItemCompra = new TabelaItemCompra();
    public static TelaCompra tela;
    public ContaPagar contaPagar = new ContaPagar();
    public Compra compra = new Compra();
    public DaoCompra daoCompra = new DaoCompra(compra);
    public CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
    public DaoCondicaoPagamento daoCondicaoPagamento = new DaoCondicaoPagamento(condicaoPagamento);
    public ItemCompra itemCompra = new ItemCompra();
    public ParametrosConsulta parametrosConsulta =
            new ParametrosConsulta("Consulta de Produto",
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
    public ParametrosConsulta parametrosConsultaFornecedores =
            new ParametrosConsulta("Consulta de Fornecedor",
                    DaoPessoa.SQLPESQUISARFORNECEDORES,
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
    public ParametrosConsulta parametrosConsulta3 =
            new ParametrosConsulta("Consulta de Condição de Pagamento",
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
    public MeuCampoInteger campoCodigo
            = new MeuCampoInteger(6, false, false, false, "Código");
    public MeuCampoData campoData
            = new MeuCampoData(10, true, "Data");
    public MeuJComboBox campoStatus
            = new MeuJComboBox(false, "Status", new String[][]{{"PA", "Pedido Aberto"}, {"PC", "Pedido Cancelado"}, {"CF", "Compra Finalizada"}, {"CC", "Compra Cancelada"}});
    public MeuCampoBuscar campoFornecedor = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroPessoa.class, DaoPessoa.SQLFORNECEDORES, DaoPessoa.SQLINATIVOS, parametrosConsultaFornecedores, true, true, "Fornecedor", 30);
    public MeuCampoBuscar campoProdutoItemCompra = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroProduto.class, DaoProduto.SQLCOMBOBOX, DaoProduto.SQLINATIVOS, parametrosConsulta, false, true, "Produto", 40);
    public MeuCampoBuscar campoCondicaoPagamento = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroCondicaoPagamento.class, DaoCondicaoPagamento.SQLCOMBOBOX, DaoCondicaoPagamento.SQLINATIVOS, parametrosConsulta3, true, true, "Cond. Pagamento", 15);
    private MeuCampoCheckBox cpGerada = new MeuCampoCheckBox(false, false, "Conta gerada");
    
    private JPanel painelItemProduto = new JPanel();
    private JPanel painelInformacoesItem = new JPanel();
    private JPanel painelProduto = new JPanel();

    private JPanel painelValores = new JPanel();

    JLabel labelQuantidadeItemCompra = new JLabel("Qtde: ");
    JLabel labelValorUnitarioItemCompra = new JLabel("Valor unitário: ");
    JLabel labelDescontoItemCompra = new JLabel("Desconto: ");
    JLabel labelValorTotalItemCompra = new JLabel("Total: ");
    
    private Icon iconAdicionar = new ImageIcon(getClass().getResource("/icones/adicionar.png"));
    private Icon iconRemover = new ImageIcon(getClass().getResource("/icones/remover.png"));

    private MeuJButton botaoAdicionar = new MeuJButton(iconAdicionar, false, false ,false);
    private MeuJButton botaoRemover = new MeuJButton(iconRemover, false, false, false);
    

    public TelaCompra() {
        super("Cadastro de Compra");
        adicionaScrollPane();
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/compra.png")));
        adicionaComponente(1, 0, 1, 1, campoCodigo, jpComponentes);
        adicionaComponente(2, 0, 1, 1, campoData, jpComponentes);
        adicionaComponente(3, 0, 1, 1, campoStatus, jpComponentes);
        adicionaComponente(4, 0, 1, 1, campoFornecedor, jpComponentes);

        adicionaComponente(6, 0, 1, 1, cpGerada, jpComponentes);
        
        //tabelaItem
        adicionaComponente(7, 0, 1, 1, tabelaItemCompra, jpComponentes);
//        JPanel espacador = new JPanel();
//        espacador.setPreferredSize(new Dimension(20, 10));
//        adicionaComponente(1, 2, 1, 1, espacador, jpComponentes);

        criaPainelProdutoItem();
        criaPainelValores();
        adicionaComponenteArray(campoProdutoItemCompra);
        adicionaComponenteArray(campoQuantidadeItemCompra);
        adicionaComponenteArray(campoValorUnitarioItemCompra);
        adicionaComponenteArray(campoDescontoItemCompra);
        adicionaComponenteArray(campoValorTotalItemCompra);
        adicionaComponenteArray(botaoAdicionar);
        adicionaComponenteArray(botaoRemover);
        habilitaComponentes(false);
        setSize(1350, 630);
        //pack();
        adicionaListeners();
        centralizaTela();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCompra();
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
        BigDecimal quantidadeItem = BigDecimal.valueOf(new Double((int) campoQuantidadeItemCompra.getValor()));
        BigDecimal valorUnitarioItem = campoValorUnitarioItemCompra.getValor();
        BigDecimal descontoItem = campoDescontoItemCompra.getValor();
        BigDecimal valorTotalItem = quantidadeItem.multiply(valorUnitarioItem.subtract(descontoItem));
//        if (quemChamou != null && valorTotalItem.compareTo(BigDecimal.ZERO) == -1) {
//            JOptionPane.showMessageDialog(null, "Valor do item de compra negativo. Enquanto o valor estiver negativo não será possível confirmar a compra. Verifique!!!");
//            quemChamou.requestFocus();
//        } else if (quemChamou != null && valorLiquido.compareTo(BigDecimal.ZERO) == -1) {
//            JOptionPane.showMessageDialog(null, "Valor total da compra negativo. Enquanto o valor estiver negativo não será possível confirmar a compra. Verifique!!!");
//            quemChamou.requestFocus();
//        } else {
            campoValorTotalItemCompra.setValor(valorTotalItem);
            tabelaItemCompra.getTableModel().alteraValorUnitario(campoValorUnitarioItemCompra.getValor(), tabelaItemCompra.getLinhaSelecionada());
            tabelaItemCompra.getTableModel().alteraValorTotal(campoValorTotalItemCompra.getValor(), tabelaItemCompra.getLinhaSelecionada());
//        }

        List<ItemCompra> itens = tabelaItemCompra.getTableModel().getDados();
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (int i = 0; i < itens.size(); i++) {
            valorTotal = valorTotal.add(itens.get(i).getValorTotal());
        }
        campoValorTotal.setValor(valorTotal);
        campoValorLiquido.setValor(valorTotal.subtract(campoDesconto.getValor()));
        campoValorTotalItemCompra.setValor(valorTotalItem);
        
        //o IF abaixo valida se o campoValorLiquido ou o campoValorTotalItemCompra estão negativos. Se sim, o botao Confirmar é desabilitado para impedir o usuário de continuar.
        if (campoValorLiquido.getValor().compareTo(BigDecimal.ZERO) == -1 || campoValorTotalItemCompra.getValor().compareTo(BigDecimal.ZERO) == -1) {
            tela.jbConfirmar.setEnabled(false);
        } else {
            tela.jbConfirmar.setEnabled(true);
        }
    }

    public void setPersistencia() {
        compra.setId((int) campoCodigo.getValor());
        compra.setData(campoData.getValorDate());
        compra.setValorTotal(campoValorTotal.getValor());
        compra.setDesconto(campoDesconto.getValor());
        compra.setValorLiquido(campoValorLiquido.getValor());
        compra.setStatus((String) campoStatus.getValor());
        compra.setCpGerada(cpGerada.getValor());
        compra.getPessoa().setId((int) campoFornecedor.getValor());
        compra.getCondicaoPagamento().setId((int) campoCondicaoPagamento.getValor());
        compra.setItensCompra(((TableModelItemCompra) tabelaItemCompra.getTabela().getModel()).getDados());
    }

    public void getPersistencia() {
        campoCodigo.setValor(compra.getId());
        campoData.setValor(compra.getData());
        campoValorTotal.setValor(compra.getValorTotal());
        campoDesconto.setValor(compra.getDesconto());
        campoValorLiquido.setValor(compra.getValorLiquido());
        campoStatus.setValor(compra.getStatus());
        cpGerada.setValor(compra.isCpGerada());
        campoFornecedor.setValor(compra.getPessoa().getId());
        campoCondicaoPagamento.setValor(compra.getCondicaoPagamento().getId());
        ((TableModelItemCompra) tabelaItemCompra.getTabela().getModel()).setDados(compra.getItensCompra());
    }


    public void getTelaContaPagar() {
        TelaContaPagar telaContaPagar = new TelaContaPagar(contaPagar);
        TelaSistema.jdp.add(telaContaPagar);
        TelaSistema.jdp.moveToFront(this);
    }

    public void adicionaListeners() {
        campoProdutoItemCompra.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    if (campoProdutoItemCompra.getValor() == "" || campoProdutoItemCompra.achou == false) {

                    } else {
                        tabelaItemCompra.getTableModel().alteraProduto(campoProdutoItemCompra.getValor(), tabelaItemCompra.getLinhaSelecionada());
                        Produto produto = tabelaItemCompra.getTableModel().getProduto(tabelaItemCompra.getLinhaSelecionada());
                        System.out.println(produto.getPrecocompra());
                        campoValorUnitarioItemCompra.setValor(produto.getPrecocompra());
                        atualizaCamposTotal(null);
                    }
                }
            }
        });
        campoProdutoItemCompra.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (campoProdutoItemCompra.eVazio()) {
                            campoQuantidadeItemCompra.limpar();
                            tabelaItemCompra.getTableModel().alteraProduto(0, tabelaItemCompra.getLinhaSelecionada());
                            tabelaItemCompra.getTableModel().alteraQuantidade(campoQuantidadeItemCompra.getValor(), tabelaItemCompra.getLinhaSelecionada());
                            campoValorUnitarioItemCompra.limpar();
                            campoDescontoItemCompra.limpar();
                            campoValorTotalItemCompra.limpar();
                        }
                        atualizaCamposTotal(null);
                    }
                });
            }
        });

        campoQuantidadeItemCompra.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        tabelaItemCompra.getTableModel().alteraQuantidade(campoQuantidadeItemCompra.getValor(), tabelaItemCompra.getLinhaSelecionada());
                        atualizaCamposTotal(null);
                    }
                });
            }
        });
        campoValorUnitarioItemCompra.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        tabelaItemCompra.getTableModel().alteraValorUnitario(campoValorUnitarioItemCompra.getValor(), tabelaItemCompra.getLinhaSelecionada());
                        atualizaCamposTotal(null);
                    }
                });
            }
        });
        campoDescontoItemCompra.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        BigDecimal quantidadeItem = BigDecimal.valueOf(new Double((int) campoQuantidadeItemCompra.getValor()));
                        BigDecimal valorUnitarioItem = campoValorUnitarioItemCompra.getValor();
                        BigDecimal descontoItem = campoDescontoItemCompra.getValor();
                        BigDecimal valorTotalItem = quantidadeItem.multiply(valorUnitarioItem.subtract(descontoItem));
                        campoValorTotalItemCompra.setValor(valorTotalItem);
                        tabelaItemCompra.getTableModel().alteraValorUnitario(campoValorUnitarioItemCompra.getValor(), tabelaItemCompra.getLinhaSelecionada());
                        tabelaItemCompra.getTableModel().alteraValorTotal(campoValorTotalItemCompra.getValor(), tabelaItemCompra.getLinhaSelecionada());
                        tabelaItemCompra.getTableModel().alteraDesconto(campoDescontoItemCompra.getValor(), tabelaItemCompra.getLinhaSelecionada());
                        atualizaCamposTotal(campoDescontoItemCompra);
                    }
                });
            }
        });
        campoDescontoItemCompra.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {

            } 
            @Override
            public void focusLost(FocusEvent fe) {               
                BigDecimal quantidadeItem = BigDecimal.valueOf(new Double((int) campoQuantidadeItemCompra.getValor()));
                BigDecimal valorUnitarioItem = campoValorUnitarioItemCompra.getValor();
                BigDecimal descontoItem = campoDescontoItemCompra.getValor();
                BigDecimal valorTotalItem = quantidadeItem.multiply(valorUnitarioItem.subtract(descontoItem));
                if (valorTotalItem.compareTo(BigDecimal.ZERO) == -1) {
                    JOptionPane.showMessageDialog(null, "Valor do item de compra negativo. Enquanto o valor estiver negativo não será possível confirmar a compra. Verifique!!!");
                    campoDescontoItemCompra.requestFocus();
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
                    JOptionPane.showMessageDialog(null, "Valor total da compra negativo. Enquanto o valor estiver negativo não será possível confirmar a compra. Verifique!!!");
                    campoDesconto.requestFocus();
                }
            }
        });

        tabelaItemCompra.getTabela().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting() && tabelaItemCompra.getTabela().getSelectedRow() >= 0) {
                    if (tabelaItemCompra.getTableModel().getValueAt(tabelaItemCompra.getLinhaSelecionada(), 0) == "") {
                        campoProdutoItemCompra.limpar();
                    }
                    ItemCompra itemCompra = tabelaItemCompra.getTableModel().getItemCompra(tabelaItemCompra.getTabela().getSelectedRow());
                    campoProdutoItemCompra.setValor(itemCompra.getProduto().getId());
                    campoQuantidadeItemCompra.setValor(itemCompra.getQuantidade());
                    campoValorUnitarioItemCompra.setValor(itemCompra.getValorUnitario());
                    campoDescontoItemCompra.setValor(itemCompra.getDesconto());
                    campoValorTotalItemCompra.setValor(itemCompra.getValorTotal());
                    atualizaCamposTotal(null);
                }
            }
        });
        
        botaoAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                campoProdutoItemCompra.limpar();
                TabelaItemCompra.tmi.adicionaLinha();
                TabelaItemCompra.tabela.addRowSelectionInterval(TabelaItemCompra.tmi.getRowCount() - 1, TabelaItemCompra.tmi.getRowCount() - 1);
            }
        });
        botaoRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (TabelaItemCompra.tabela.getSelectedRow() >= 0) {
                    if (TabelaItemCompra.tmi.getRowCount() > 1) {
                        TabelaItemCompra.tmi.removeLinha(TabelaItemCompra.tabela.getSelectedRow());
                        TabelaItemCompra.tabela.addRowSelectionInterval(TabelaItemCompra.tmi.getRowCount() - 1, TabelaItemCompra.tmi.getRowCount() - 1);
                        TelaVenda.atualizaCamposTotal(null);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Selecione uma linha para poder excluí-la.");
                }
            }
        });
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
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
        List<ItemCompra> itens = ((TableModelItemCompra) tabelaItemCompra.getTabela().getModel()).getDados();
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getProduto().getId() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Existe(m) produto(s) não informado(s).");
                return false;
            }
            if (itens.get(i).getQuantidade() <= 0) {
                JOptionPane.showMessageDialog(null,
                        "Existe(m) produto(s) com quantidade inválida(s).");
                return false;
            }
            if (itens.get(i).getValorUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(null,
                        "Existe(m) produto(s) com valor unitário invalido(s).");
                return false;
            }
            if (itens.get(i).getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(null,
                        "Existe(m) produto(s) com valor total negativo.");
                return false;
            }
        }
        return true;
    }

    @Override
    public void habilitaBotoes() {
        super.habilitaBotoes();
        if ((campoStatus != null && campoStatus.getValor().equals("CF")) || (campoStatus != null && campoStatus.getValor().equals("CC")) || (campoStatus != null && campoStatus.getValor().equals("PC"))) {
            jbAlterar.setEnabled(false);
            jbExcluir.setEnabled(false);
        }
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno = daoCompra.incluir();
        consultarBD(compra.getId());
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoCompra.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoCompra.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        compra.setId(pk);
        daoCompra.consultar();
        getPersistencia();
        super.consultarBD(pk);
    }

    @Override
    public void incluir() {
        super.incluir();
        campoData.setValor(new Date());
    }

    @Override
    public void alterar() {
        super.alterar();
        tabelaItemCompra.getTabela().addRowSelectionInterval(0, 0);
    }

    @Override
    public void cancelar() {
        super.cancelar();
        tabelaItemCompra.limparCancelar();
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Compra",
                DaoCompra.SQLPESQUISAR,
                new String[]{"Código", "Fornecedor", "Data", "Valor", "Status", "CP Gerada"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Fornecedor", "NOMERAZAOSOCIAL", String.class),
                        new FiltroPesquisa("Data", "DATA", Date.class),
                        new FiltroPesquisa("Valor", "VALORLIQUIDO", String.class),
                        new FiltroPesquisa("Status", "STATUS", String.class),
                        new FiltroPesquisa("CP Gerada", "CPGERADA", String.class),
                },
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
        painelProduto.add(campoProdutoItemCompra);
        painelProduto.add(labelQuantidadeItemCompra);
        painelProduto.add(campoQuantidadeItemCompra);
        adicionaComponente(1, 1, 1, 1, painelProduto, painelItemProduto);

        painelInformacoesItem.setLayout(new FlowLayout());
        painelInformacoesItem.add(labelValorUnitarioItemCompra);
        painelInformacoesItem.add(campoValorUnitarioItemCompra);
        painelInformacoesItem.add(labelDescontoItemCompra);
        painelInformacoesItem.add(campoDescontoItemCompra);
        painelInformacoesItem.add(labelValorTotalItemCompra);
        painelInformacoesItem.add(campoValorTotalItemCompra);
        painelInformacoesItem.add(botaoAdicionar);
        painelInformacoesItem.add(botaoRemover);
        adicionaComponente(2, 1, 1, 1, painelInformacoesItem, painelItemProduto);

        adicionaComponente(5, 0, 1, 1, painelItemProduto, jpComponentes);
    }
    
    private void criaPainelValores() {
        //painelValores
        painelValores.setBorder(BorderFactory.createTitledBorder("Valores"));
        painelValores.setLayout(new GridBagLayout());
        adicionaComponente(1, 1, 1, 1, campoCondicaoPagamento, painelValores);
        adicionaComponente(2, 1, 1, 1, campoValorTotal, painelValores);
        adicionaComponente(3, 1, 1, 1, campoDesconto, painelValores);
        adicionaComponente(4, 1, 1, 1, campoValorLiquido, painelValores);
        adicionaComponente(7, 1, 1, 1, painelValores, jpComponentes);

    }
    
}
