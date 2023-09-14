package telas;

import bd.Conexao;
import componentes.*;
import dao.DaoCaixa;
import dao.DaoMovimentoCaixa;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.Caixa;
import pojo.MovimentoCaixa;
import pojo.ParametrosConsulta;
import renderizador.CellRendererData;
import renderizador.CellRendererHora;
import renderizador.InteiroRender;
import renderizador.MonetarioRender;
import renderizador.RenderizadorStatus;
import renderizador.RenderizadorTexto;
import tabela.TabelaMovimentoCaixa;

public class TelaMovimentoCaixa extends TelaCadastro {

    public static TelaMovimentoCaixa tela;

    GregorianCalendar gCalendar = new GregorianCalendar();

    private Caixa caixa = new Caixa();
    private DaoCaixa daoCaixa = new DaoCaixa(caixa);

    public MovimentoCaixa movimentoCaixa = new MovimentoCaixa();
    private DaoMovimentoCaixa daoMovimentoCaixa = new DaoMovimentoCaixa(movimentoCaixa);

    public ParametrosConsulta parametrosConsulta
            = new ParametrosConsulta("Consulta de Caixa",
                    DaoCaixa.SQLCONSULTARTUDO,
                    new String[]{"Código", "Data", "Saldo Inicial", "Saldo Final", "Diferença", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Data", "Data", Date.class),
                        new FiltroPesquisa("Saldo Inicial", "SALDOINICIAL", String.class),
                        new FiltroPesquisa("Saldo Final", "SALDOFINAL", String.class),
                        new FiltroPesquisa("Diferença", "DIFERENCA", String.class),
                        new FiltroPesquisa("Situação", "ABERTO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new CellRendererData(),
                        new MonetarioRender(), new MonetarioRender(), new MonetarioRender(), new RenderizadorTexto()},
                    this, this, false, false, false
            );

    public MeuCampoBuscar campoCaixa = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaMovimentoCaixa.class, DaoCaixa.SQLCOMBOBOX, DaoCaixa.SQLCONSULTARTUDO, parametrosConsulta, true, false, "Caixa", 30);
    private MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuJTextArea campoDescricao = new MeuJTextArea("Descrição", false, 300, 50);
    private MeuJComboBox campoDebitoCredito = new MeuJComboBox(true, "Tipo", new String[][]{{"D", "Débito"}, {"C", "Crédito"}});
    public MeuCampoMonetario campoValor = new MeuCampoMonetario(15, true, false, true, "Valor");
    public MeuCampoData campoData = new MeuCampoData(10, false, "Data");
    public MeuCampoHora campoHoraTransacao = new MeuCampoHora(5, false, "Hora");
    public TabelaMovimentoCaixa tabelaMovimentosCaixa = new TabelaMovimentoCaixa();

    public TelaMovimentoCaixa() {
        super("Tela Movimento de Caixa");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/caixa.png")));

        adicionaComponente(1, 0, 1, 1, campoCodigo, jpComponentes);
        adicionaComponente(2, 0, 1, 1, campoCaixa, jpComponentes);
        adicionaComponente(3, 0, 1, 1, campoData, jpComponentes);
        adicionaComponente(4, 0, 1, 1, campoHoraTransacao, jpComponentes);
        adicionaComponente(5, 0, 1, 1, campoDebitoCredito, jpComponentes);
        adicionaComponente(6, 0, 1, 1, campoValor, jpComponentes);
        adicionaComponente(7, 0, 1, 1, campoDescricao, jpComponentes);

        removeBotao(jbExcluir);
        removeBotao(jbAlterar);

        adicionaListeners();
        adicionaScrollPane();
        habilitaComponentes(false);

        pack();
        //setSize(800, 630);  
        //setMaximumSize(new Dimension(getWidth(), getHeight()));
        //setMinimumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();
    }

