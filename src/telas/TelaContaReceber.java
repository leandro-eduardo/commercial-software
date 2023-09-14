package telas;

import componentes.*;
import dao.DaoCondicaoPagamento;
import dao.DaoContaReceber;
import dao.DaoPessoa;
import dao.DaoVenda;
import java.awt.*;
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
import pojo.ContaReceber;
import pojo.ParametrosConsulta;
import pojo.ParcelaContaReceber;
import pojo.Venda;
import renderizador.*;
import tabela.TabelaParcelaContaReceber;
import tabela.TableModelParcelaContaReceber;

public class TelaContaReceber extends TelaCadastro {

    public static MeuCampoMonetario campoValorLiquido = new MeuCampoMonetario(8, true, true, false, "Valor líquido");
    public static TelaContaReceber tela;
    public ParametrosConsulta parametrosConsulta
            = new ParametrosConsulta("Consulta de Venda",
                    DaoVenda.SQLPESQUISAR2,
                    new String[]{"Código", "Cliente", "Data", "Valor", "Status", "CR Gerada"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Cliente", "NOMERAZAOSOCIAL", String.class),
                        new FiltroPesquisa("Data", "DATA", Date.class),
                        new FiltroPesquisa("Valor", "VALORLIQUIDO", String.class),
                        new FiltroPesquisa("Status", "STATUS", String.class),
                        new FiltroPesquisa("CR Gerada", "CRGERADA", String.class),},
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new CellRendererData(),
                        new MonetarioRender(), new RenderizadorStatus(), new RenderizadorStatus()},
                    this, TelaContaReceber.class, true, false, false
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
                        new RenderizadorTexto(), new RenderizadorTexto(),},
                    this, TelaContaReceber.class, false, false, false
            );
    public ParametrosConsulta parametrosConsulta3
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
    public MeuCampoData campoData = new MeuCampoData(10, false, "Data");
    public MeuCampoBuscar campoVenda = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaVenda.class, DaoVenda.SQLCOMBOBOX, DaoVenda.SQLINATIVOS, parametrosConsulta, false, true, "Venda", 15);
    public MeuCampoBuscar campoPessoa = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroPessoa.class, DaoPessoa.SQLCLIENTES, DaoPessoa.SQLINATIVOS, parametrosConsultaClientes, true, true, "Cliente", 30);
    public MeuCampoBuscar campoCondicaoPagamento = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroCondicaoPagamento.class, DaoCondicaoPagamento.SQLCOMBOBOX, DaoCondicaoPagamento.SQLINATIVOS, parametrosConsulta3, true, true, "Cond. Pagamento", 15);
    public MeuCampoInteger campoParcelaID = new MeuCampoInteger(5, false, false, true, "ID");
    public MeuCampoData campoVencimento = new MeuCampoData(10, false, "Vencimento");
    public MeuCampoMonetario campoValorParcela = new MeuCampoMonetario(15, true, true, true, "Valor");
    public MeuCampoMonetario campoValorPendenteParcela = new MeuCampoMonetario(15, true, true, true, "Valor Pendente");
    public MeuCampoCheckBox campoQuitadaParcela = new MeuCampoCheckBox(false, true, "Quitada");
    public JButton botaoGerarParcelas = new JButton("Gerar Parcelas");
    public TabelaParcelaContaReceber tabelaParcela = new TabelaParcelaContaReceber();
    private ContaReceber contaReceber = new ContaReceber();
    private DaoContaReceber daoContaReceber = new DaoContaReceber(contaReceber);
    public static MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuCampoMonetario campoValorTotal = new MeuCampoMonetario(8, true, false, false, "Valor total");
    public static MeuCampoMonetario campoDesconto = new MeuCampoMonetario(8, true, false, false, "Desconto");
    private MeuCampoMonetario campoValorPendente = new MeuCampoMonetario(8, true, true, false, "Valor pendente");
    private MeuCampoCheckBox campoQuitada = new MeuCampoCheckBox(false, true, "Quitada");
    public static MeuJTextArea campoDescricao = new MeuJTextArea("Descrição", false, 400, 50);
    private JPanel painelStatus = new JPanel();

    public TelaContaReceber() {
        super("Cadastro de Conta a Receber");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/contapagar.png")));
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(1, 5, 0, 0, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoQuitada, painelStatus);

        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        adicionaComponente(2, 1, 1, 1, campoData, jpComponentes);
        adicionaComponente(3, 1, 1, 1, campoPessoa, jpComponentes);
        adicionaComponente(4, 1, 1, 1, campoVenda, jpComponentes);
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
        campoVenda.editar(true);
        campoPessoa.editar(true);
        campoCondicaoPagamento.editar(true);
        campoValorTotal.setEditable(true);
        campoDesconto.setEditable(true);
        campoValorLiquido.setEditable(true);
        campoValorPendente.setEditable(true);
        adicionaListeners();
        adicionaScrollPane();
        habilitaComponentes(false);
        setSize(800, 630);
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMaximumSize(new Dimension(getWidth(), getHeight()));
        //pack();
        centralizaTela();
    }

    public TelaContaReceber(ContaReceber contaReceber) {
        this();
        this.contaReceber = contaReceber;
        getPersistencia();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaContaReceber();
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
        contaReceber.setId((Integer) campoCodigo.getValor());
        contaReceber.setData(campoData.getValorDate());
        contaReceber.setValortotal(campoValorTotal.getValor());
        contaReceber.setDesconto(campoDesconto.getValor());
        contaReceber.setValorliquido(campoValorLiquido.getValor());
        contaReceber.setValorpendente(campoValorPendente.getValor());
        contaReceber.setQuitada(campoQuitada.getValor());
        contaReceber.setDescricao(campoDescricao.getValor());
        contaReceber.getVenda().setId((Integer) campoVenda.getValor());
        contaReceber.getPessoa().setId((Integer) campoPessoa.getValor());
        contaReceber.getCondicaoPagamento().setId((Integer) campoCondicaoPagamento.getValor());
        contaReceber.setItensContaReceber(((TableModelParcelaContaReceber) tabelaParcela.getTabela().getModel()).getDados());
    }

    public void getPersistencia() {
        campoCodigo.setValor(contaReceber.getId());
        campoData.setValor(contaReceber.getData());
        campoValorTotal.setValor(contaReceber.getValorliquido());
        campoDesconto.setValor(contaReceber.getDesconto());
        campoValorLiquido.setValor(contaReceber.getValorliquido());
        campoValorPendente.setValor(contaReceber.getValorpendente());
        campoQuitada.setValor(contaReceber.isQuitada());
        campoDescricao.setValor(contaReceber.getDescricao());
        campoVenda.setValor(contaReceber.getVenda().getId());
        campoPessoa.setValor(contaReceber.getPessoa().getId());
        campoCondicaoPagamento.setValor(contaReceber.getCondicaoPagamento().getId());
        ((TableModelParcelaContaReceber) tabelaParcela.getTabela().getModel()).setDados(contaReceber.getItensContaReceber());
    }

    public void calcular() {
        BigDecimal desconto = campoDesconto.getValor();
        BigDecimal valorTotal = campoValorTotal.getValor().subtract(desconto);
        campoValorLiquido.setValor(valorTotal);
        campoValorPendente.setValor(valorTotal);
    }

    public void adicionaListeners() {
        campoVenda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    if (campoVenda.achou == true) {
                        preencherDados();
                        campoDescricao.setValor("DOC. DE VENDA REFERÊNCIA " + campoVenda.getValor());
//                        campoPessoa.editar(false);
//
//                        Venda venda = new Venda();
//                        DaoVenda daoVenda = new DaoVenda(venda);
//                        venda.setId((int) campoVenda.getValor());
//                        daoVenda.consultar();
//                        campoPessoa.setValor(venda.getPessoa().getId());
//                        campoCondicaoPagamento.setValor(venda.getCondicaoPagamento().getId());
//                        campoValorTotal.setValor(venda.getValorLiquido());
//                        campoValorLiquido.setValor(venda.getValorLiquido());
//                        campoValorPendente.setValor(venda.getValorLiquido());
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

        campoVenda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (campoVenda.eVazio()) {
                            campoDescricao.limpar();
                            campoPessoa.setEnabled(true);
                            campoPessoa.limpar();
                            campoCondicaoPagamento.limpar();
                            campoValorTotal.limpar();
                            campoDesconto.limpar();
                            campoValorLiquido.limpar();
                            campoValorPendente.limpar();
                            ((TableModelParcelaContaReceber) tabelaParcela.getTabela().getModel()).limparDados();
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
                        campoVenda.editar(false);
                    } else {
                        campoVenda.editar(true);
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
                            campoVenda.setEnabled(true);
                            campoVenda.editar(true);
                        }
                    }
                });
            }
        });

