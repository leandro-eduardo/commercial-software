package telas;

import bd.Conexao;
import componentes.*;
import dao.DaoCaixa;
import dao.DaoContaPagar;
import dao.DaoPagamento;
import dao.DaoParcelaContaPagar;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_ENTER;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.Caixa;
import pojo.ContaPagar;
import pojo.Pagamento;
import pojo.ParametrosConsulta;
import pojo.ParcelaContaPagar;
import renderizador.*;

public class TelaPagamento extends TelaCadastro {

    public static TelaPagamento tela;
    private Pagamento pagamento = new Pagamento();
    private DaoPagamento daoPagamento = new DaoPagamento(pagamento);
    
    BigDecimal valorPendente = BigDecimal.ZERO;
    Integer auxiliarIDCaixaAberto;
    
    public ParametrosConsulta parametrosConsulta =
            new ParametrosConsulta("Contas a Pagar - Efetuar Pagamento",
                DaoParcelaContaPagar.SQL_PESQUISAR,
                new String[]{"Código", "Fornecedor", "Nº Conta", "Data Conta", "Data Vcto", "Parcela", "Valor Conta", "Valor em Aberto"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Fornecedor", "PESSOA_NOME", String.class),
                        new FiltroPesquisa("Nº da Conta", "CONTAPAGAR_ID", String.class),
                        new FiltroPesquisa("Data da Conta", "CONTAPAGAR_DATA", Date.class),
                        new FiltroPesquisa("Data Vcto", "VENCIMENTO", Date.class),
                        new FiltroPesquisa("Parcela", "PARCELAS", String.class),
                        new FiltroPesquisa("Valor da Conta", "VALOR", String.class),
                        new FiltroPesquisa("Valor em Aberto", "VALORPENDENTE", String.class)
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                        new InteiroRenderCenter(), new CellRendererData(), new CellRendererData(), new RenderizadorParcelas(), new MonetarioRender(), new MonetarioRender()},
                this, this, true, false, false);
    private MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    public MeuCampoData campoData = new MeuCampoData(10, true, "Data");
    public MeuCampoBuscar campoConta = new MeuCampoBuscar(TelaConsultaPagamento.class, TelaContaPagar.class, DaoParcelaContaPagar.SQL_PESQUISAR, DaoPagamento.SQLINATIVOS, parametrosConsulta, true, true, "Conta", 30);
    public MeuCampoMonetario campoValorConta = new MeuCampoMonetario(8, true, false, false, "Valor");
    public static MeuCampoMonetario campoDesconto = new MeuCampoMonetario(8, true, false, false, "Desconto");
    public static MeuCampoMonetario campoJuros = new MeuCampoMonetario(8, true, false, false, "Juros");
    public static MeuCampoMonetario campoMulta = new MeuCampoMonetario(8, true, false, false, "Multa");
    public static MeuCampoMonetario campoValorLiquido = new MeuCampoMonetario(8, false, false, false, "Valor líquido");
    private MeuJTextArea campoDescricao = new MeuJTextArea("Descrição", false, 400, 50);
      

    public TelaPagamento() {
        super("Cadastro de Pagamento");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/contapagar.png")));
        //painelStatus
       

        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        adicionaComponente(2, 1, 1, 1, campoData, jpComponentes);
        adicionaComponente(3, 1, 1, 1, campoConta, jpComponentes);
        adicionaComponente(4, 1, 1, 1, campoValorConta, jpComponentes);
        adicionaComponente(5, 1, 1, 1, campoDesconto, jpComponentes);
        adicionaComponente(6, 1, 1, 1, campoJuros, jpComponentes);
        adicionaComponente(7, 1, 1, 1, campoMulta, jpComponentes);
        adicionaComponente(8, 1, 1, 1, campoValorLiquido, jpComponentes);
        adicionaComponente(9, 1, 1, 1, campoDescricao, jpComponentes);
        
        jbAlterar.setVisible(false);
        jbExcluir.setVisible(false);
        
        adicionaListeners();
        habilitaComponentes(false);
        campoData.setEnabled(false);
        campoJuros.limpar();
        campoMulta.limpar();
        campoValorLiquido.limpar();
        //setSize(770, 460);
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMaximumSize(new Dimension(getWidth(), getHeight()));  
        pack();
        centralizaTela();
    }

