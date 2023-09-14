package telas;

import componentes.FiltroPesquisa;
import componentes.MeuCampoCheckBox;
import componentes.MeuCampoInteger;
import componentes.MeuJTextField;
import dao.DaoCondicaoPagamento;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.CondicaoPagamento;
import pojo.ParametrosConsulta;
import renderizador.InteiroRender;
import renderizador.RenderizadorTexto;
import util.RestricaoCaracteres;

public class TelaCadastroCondicaoPagamento extends TelaCadastro {

    public static TelaCadastroCondicaoPagamento tela;
    public CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
    public DaoCondicaoPagamento daoCondicaoPagamento = new DaoCondicaoPagamento(condicaoPagamento);
    public ParametrosConsulta parametrosConsulta;
    public MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    public MeuJTextField campoDescricao = new MeuJTextField(25, true, true, "Descrição");
    public MeuCampoInteger campoParcelas = new MeuCampoInteger(2, false, true, true, "Parcelas");
    public MeuCampoInteger campoCarencia = new MeuCampoInteger(2, false, true, false, "Carência");
    public MeuCampoInteger campoPrazo = new MeuCampoInteger(2, false, true, true, "Prazo");
    public MeuCampoCheckBox campoAtivo = new MeuCampoCheckBox(true, true, "Ativo");
    private JPanel painelStatus = new JPanel();

    public TelaCadastroCondicaoPagamento() {
        super("Cadastro de Condição de Pagamento");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/condicaopagamento.png")));
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(6, 1, 0, 0, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoAtivo, painelStatus);
        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        campoDescricao.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(2, 1, 1, 1, campoDescricao, jpComponentes);
        adicionaComponente(3, 1, 1, 1, campoParcelas, jpComponentes);
        adicionaComponente(4, 1, 1, 1, campoCarencia, jpComponentes);
        adicionaComponente(5, 1, 1, 1, campoPrazo, jpComponentes);
        habilitaComponentes(false);
        //setSize(700, 250);
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMaximumSize(new Dimension(getWidth(), getHeight()));
        pack();
        centralizaTela();

    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCadastroCondicaoPagamento();
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
        condicaoPagamento.setId((int) campoCodigo.getValor());
        condicaoPagamento.setDescricao(campoDescricao.getText());
        condicaoPagamento.setParcelas((int) campoParcelas.getValor());
        condicaoPagamento.setCarencia((int) campoCarencia.getValor());
        condicaoPagamento.setPrazo((int) campoPrazo.getValor());
        condicaoPagamento.setAtivo(campoAtivo.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(condicaoPagamento.getId());
        campoDescricao.setText(condicaoPagamento.getDescricao());
        campoParcelas.setValor(condicaoPagamento.getParcelas());
        campoCarencia.setValor(condicaoPagamento.getCarencia());
        campoPrazo.setValor(condicaoPagamento.getPrazo());
        campoAtivo.setValor(condicaoPagamento.isAtivo());
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno = daoCondicaoPagamento.incluir();
        getPersistencia();
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoCondicaoPagamento.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoCondicaoPagamento.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        condicaoPagamento.setId(pk);
        daoCondicaoPagamento.consultar();
        getPersistencia();
        super.consultarBD(pk);
    }

    @Override
    public void consultar() {
        FiltroPesquisa[] filtros = new FiltroPesquisa[]{
                new FiltroPesquisa("Código", "ID", Integer.class),
                new FiltroPesquisa("Descrição", "DESCRICAO", String.class),
                new FiltroPesquisa("Situação", "ATIVO", String.class),
        };
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Condição de Pagamento",
                DaoCondicaoPagamento.SQLPESQUISAR,
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
                this, this, false, false, false)
        );
    }
}