//        campoVenda.addFocusListener(new FocusListener() { //Utilizava antes de passar o KeyListener (new KeyAdapter) - (ANTIGO)
//            @Override
//            public void focusGained(FocusEvent e) {
//            }
//
//            @Override
//            public void focusLost(FocusEvent e) {
//                if (!campoVenda.getValor().equals("")) {
//                    campoPessoa.editar(false);
//                    Venda venda = new Venda();
//                    DaoVenda daoVenda = new DaoVenda(venda);
//                    venda.setId((int) campoVenda.getValor());
//                    daoVenda.consultar();
//                    campoPessoa.setValor(venda.getPessoa().getId());
//                    campoCondicaoPagamento.setValor(venda.getCondicaoPagamento().getId());
//                    campoValorTotal.setValor(venda.getValorLiquido());
//                    campoValorLiquido.setValor(venda.getValorLiquido());
//                    campoValorPendente.setValor(venda.getValorLiquido());
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
                    ((TableModelParcelaContaReceber) tabelaParcela.getTabela().getModel()).limparDados();
                    ((TableModelParcelaContaReceber) tabelaParcela.getTabela().getModel()).gerarParcelas((int) campoCondicaoPagamento.getValor());
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
        List<ParcelaContaReceber> itens = ((TableModelParcelaContaReceber) tabelaParcela.getTabela().getModel()).getDados();
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
        boolean retorno = daoContaReceber.incluir();
        consultarBD(contaReceber.getId());
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoContaReceber.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoContaReceber.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        contaReceber.setId(pk);
        daoContaReceber.consultar();
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
        ((TableModelParcelaContaReceber) tabelaParcela.getTabela().getModel()).limparDados();
    }

    @Override
    public void limpar() {
        super.limpar();
        botaoGerarParcelas.setEnabled(false);
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Conta a Receber",
                DaoContaReceber.SQLPESQUISAR,
                new String[]{"Código", "Nome", "Venda/OS", "Data", "Valor", "Pagamento", "Quitada"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                    new FiltroPesquisa("Nome", "PESSOA_NOME", String.class),
                    new FiltroPesquisa("Venda", "COMPRA_ID", String.class),
                    new FiltroPesquisa("Data", "DATA", Date.class),
                    new FiltroPesquisa("Situação", "QUITADA", String.class),},
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new InteiroRender(),
                    new CellRendererData(), new MonetarioRender(), new RenderizadorStatus(), new RenderizadorStatus()},
                this, TelaConsultaFiltro.class, true, false, false)
        );
    }

    public void preencherDados() {
        if (!campoVenda.getValor().equals(0)) {
            campoPessoa.editar(false);
            Venda venda = new Venda();
            DaoVenda daoVenda = new DaoVenda(venda);
            venda.setId((int) campoVenda.getValor());
            daoVenda.consultar();
            campoPessoa.setValor(venda.getPessoa().getId());
            campoCondicaoPagamento.setValor(venda.getCondicaoPagamento().getId());
            campoValorTotal.setValor(venda.getValorTotal());
            campoDesconto.setValor(venda.getDesconto());
            campoValorLiquido.setValor(venda.getValorLiquido());
            campoValorPendente.setValor(venda.getValorLiquido());
            campoVenda.habilitar(true);
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
            //campoVenda.habilitar(false);
        }
    }
}