    public TelaPagamento(Pagamento pagamento) {
        this();
        this.pagamento = pagamento;
        //getPersistencia();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaPagamento();
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
        pagamento.setId((Integer) campoCodigo.getValor());
        pagamento.setData(campoData.getValorDate());
        pagamento.setValor(campoValorConta.getValor());
        pagamento.setDesconto(campoDesconto.getValor());
        pagamento.setJuros(campoJuros.getValor());
        pagamento.setMulta(campoMulta.getValor());
        pagamento.setValortotal(campoValorLiquido.getValor());
        pagamento.setDescricao(campoDescricao.getValor());
        pagamento.getParcelaContaPagar().setId((Integer) campoConta.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(pagamento.getId());
        campoData.setValor(pagamento.getData());
        campoValorConta.setValor(pagamento.getValor());
        campoDesconto.setValor(pagamento.getDesconto());
        campoJuros.setValor(pagamento.getJuros());
        campoMulta.setValor(pagamento.getMulta());
        campoValorLiquido.setValor(pagamento.getValortotal());
        campoDescricao.setValor(pagamento.getDescricao());  
        campoConta.setValor(pagamento.getParcelaContaPagar().getId());
    }

    public void calcular() {
        BigDecimal desconto = campoDesconto.getValor();
        BigDecimal juros = campoJuros.getValor();
        BigDecimal multa = campoMulta.getValor();
        BigDecimal valorTotal = campoValorConta.getValor().subtract(desconto).add(juros).add(multa);
        campoValorLiquido.setValor(valorTotal);
    }

    public void adicionaListeners() {
        campoConta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    if (campoConta.achou == true) {
                        preencherDados();
                    } else {
                        campoConta.limpar();
                        campoValorConta.limpar();
                        campoDesconto.limpar();
                        campoJuros.limpar();
                        campoMulta.limpar();
                        campoValorLiquido.limpar();
                    }
                }
            }
        });

        campoConta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (campoConta.eVazio()) {                          
                            campoValorConta.limpar();
                            campoDesconto.limpar();
                            campoJuros.limpar();
                            campoMulta.limpar();
                            campoValorLiquido.limpar();     
                        }
                    }
                });
            }
        });

        campoValorConta.addKeyListener(new KeyAdapter() {
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
        
        campoJuros.addKeyListener(new KeyAdapter() {
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
        
        campoMulta.addKeyListener(new KeyAdapter() {
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
        
        campoValorConta.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {           
           
            } 
            @Override
            public void focusLost(FocusEvent fe) { 
                BigDecimal valorContaDigitado = campoValorConta.getValor();
                if (valorContaDigitado.compareTo(valorPendente) == 1) {
                    JOptionPane.showMessageDialog(null, "Valor digitado maior que o valor da conta.");
                    campoValorConta.requestFocus();
                    campoValorConta.setValor(valorPendente);
                    calcular();
                } else {
                    calcular();
                }
            }
        });
//        
//        this.addInternalFrameListener(new InternalFrameAdapter() {
//            @Override
//            public void internalFrameClosed(InternalFrameEvent e) {
//                limpar();
//            }
//        });
    }

    @Override
    public boolean validaComponentes() {
//      if (super.verificarCampos() == false) {  //Esta linha faz exatamente o mesmo do que a linha abaixo.
        if (!super.validaComponentes()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        getPersistencia();
        boolean retorno = daoPagamento.incluir();
        consultarBD(pagamento.getId());
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoPagamento.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoPagamento.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        pagamento.setId(pk);
        daoPagamento.consultar();
        getPersistencia();
    }

    @Override
    public void incluir() {
        super.incluir();
        campoData.setValor(new Date());
    }

    @Override
    public void alterar() {
        super.alterar();
    }
    
    @Override
    public void confirmar() {
        if (validaComponentes()) {
            BigDecimal valorContaDigitado = campoValorConta.getValor();
            if (valorContaDigitado.compareTo(valorPendente) == 1) {
                return;
            }
            BigDecimal valorComparacao = campoValorLiquido.getValor();
            Caixa caixa = new Caixa();
            DaoCaixa daoCaixa = new DaoCaixa(caixa);
            pegaIDCaixaAberto();
            caixa.setId((int) auxiliarIDCaixaAberto);
            daoCaixa.consultar();
            if (valorComparacao.compareTo(caixa.getSaldo()) == 1) {
                JOptionPane.showMessageDialog(null, "Saldo em caixa insuficiente.");
                campoValorLiquido.requestFocus();
                return;
            } else {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja realmente efetuar o pagamento?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if ((opcao != JOptionPane.OK_OPTION) || (!incluirBD())) {
                    estadoTela = INCLUINDO;
                    habilitaBotoes();
                    return;
                }
            }
            super.confirmar();
            efetuarBaixaParcelaContaPagar();
            efetuarBaixaContaPagar();
            estadoTela = PADRAO;
            habilitaComponentes(false);
            habilitaBotoes();
            if (TelaCaixa.tela != null) {
                TelaCaixa.tela.tabelaMovimentosCaixa.getTableModel().limparDados();
                pegaIDCaixaAberto();
                TelaCaixa.tela.consultarBD(auxiliarIDCaixaAberto); 
            }  
            if (TelaContaPagar.tela != null) {
                TelaContaPagar.tela.consultarBD((int) TelaContaPagar.tela.campoCodigo.getValor());
            }
        }
    }

    @Override
    public void cancelar() {
        super.cancelar();
    }

    @Override
    public void limpar() {
        super.limpar();
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Pagamentos",
                DaoPagamento.SQLPESQUISAR,
                new String[]{"Código", "Fornecedor", "Nº Conta", "Data", "Valor", "Juros", "Multa", "Valor Líquido"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Fornecedor", "PESSOA_NOME", String.class),
                        new FiltroPesquisa("Nº Conta", "PARCELACONTAPAGAR_ID", String.class),
                        new FiltroPesquisa("Data", "DATA", Date.class),
                        new FiltroPesquisa("Valor", "VALOR", String.class),
                        new FiltroPesquisa("Juros", "JUROS", String.class),
                        new FiltroPesquisa("Multa", "MULTA", String.class),
                        new FiltroPesquisa("Valor Líquido", "VALORTOTAL", String.class),
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new InteiroRenderCenter(),
                        new CellRendererData(), new MonetarioRender(), new MonetarioRender(), new MonetarioRender(), new MonetarioRender(),},
                this, TelaConsultaFiltro.class, true, false, false)
        );
    }

    public void preencherDados() {
        if (!campoConta.getValor().equals(0)) {
            ParcelaContaPagar parcelaContaPagar = new ParcelaContaPagar();
            DaoParcelaContaPagar daoParcelaContaPagar = new DaoParcelaContaPagar(parcelaContaPagar);
            parcelaContaPagar.setId((int) campoConta.getValor());
            daoParcelaContaPagar.consultar();
            campoValorConta.setValor(parcelaContaPagar.getValorPendente()); 
            valorPendente = parcelaContaPagar.getValorPendente();
            calcular();
        } else {
            campoConta.limpar();
            campoValorConta.limpar();
            campoJuros.limpar();
            campoMulta.limpar();
            campoValorLiquido.limpar();
        }
    }
    
    public boolean efetuarBaixaParcelaContaPagar() {
        ParcelaContaPagar parcelaContaPagar = new ParcelaContaPagar();
        String SQL_ALTERAR_PARCELA_BAIXA = "UPDATE PARCELACONTAPAGAR SET VALORPENDENTE = ? WHERE ID = ?";
        BigDecimal auxiliarBaixa = BigDecimal.ZERO;
        DaoParcelaContaPagar daoParcelaContaPagar = new DaoParcelaContaPagar(parcelaContaPagar);
        parcelaContaPagar.setId((int) campoConta.getValor());
        daoParcelaContaPagar.consultar();
        auxiliarBaixa = parcelaContaPagar.getValorPendente().subtract(campoValorConta.getValor());
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_ALTERAR_PARCELA_BAIXA);
            ps.setBigDecimal(1, auxiliarBaixa);
            ps.setInt(2, (int) campoConta.getValor());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar alterar o valor da parcela conta pagar (Tela de Pagamento).");
            return false;
        }
    }
    
    public boolean efetuarBaixaContaPagar() {
        ParcelaContaPagar parcelaContaPagar = new ParcelaContaPagar();
        DaoParcelaContaPagar daoParcelaContaPagar = new DaoParcelaContaPagar(parcelaContaPagar);
        String SQL_ALTERAR_CONTA_PAGAR_BAIXA = "UPDATE CONTAPAGAR SET VALORPENDENTE = ? WHERE ID = ?";
        BigDecimal auxiliarBaixa = BigDecimal.ZERO;
        Integer auxiliarIdContaPagar;
        parcelaContaPagar.setId((int) campoConta.getValor());
        daoParcelaContaPagar.consultar();
        ContaPagar contaPagar = new ContaPagar();
        DaoContaPagar daoContaPagar = new DaoContaPagar(contaPagar);
        contaPagar.setId(parcelaContaPagar.getContaPagar().getId());
        daoContaPagar.consultar();
        auxiliarIdContaPagar = parcelaContaPagar.getContaPagar().getId();
        auxiliarBaixa = contaPagar.getValorpendente().subtract(campoValorConta.getValor());
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_ALTERAR_CONTA_PAGAR_BAIXA);
            ps.setBigDecimal(1, auxiliarBaixa);
            ps.setInt(2, (int) auxiliarIdContaPagar);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar alterar o valor pendente da conta a pagar (Tela de Pagamento).");
            return false;
        }
    }
    
    public void pegaIDCaixaAberto() {
        String SQL_VERIFICA_CAIXA = "SELECT ID FROM CAIXA WHERE ID = (SELECT MAX(ID) FROM CAIXA) and SITUACAO = 'Aberto'";
        try {
            ResultSet rs = Conexao.getConexao().createStatement().executeQuery(SQL_VERIFICA_CAIXA);
            if (rs.next()) {
                auxiliarIDCaixaAberto = rs.getInt(1);
            } else {
                JOptionPane.showMessageDialog(null, "Não há nenhum caixa aberto.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter o ID do Caixa.");
            e.printStackTrace();
        }
    }
}
    

