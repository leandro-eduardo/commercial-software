package telas;

import bd.Conexao;
import componentes.*;
import dao.DaoContaReceber;
import dao.DaoParcelaContaReceber;
import dao.DaoRecebimento;
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
import pojo.ContaReceber;
import pojo.ParametrosConsulta;
import pojo.ParcelaContaReceber;
import pojo.Recebimento;
import renderizador.*;

public class TelaRecebimento extends TelaCadastro {

    public static TelaRecebimento tela;
    private Recebimento recebimento = new Recebimento();
    private DaoRecebimento daoRecebimento = new DaoRecebimento(recebimento);

    BigDecimal valorPendente = BigDecimal.ZERO;
    Integer auxiliarIDCaixaAberto;

    public ParametrosConsulta parametrosConsulta
            = new ParametrosConsulta("Contas a Receber - Efetuar Recebimento",
                    DaoParcelaContaReceber.SQL_PESQUISAR,
                    new String[]{"Código", "Cliente", "Nº Conta", "Data Conta", "Data Vcto", "Parcela", "Valor Conta", "Valor em Aberto"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Cliente", "PESSOA_NOME", String.class),
                        new FiltroPesquisa("Nº da Conta", "CONTARECEBER_ID", String.class),
                        new FiltroPesquisa("Data da Conta", "CONTARECEBER_DATA", Date.class),
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
    public MeuCampoBuscar campoConta = new MeuCampoBuscar(TelaConsultaRecebimento.class, TelaContaReceber.class, DaoParcelaContaReceber.SQL_PESQUISAR, DaoRecebimento.SQLINATIVOS, parametrosConsulta, true, true, "Conta", 30);
    public MeuCampoMonetario campoValorConta = new MeuCampoMonetario(8, true, false, false, "Valor");
    public static MeuCampoMonetario campoDesconto = new MeuCampoMonetario(8, true, false, false, "Desconto");
    public static MeuCampoMonetario campoJuros = new MeuCampoMonetario(8, true, false, false, "Juros");
    public static MeuCampoMonetario campoMulta = new MeuCampoMonetario(8, true, false, false, "Multa");
    public static MeuCampoMonetario campoValorLiquido = new MeuCampoMonetario(8, false, false, false, "Valor líquido");
    private MeuJTextArea campoDescricao = new MeuJTextArea("Descrição", false, 400, 50);

    public TelaRecebimento() {
        super("Cadastro de Recebimento");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/contareceber.png")));
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

    public TelaRecebimento(Recebimento recebimento) {
        this();
        this.recebimento = recebimento;
        //getPersistencia();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaRecebimento();
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
        recebimento.setId((Integer) campoCodigo.getValor());
        recebimento.setData(campoData.getValorDate());
        recebimento.setValor(campoValorConta.getValor());
        recebimento.setDesconto(campoDesconto.getValor());
        recebimento.setJuros(campoJuros.getValor());
        recebimento.setMulta(campoMulta.getValor());
        recebimento.setValortotal(campoValorLiquido.getValor());
        recebimento.setDescricao(campoDescricao.getValor());
        recebimento.getParcelaContaReceber().setId((Integer) campoConta.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(recebimento.getId());
        campoData.setValor(recebimento.getData());
        campoValorConta.setValor(recebimento.getValor());
        campoDesconto.setValor(recebimento.getDesconto());
        campoJuros.setValor(recebimento.getJuros());
        campoMulta.setValor(recebimento.getMulta());
        campoValorLiquido.setValor(recebimento.getValortotal());
        campoDescricao.setValor(recebimento.getDescricao());
        campoConta.setValor(recebimento.getParcelaContaReceber().getId());
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
        boolean retorno = daoRecebimento.incluir();
        consultarBD(recebimento.getId());
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoRecebimento.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoRecebimento.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        recebimento.setId(pk);
        daoRecebimento.consultar();
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
            int opcao = JOptionPane.showConfirmDialog(null, "Deseja realmente efetuar o recebimento?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if ((opcao != JOptionPane.OK_OPTION) || (!incluirBD())) {
                estadoTela = INCLUINDO;
                habilitaBotoes();
                return;
            } else {
                super.confirmar();
                efetuarBaixaParcelaContaReceber();
                efetuarBaixaContaReceber();
                estadoTela = PADRAO;
                habilitaComponentes(false);
                habilitaBotoes();
                if (TelaCaixa.tela != null) {
                    TelaCaixa.tela.tabelaMovimentosCaixa.getTableModel().limparDados();
                    pegaIDCaixaAberto();
                    TelaCaixa.tela.consultarBD(auxiliarIDCaixaAberto);
                }
                if (TelaContaReceber.tela != null) {
                    TelaContaReceber.tela.consultarBD((int) TelaContaReceber.tela.campoCodigo.getValor());
                }
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
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Recebimentos",
                DaoRecebimento.SQLPESQUISAR,
                new String[]{"Código", "Fornecedor", "Nº Conta", "Data", "Valor", "Juros", "Multa", "Valor Líquido"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                    new FiltroPesquisa("Fornecedor", "PESSOA_NOME", String.class),
                    new FiltroPesquisa("Nº Conta", "PARCELACONTARECEBER_ID", String.class),
                    new FiltroPesquisa("Data", "DATA", Date.class),
                    new FiltroPesquisa("Valor", "VALOR", String.class),
                    new FiltroPesquisa("Juros", "JUROS", String.class),
                    new FiltroPesquisa("Multa", "MULTA", String.class),
                    new FiltroPesquisa("Valor Líquido", "VALORTOTAL", String.class),},
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new InteiroRenderCenter(),
                    new CellRendererData(), new MonetarioRender(), new MonetarioRender(), new MonetarioRender(), new MonetarioRender(),},
                this, TelaConsultaFiltro.class, true, false, false)
        );
    }

    public void preencherDados() {
        if (!campoConta.getValor().equals(0)) {
            ParcelaContaReceber parcelaContaReceber = new ParcelaContaReceber();
            DaoParcelaContaReceber daoParcelaContaReceber = new DaoParcelaContaReceber(parcelaContaReceber);
            parcelaContaReceber.setId((int) campoConta.getValor());
            daoParcelaContaReceber.consultar();
            campoValorConta.setValor(parcelaContaReceber.getValorPendente());
            valorPendente = parcelaContaReceber.getValorPendente();
            calcular();
        } else {
            campoConta.limpar();
            campoValorConta.limpar();
            campoJuros.limpar();
            campoMulta.limpar();
            campoValorLiquido.limpar();
        }
    }

    public boolean efetuarBaixaParcelaContaReceber() {
        ParcelaContaReceber parcelaContaReceber = new ParcelaContaReceber();
        String SQL_ALTERAR_PARCELA_BAIXA = "UPDATE PARCELACONTARECEBER SET VALORPENDENTE = ? WHERE ID = ?";
        BigDecimal auxiliarBaixa = BigDecimal.ZERO;
        DaoParcelaContaReceber daoParcelaContaReceber = new DaoParcelaContaReceber(parcelaContaReceber);
        parcelaContaReceber.setId((int) campoConta.getValor());
        daoParcelaContaReceber.consultar();
        auxiliarBaixa = parcelaContaReceber.getValorPendente().subtract(campoValorConta.getValor());
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_ALTERAR_PARCELA_BAIXA);
            ps.setBigDecimal(1, auxiliarBaixa);
            ps.setInt(2, (int) campoConta.getValor());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar alterar o valor da parcela conta receber (Tela de Recebimento).");
            return false;
        }
    }

