package telas;

import componentes.*;
import dao.DaoCompra;
import dao.DaoCondicaoPagamento;
import dao.DaoContaPagar;
import dao.DaoPessoa;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_ENTER;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.Compra;
import pojo.ContaPagar;
import pojo.ParametrosConsulta;
import pojo.ParcelaContaPagar;
import renderizador.*;
import tabela.TabelaParcelaContaPagar;
import tabela.TableModelParcelaContaPagar;

public class TelaContaPagar extends TelaCadastro {

    public static MeuCampoMonetario campoValorLiquido = new MeuCampoMonetario(8, true, true, false, "Valor líquido");
    public static TelaContaPagar tela;
    public ParametrosConsulta parametrosConsulta =
            new ParametrosConsulta("Consulta de Compra",
                    DaoCompra.SQLPESQUISAR2,
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
                    this, TelaContaPagar.class, true, false, false
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
                            new RenderizadorTexto(), new RenderizadorTexto(),
                    },
                    this, TelaContaPagar.class, false, false, false
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
    public MeuCampoData campoData = new MeuCampoData(10, false, "Data");
    public MeuCampoBuscar campoCompra = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCompra.class, DaoCompra.SQLCOMBOBOX, DaoCompra.SQLINATIVOS, parametrosConsulta, false, true, "Compra", 15);
    public MeuCampoBuscar campoPessoa = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroPessoa.class, DaoPessoa.SQLFORNECEDORES, DaoPessoa.SQLINATIVOS, parametrosConsultaFornecedores, true, true, "Fornecedor", 30);
    public MeuCampoBuscar campoCondicaoPagamento = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroCondicaoPagamento.class, DaoCondicaoPagamento.SQLCOMBOBOX, DaoCondicaoPagamento.SQLINATIVOS, parametrosConsulta3, true, true, "Cond. Pagamento", 15);
    public MeuCampoInteger campoParcelaID = new MeuCampoInteger(5, false, false, true, "ID");
    public MeuCampoData campoVencimento = new MeuCampoData(10, false, "Vencimento");
    public MeuCampoMonetario campoValorParcela = new MeuCampoMonetario(15, true, true, true, "Valor");
    public MeuCampoMonetario campoValorPendenteParcela = new MeuCampoMonetario(15, true, true, true, "Valor Pendente");
    public MeuCampoCheckBox campoQuitadaParcela = new MeuCampoCheckBox(false, true, "Quitada");
    public JButton botaoGerarParcelas = new JButton("Gerar Parcelas");
    public TabelaParcelaContaPagar tabelaParcela = new TabelaParcelaContaPagar();
    private ContaPagar contaPagar = new ContaPagar();
    private DaoContaPagar daoContaPagar = new DaoContaPagar(contaPagar);
    public static MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuCampoMonetario campoValorTotal = new MeuCampoMonetario(8, true, false, false, "Valor total");
    public static MeuCampoMonetario campoDesconto = new MeuCampoMonetario(8, true, false, false, "Desconto");
    private MeuCampoMonetario campoValorPendente = new MeuCampoMonetario(8, true, true, false, "Valor pendente");
    private MeuCampoCheckBox campoQuitada = new MeuCampoCheckBox(false, true, "Quitada");
    public static MeuJTextArea campoDescricao = new MeuJTextArea("Descrição", false, 400, 50);
    private JPanel painelStatus = new JPanel();

    public TelaContaPagar() {
        super("Cadastro de Conta a Pagar");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/contapagar.png")));
        
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(1, 2, 0, 0, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoQuitada, painelStatus);

        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        adicionaComponente(2, 1, 1, 1, campoData, jpComponentes);
        adicionaComponente(3, 1, 1, 1, campoPessoa, jpComponentes);
        adicionaComponente(4, 1, 1, 1, campoCompra, jpComponentes);
        adicionaComponente(5, 1, 1, 1, campoValorTotal, jpComponentes);
        adicionaComponente(6, 1, 1, 1, campoDesconto, jpComponentes);
        adicionaComponente(7, 1, 1, 1, campoValorLiquido, jpComponentes);
        adicionaComponente(8, 1, 1, 1, campoValorPendente, jpComponentes);
        adicionaComponente(9, 1, 1, 1, campoCondicaoPagamento, jpComponentes);
        adicionaComponente(10, 1, 1, 1, campoDescricao, jpComponentes);
        adicionaComponente(11, 1, 1, 1, botaoGerarParcelas, jpComponentes);
        adicionaComponente(12, 1, 1, 6, tabelaParcela, jpComponentes);

        botaoGerarParcelas.setEnabled(false);
        campoPessoa.editar(true);
        campoCompra.editar(true);
        campoPessoa.editar(true);
        campoCondicaoPagamento.editar(true);
        campoValorTotal.setEditable(true);
        campoDesconto.setEditable(true);
        campoValorLiquido.setEditable(true);
        campoValorPendente.setEditable(true);
        adicionaListeners();
        adicionaScrollPane();
        habilitaComponentes(false);
        //setMaximumSize(new Dimension(getWidth(), getHeight()));
        //pack();
        setSize(800, 630);    
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();
    }

    public TelaContaPagar(ContaPagar contaPagar) {
        this();
        this.contaPagar = contaPagar;
        getPersistencia();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaContaPagar();
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
        contaPagar.setId((Integer) campoCodigo.getValor());
        contaPagar.setData(campoData.getValorDate());
        contaPagar.setValortotal(campoValorTotal.getValor());
        contaPagar.setDesconto(campoDesconto.getValor());
        contaPagar.setValorliquido(campoValorLiquido.getValor());
        contaPagar.setValorpendente(campoValorPendente.getValor());
        contaPagar.setQuitada(campoQuitada.getValor());
        contaPagar.setDescricao(campoDescricao.getValor());
        contaPagar.getCompra().setId((Integer) campoCompra.getValor());
        contaPagar.getPessoa().setId((Integer) campoPessoa.getValor());
        contaPagar.getCondicaoPagamento().setId((Integer) campoCondicaoPagamento.getValor());
        contaPagar.setItensContaPagar(((TableModelParcelaContaPagar) tabelaParcela.getTabela().getModel()).getDados());
    }

    public void getPersistencia() {
        campoCodigo.setValor(contaPagar.getId());
        campoData.setValor(contaPagar.getData());
        campoValorTotal.setValor(contaPagar.getValorliquido());
        campoDesconto.setValor(contaPagar.getDesconto());
        campoValorLiquido.setValor(contaPagar.getValorliquido());
        campoValorPendente.setValor(contaPagar.getValorpendente());
        campoQuitada.setValor(contaPagar.isQuitada());
        campoDescricao.setValor(contaPagar.getDescricao());
        campoCompra.setValor(contaPagar.getCompra().getId());
        campoPessoa.setValor(contaPagar.getPessoa().getId());
        campoCondicaoPagamento.setValor(contaPagar.getCondicaoPagamento().getId());
        ((TableModelParcelaContaPagar) tabelaParcela.getTabela().getModel()).setDados(contaPagar.getItensContaPagar());
    }

    public void calcular() {
        BigDecimal desconto = campoDesconto.getValor();
        BigDecimal valorTotal = campoValorTotal.getValor().subtract(desconto);
        campoValorLiquido.setValor(valorTotal);
        campoValorPendente.setValor(valorTotal);
    }

    public void adicionaListeners() {
        campoCompra.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    if (campoCompra.achou == true) {
                        preencherDados();
                        campoDescricao.setValor("DOC. DE COMPRA REFERÊNCIA " + campoCompra.getValor());
//                        campoPessoa.editar(false); //rotina trocada pelo método preencherDados(), pois faz a mesma função do método.
//
//                        Compra compra = new Compra();
//                        DaoCompra daoCompra = new DaoCompra(compra);
//                        compra.setId((int) campoCompra.getValor());
//                        daoCompra.consultar();
//                        campoPessoa.setValor(compra.getPessoa().getId());
//                        campoCondicaoPagamento.setValor(compra.getCondicaoPagamento().getId());
//                        campoValorTotal.setValor(compra.getValorTotal());
//                        campoDesconto.setValor(compra.getDesconto());
//                        campoValorLiquido.setValor(compra.getValorLiquido());
//                        campoValorPendente.setValor(compra.getValorLiquido());
//                        campoPessoa.editar(false);
//                        campoCondicaoPagamento.editar(false);
//                        campoValorTotal.setEditable(false);
//                        campoDesconto.setEditable(false);
//                        campoValorLiquido.setEditable(false);
//                        campoValorPendente.setEditable(false);
                    } else {
                        campoPessoa.limpar();
                        campoCondicaoPagamento.limpar();
                        campoValorTotal.limpar();
                        campoDesconto.limpar();
                        campoValorLiquido.limpar();
                        campoValorPendente.limpar();
                        campoPessoa.editar(true);
                        campoCondicaoPagamento.editar(true);
                        campoValorTotal.setEditable(true);
                        campoDesconto.setEditable(true);
                        campoValorLiquido.setEditable(true);
                        campoValorPendente.setEditable(true);
                    }
                }
            }
        });


        campoCompra.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (campoCompra.eVazio()) {
                            campoPessoa.setEnabled(true);
                            campoPessoa.limpar();
                            campoCondicaoPagamento.limpar();
                            campoValorTotal.limpar();
                            campoValorLiquido.limpar();
                            campoValorPendente.limpar();
                            ((TableModelParcelaContaPagar) tabelaParcela.getTabela().getModel()).limparDados();
                            campoPessoa.editar(true);
                            campoCondicaoPagamento.editar(true);
                            campoValorTotal.setEditable(true);
                            campoDesconto.setEditable(true);
                            campoValorLiquido.setEditable(true);
                            campoValorPendente.setEditable(true);
                        }
                    }
                });
            }
        });

        campoPessoa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    if (campoPessoa.achou == true) {
                        campoCompra.editar(false);
                    } else {
                        campoCompra.editar(true);
                    }
                }
            }
        });

        campoPessoa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (campoPessoa.eVazio()) {
                            campoCompra.setEnabled(true);
                            campoCompra.editar(true);
                        }
                    }
                });
            }
        });


