package telas;

import componentes.*;
import dao.DaoCidade;
import dao.DaoEstado;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.Cidade;
import pojo.ParametrosConsulta;
import renderizador.InteiroRender;
import renderizador.RenderizadorTexto;
import util.RestricaoCaracteres;

public class TelaCadastroCidade extends TelaCadastro {

    public static TelaCadastroCidade tela;
    public ParametrosConsulta parametrosConsulta =
            new ParametrosConsulta("Consulta de Estado",
                    DaoEstado.SQLPESQUISAR2,
                    new String[]{"Código", "Nome", "Sigla", "País", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                            new FiltroPesquisa("Nome", "ESTADO_NOME", String.class),
                            new FiltroPesquisa("Sigla", "SIGLA", String.class),
                            new FiltroPesquisa("País", "PAIS_NOME", String.class),
                            new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                            new RenderizadorTexto(), new RenderizadorTexto(), new RenderizadorTexto()},
                    this, this, false, false, false
            );
    public MeuCampoBuscar campoEstado = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroEstado.class, DaoEstado.SQLCOMBOBOX, DaoEstado.SQLINATIVOS, parametrosConsulta, true, true, "Estado", 30);
    private Cidade cidade = new Cidade();
    private DaoCidade daoCidade = new DaoCidade(cidade);
    private MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuJTextField campoNome = new MeuJTextField(25, true, true, "Nome");
    private MeuCampoCheckBox campoAtivo = new MeuCampoCheckBox(true, true, "Ativo");
    private JPanel painelStatus = new JPanel();

    public TelaCadastroCidade() {
        super("Cadastro de Cidade");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cidade.png")));
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(4, 1, 1, 1, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoAtivo, painelStatus);

        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        campoNome.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(2, 1, 1, 1, campoNome, jpComponentes);
        adicionaComponente(3, 1, 1, 1, campoEstado, jpComponentes);
        final Class tela = TelaCadastroEstado.class;
        habilitaComponentes(false);
        //setSize(700, 270);
        pack();
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMaximumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCadastroCidade();
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
        cidade.setId((Integer) campoCodigo.getValor());
        cidade.setNome(campoNome.getText());
        cidade.getEstado().setId((Integer) campoEstado.getValor());
        cidade.setAtivo(campoAtivo.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(cidade.getId());
        campoNome.setText(cidade.getNome());
        campoEstado.setValor(cidade.getEstado().getId());
        campoAtivo.setValor(cidade.isAtivo());
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno = daoCidade.incluir();
        getPersistencia();
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoCidade.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoCidade.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        cidade.setId(pk);
        daoCidade.consultar();
        getPersistencia();
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Cidade",
                DaoCidade.SQLPESQUISAR,
                new String[]{"Código", "Nome", "Estado", "Situação"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "NOME", String.class),
                        new FiltroPesquisa("Estado", "ESTADO_NOME", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                        new RenderizadorTexto(), new RenderizadorTexto()},
                this, this, false, false, false)
        );
    }

}