    public boolean efetuarBaixaContaReceber() {
        ParcelaContaReceber parcelaContaReceber = new ParcelaContaReceber();
        DaoParcelaContaReceber daoParcelaContaReceber = new DaoParcelaContaReceber(parcelaContaReceber);
        String SQL_ALTERAR_CONTA_RECEBER_BAIXA = "UPDATE CONTARECEBER SET VALORPENDENTE = ? WHERE ID = ?";
        BigDecimal auxiliarBaixa = BigDecimal.ZERO;
        Integer auxiliarIdContaReceber;
        parcelaContaReceber.setId((int) campoConta.getValor());
        daoParcelaContaReceber.consultar();
        ContaReceber contaReceber = new ContaReceber();
        DaoContaReceber daoContaReceber = new DaoContaReceber(contaReceber);
        contaReceber.setId(parcelaContaReceber.getContaReceber().getId());
        daoContaReceber.consultar();
        auxiliarIdContaReceber = parcelaContaReceber.getContaReceber().getId();
        auxiliarBaixa = contaReceber.getValorpendente().subtract(campoValorConta.getValor());
        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_ALTERAR_CONTA_RECEBER_BAIXA);
            ps.setBigDecimal(1, auxiliarBaixa);
            ps.setInt(2, (int) auxiliarIdContaReceber);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar alterar o valor pendente da conta a receber (Tela de Recebimento).");
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
