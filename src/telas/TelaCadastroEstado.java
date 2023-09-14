package telas;

import componentes.*;
import dao.DaoEstado;
import dao.DaoPais;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.Estado;
import pojo.ParametrosConsulta;
import renderizador.InteiroRender;
import renderizador.RenderizadorTexto;
import util.RestricaoCaracteres;
import util.RestricaoCaracteresSigla;

public class TelaCadastroEstado extends TelaCadastro {

    public static TelaCadastroEstado tela;
    public ParametrosConsulta parametrosConsulta =
            new ParametrosConsulta("Consulta de País",
                    DaoPais.SQLPESQUISAR2,
                    new String[]{"Código", "Nome", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                            new FiltroPesquisa("Nome", "NOME", String.class),
                            new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                            new RenderizadorTexto(), new RenderizadorTexto()},
                    this, this, false, false, false
            );
    public MeuCampoBuscar campoPais = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroPais.class, DaoPais.SQLCOMBOBOX, DaoPais.SQLINATIVOS, parametrosConsulta, true, true, "País", 30);
    private Estado estado = new Estado();
    private DaoEstado daoEstado = new DaoEstado(estado);
    private MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuJTextField campoNome = new MeuJTextField(25, true, true, "Nome");
    private MeuJTextField campoSigla = new MeuJTextField(3, true, true, "Sigla");
    private MeuCampoCheckBox campoAtivo = new MeuCampoCheckBox(true, true, "Ativo");
    private JPanel painelStatus = new JPanel();


    public TelaCadastroEstado() {
        super("Cadastro de Estado");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/estado.png")));
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(5, 1, 0, 0, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoAtivo, painelStatus);

        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        campoNome.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(2, 1, 1, 1, campoNome, jpComponentes);
        campoSigla.setDocument(new RestricaoCaracteresSigla(2));
        adicionaComponente(3, 1, 1, 1, campoSigla, jpComponentes);
        adicionaComponente(4, 1, 1, 1, campoPais, jpComponentes);
        habilitaComponentes(false);
        //setSize(700, 300);
        pack();
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMaximumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCadastroEstado();
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
        estado.setId((Integer) campoCodigo.getValor());
        estado.setNome(campoNome.getText());
        estado.setSigla(campoSigla.getText());
        estado.setAtivo(campoAtivo.getValor());
        estado.getPais().setId((Integer) campoPais.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(estado.getId());
        campoNome.setText(estado.getNome());
        campoSigla.setText(estado.getSigla());
        campoAtivo.setValor(estado.isAtivo());
        campoPais.setValor(estado.getPais().getId());
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno = daoEstado.incluir();
        getPersistencia();
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoEstado.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoEstado.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        estado.setId(pk);
        daoEstado.consultar();
        getPersistencia();
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Estado",
                DaoEstado.SQLPESQUISAR,
                new String[]{"Código", "Nome", "Sigla", "País", "Situação"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "ESTADO_NOME", String.class),
                        new FiltroPesquisa("Sigla", "SIGLA", String.class),
                        new FiltroPesquisa("País", "PAIS_NOME", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                        new RenderizadorTexto(), new RenderizadorTexto(), new RenderizadorTexto()},
                this, this, false, false, false)
        );
    }
}