    public TelaMovimentoCaixa(MovimentoCaixa movimentoCaixa) {
        this();
        this.movimentoCaixa = movimentoCaixa;
        getPersistencia();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaMovimentoCaixa();
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
        movimentoCaixa.setId((Integer) campoCodigo.getValor());
        movimentoCaixa.setDescricao(campoDescricao.getValor());
        movimentoCaixa.setDebitoCredito((String) campoDebitoCredito.getValor());
        movimentoCaixa.setValor(campoValor.getValor());
        movimentoCaixa.setData(campoData.getValorDate());
        movimentoCaixa.setHoraTransacao(campoHoraTransacao.getValorHora());
        movimentoCaixa.getCaixa().setId((Integer) campoCaixa.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(movimentoCaixa.getId());
        campoDescricao.setValor(movimentoCaixa.getDescricao());
        campoDebitoCredito.setValor(movimentoCaixa.getDebitoCredito());
        campoValor.setValor(movimentoCaixa.getValor());
        campoData.setValor(movimentoCaixa.getData());
        campoHoraTransacao.setValor(movimentoCaixa.getHoraTransacao());
        campoCaixa.setValor(movimentoCaixa.getCaixa().getId());
    }

//    public void calcular() {
//        BigDecimal saldoInicial = campoSaldoInicial.getValor();
//        BigDecimal diferenca = campoSaldoFinal.getValor().subtract(saldoInicial);
//        campoDiferenca.setValor(diferenca);
//    }
    public void adicionaListeners() {

    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        getPersistencia();
        boolean retorno = daoMovimentoCaixa.incluir();
        consultarBD(movimentoCaixa.getId());
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoMovimentoCaixa.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoMovimentoCaixa.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        movimentoCaixa.setId(pk);
        daoMovimentoCaixa.consultar();
        getPersistencia();
    }

    @Override
    public void incluir() {
        if (verificaCaixaAberto() == false) {
            return;
        } else {
            super.incluir();
            campoData.setValor(new Date());
            campoHoraTransacao.setEnabled(false);
            pegaIDCaixaAberto();
        }

    }

    @Override
    public void alterar() {
        super.alterar();
        //botaoGerarParcelas.setEnabled(true);
    }

    @Override
    public void cancelar() {
        super.cancelar();
        //botaoGerarParcelas.setEnabled(false);
        //((TableModelParcelaCaixa) tabelaParcela.getTabela().getModel()).limparDados();
    }

    @Override
    public void confirmar() {
//        if (verificaCaixaDigitadoFechado() == true) {
//            return;
//        }
        BigDecimal valorComparacao = campoValor.getValor();
        if (valorComparacao.compareTo(BigDecimal.ZERO) == 0) {
            int opcao = JOptionPane.showConfirmDialog(null, "Deseja realmente inserir uma movimentação sem valor?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if ((opcao != JOptionPane.OK_OPTION) || (!incluirBD())) {
                estadoTela = INCLUINDO;
                habilitaBotoes();
                return;
            }
        }
        caixa.setId((int) campoCaixa.getValor());
        daoCaixa.consultar();
        if (valorComparacao.compareTo(caixa.getSaldo()) == 1 && campoDebitoCredito.getSelectedItem().equals("Crédito")) {
            System.out.println("VALOR COMPARACAO " + valorComparacao);
            System.out.println("VALOR SALDO " + caixa.getSaldo());
            JOptionPane.showMessageDialog(null, "Saldo em caixa insuficiente.");
            campoValor.requestFocus();
            return;
        }
        super.confirmar();
        estadoTela = PADRAO;
        habilitaComponentes(false);
        habilitaBotoes();
        //TelaCaixa.tela.tabelaMovimentosCaixa.getTableModel().limparDados();
        TelaCaixa.tela.consultarBD(movimentoCaixa.getCaixa().getId());
    }

    @Override
    public void limpar() {
        super.limpar();
        //botaoGerarParcelas.setEnabled(false);
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Movimentos",
                DaoMovimentoCaixa.SQL_PESQUISAR,
                new String[]{"Código", "Descrição", "Tipo", "Valor", "Data", "Hora", "Caixa", "Pagamento", "Recebimento"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                    new FiltroPesquisa("Descricao", "DESCRICAO", String.class),
                    new FiltroPesquisa("Tipo", "DEBITOCREDITO", String.class),
                    new FiltroPesquisa("Valor", "VALOR", String.class),
                    new FiltroPesquisa("Data", "DATA", Date.class),
                    new FiltroPesquisa("Hora", "HORATRANSACAO", String.class),
                    new FiltroPesquisa("Caixa", "IDCAIXA", String.class),
                    new FiltroPesquisa("Pagamento", "IDPAGAMENTO", Integer.class),
                    new FiltroPesquisa("Recebimento", "IDRECEBIMENTO", Integer.class),},
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new RenderizadorStatus(),
                    new MonetarioRender(), new CellRendererData(), new CellRendererHora(), new InteiroRender(), new InteiroRender(), new InteiroRender()},
                this, TelaConsultaFiltro.class, true, false, false)
        );
    }

    public boolean verificaCaixaDigitadoFechado() {
        String SQL_VERIFICA_CAIXA = "SELECT ID FROM CAIXA WHERE ID = " + campoCaixa.getValor() + " and SITUACAO = 'Fechado'";
        Integer caixaID = 0;
        System.out.println(SQL_VERIFICA_CAIXA);
        try {
            ResultSet rs = Conexao.getConexao().createStatement().executeQuery(SQL_VERIFICA_CAIXA);
            if (rs.next()) {
                caixaID = rs.getInt(1);
                JOptionPane.showMessageDialog(null, "O caixa com o código " + caixaID + " está fechado.");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher os campos.");
            e.printStackTrace();
            return false;
        }
    }

    private boolean verificaCaixaAberto() {
        String SQL_VERIFICA_CAIXA = "SELECT ID FROM CAIXA WHERE ID = (SELECT MAX(ID) FROM CAIXA) and SITUACAO = 'Aberto'";
        try {
            ResultSet rs = Conexao.getConexao().createStatement().executeQuery(SQL_VERIFICA_CAIXA);
            if (rs.next()) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Não há nenhum caixa aberto.");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher os campos.");
            e.printStackTrace();
            return false;
        }
    }

    public void pegaIDCaixaAberto() {
        String SQL_VERIFICA_CAIXA = "SELECT ID FROM CAIXA WHERE ID = (SELECT MAX(ID) FROM CAIXA) and SITUACAO = 'Aberto'";
        try {
            ResultSet rs = Conexao.getConexao().createStatement().executeQuery(SQL_VERIFICA_CAIXA);
            if (rs.next()) {
                campoCaixa.setValor(rs.getInt(1));
            } else {
                JOptionPane.showMessageDialog(null, "Não há nenhum caixa aberto.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter o ID do Caixa.");
            e.printStackTrace();
        }
    }

//    public boolean atualizaSaldoCaixa() { //método para atualizar saldo do caixa funcionando perfeitamente quando inserido um movimento extra, apenas não o saldo quando abre o caixa
//        Caixa caixa = new Caixa();        //com saldo inicial. A trigger está fazendo toda a validação
//        DaoCaixa daoCaixa = new DaoCaixa(caixa);
//        String SQL_ATUALIZAR_SALDO = "UPDATE CAIXA SET SALDO = ? WHERE ID = ?";
//        BigDecimal auxiliarSaldo = BigDecimal.ZERO;
//        String auxiliarTipo;
//
//        caixa.setId((int) campoCaixa.getValor());
//        daoCaixa.consultar();
//        auxiliarTipo = (String) campoDebitoCredito.getValor();
//        if (auxiliarTipo.equals("C")) {
//            auxiliarSaldo = caixa.getSaldo().subtract(campoValor.getValor());
//        } else if (auxiliarTipo.equals("D")) {
//            auxiliarSaldo = caixa.getSaldo().add(campoValor.getValor());
//        }
//        try {
//            PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_ATUALIZAR_SALDO);
//            ps.setBigDecimal(1, auxiliarSaldo);
//            ps.setInt(2, (int) campoCaixa.getValor());
//            ps.executeUpdate();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Houve um problema ao tentar atualizar o saldo do caixa (TelaMovimentoCaixa).");
//            return false;
//        }
//    }
}