//        campoCompra.addFocusListener(new FocusListener() { //Utilizava antes de passar o KeyListener (new KeyAdapter) - (ANTIGO)
//            @Override
//            public void focusGained(FocusEvent e) {
//            }
//
//            @Override
//            public void focusLost(FocusEvent e) {
//                if (!campoCompra.getValor().equals("")) {
//                    campoPessoa.editar(false);
//                    Compra compra = new Compra();
//                    DaoCompra daoCompra = new DaoCompra(compra);
//                    compra.setId((int) campoCompra.getValor());
//                    daoCompra.consultar();
//                    campoPessoa.setValor(compra.getPessoa().getId());
//                    campoCondicaoPagamento.setValor(compra.getCondicaoPagamento().getId());
//                    campoValorTotal.setValor(compra.getValorLiquido());
//                    campoValorLiquido.setValor(compra.getValorLiquido());
//                    campoValorPendente.setValor(compra.getValorLiquido());
//                } else {
//                    campoPessoa.limpar();
//                    campoCondicaoPagamento.limpar();
//                    campoValorTotal.limpar();
//                    campoValorLiquido.limpar();
//                    campoValorPendente.limpar();
//                    campoPessoa.editar(true);
//                }
//            }
//        });

        botaoGerarParcelas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                BigDecimal valorTotal = campoValorTotal.getValor();
                if (valorTotal.compareTo(BigDecimal.ZERO) == 0) {
                    JOptionPane.showMessageDialog(null, "Valores não definidos.");
                } else if (campoCondicaoPagamento.eVazio()) {
                    JOptionPane.showMessageDialog(null, "Informe uma condição de pagamento.");
                } else {
                    ((TableModelParcelaContaPagar) tabelaParcela.getTabela().getModel()).limparDados();
                    ((TableModelParcelaContaPagar) tabelaParcela.getTabela().getModel()).gerarParcelas((int) campoCondicaoPagamento.getValor());
                }
            }
        });

        campoValorTotal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        calcular();
                    }
                });
            }
        });
        campoDesconto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        calcular();
                    }
                });
            }
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
//      if (super.verificarCampos() == false) {  //Esta linha faz exatamente o mesmo do que a linha abaixo.
        if (!super.validaComponentes()) {
            return false;
        }
        List<ParcelaContaPagar> itens = ((TableModelParcelaContaPagar) tabelaParcela.getTabela().getModel()).getDados();
        for (int i = 0; i < itens.size(); i++) {
            //if (itens.get(i).getId() == 0) {
            if (itens.size() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Nenhuma parcela foi gerada.");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        getPersistencia();
        boolean retorno = daoContaPagar.incluir();
        consultarBD(contaPagar.getId());
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoContaPagar.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoContaPagar.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        contaPagar.setId(pk);
        daoContaPagar.consultar();
        getPersistencia();
    }

    @Override
    public void incluir() {
        super.incluir();
        botaoGerarParcelas.setEnabled(true);
        campoData.setValor(new Date());
        campoPessoa.editar(true);
        campoPessoa.limpar();
        campoCondicaoPagamento.limpar();
        campoValorTotal.limpar();
        campoValorLiquido.limpar();
        campoValorPendente.limpar();
        campoPessoa.editar(true);
        campoCondicaoPagamento.editar(true);
        campoValorTotal.setEditable(true);
        campoDesconto.setEditable(true);
        campoValorLiquido.setEditable(true);
        campoValorPendente.setEditable(true);
    }

    @Override
    public void alterar() {
        super.alterar();
        botaoGerarParcelas.setEnabled(true);
    }

    @Override
    public void cancelar() {
        super.cancelar();
        botaoGerarParcelas.setEnabled(false);
        ((TableModelParcelaContaPagar) tabelaParcela.getTabela().getModel()).limparDados();
    }

    @Override
    public void limpar() {
        super.limpar();
        botaoGerarParcelas.setEnabled(false);
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Conta a Pagar",
                DaoContaPagar.SQLPESQUISAR,
                new String[]{"Código", "Nome", "Compra", "Data", "Valor", "Pagamento", "Quitada"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "PESSOA_NOME", String.class),
                        new FiltroPesquisa("Compra", "COMPRA_ID", String.class),
                        new FiltroPesquisa("Data", "DATA", Date.class),
                        new FiltroPesquisa("Situação", "QUITADA", String.class),
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new InteiroRender(),
                        new CellRendererData(), new MonetarioRender(), new RenderizadorStatus(), new RenderizadorStatus()},
                this, TelaConsultaFiltro.class, true, false, false)
        );
    }

    public void preencherDados() {
        if (!campoCompra.getValor().equals(0)) {
            campoPessoa.editar(false);
            Compra compra = new Compra();
            DaoCompra daoCompra = new DaoCompra(compra);
            compra.setId((int) campoCompra.getValor());
            daoCompra.consultar();
            campoPessoa.setValor(compra.getPessoa().getId());
            campoCondicaoPagamento.setValor(compra.getCondicaoPagamento().getId());
            campoValorTotal.setValor(compra.getValorTotal());
            campoDesconto.setValor(compra.getDesconto());
            campoValorLiquido.setValor(compra.getValorLiquido());
            campoValorPendente.setValor(compra.getValorLiquido());
            campoCompra.habilitar(true);
            campoPessoa.setEnabled(true);
            campoCondicaoPagamento.editar(false);
            campoValorTotal.setEditable(false);
            campoDesconto.setEditable(false);
            campoValorLiquido.setEditable(false);
            campoValorPendente.setEditable(false);
        } else {
            campoCondicaoPagamento.limpar();
            campoValorTotal.limpar();
            campoValorLiquido.limpar();
            campoValorPendente.limpar();
            campoPessoa.editar(true);
            //campoCompra.habilitar(false);
        }
    }

}
    

