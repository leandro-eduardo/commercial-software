package telas;

import componentes.*;
import dao.DaoCategoria;
import dao.DaoServico;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.ParametrosConsulta;
import pojo.Servico;
import renderizador.InteiroRender;
import renderizador.RenderizadorTexto;
import util.RestricaoCaracteres;

public class TelaCadastroServico extends TelaCadastro {

    public static TelaCadastroServico tela;
    public ParametrosConsulta parametrosConsulta =
            new ParametrosConsulta("Consulta de Categoria",
                    DaoCategoria.SQLPESQUISAR2,
                    new String[]{"Código", "Nome", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                            new FiltroPesquisa("Nome", "NOME", String.class),
                            new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                            new RenderizadorTexto()},
                    this, this, false, false, false
            );
    public MeuCampoBuscar campoCategoria = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroCategoria.class, DaoCategoria.SQLCOMBOBOX, DaoCategoria.SQLINATIVOS, parametrosConsulta, true, true, "Categoria", 30);
    private Servico servico = new Servico();
    private DaoServico daoServico = new DaoServico(servico);
    private MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuJTextField campoNome = new MeuJTextField(25, true, true, "Nome");
    private MeuCampoMonetario campoValor = new MeuCampoMonetario(8, true, false, false, "Valor");
    private MeuCampoCheckBox campoAtivo = new MeuCampoCheckBox(true, true, "Ativo");
    private JPanel painelStatus = new JPanel();

    public TelaCadastroServico() {
        super("Cadastro de Serviço");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/servico.png")));
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(5, 1, 0, 0, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoAtivo, painelStatus);

        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        campoNome.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(2, 1, 1, 1, campoNome, jpComponentes);
        adicionaComponente(3, 1, 1, 1, campoValor, jpComponentes);
        adicionaComponente(4, 1, 1, 1, campoCategoria, jpComponentes);
        habilitaComponentes(false);
        //setSize(700, 270);
        pack();
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMaximumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCadastroServico();
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
        servico.setId((Integer) campoCodigo.getValor());
        servico.setNome(campoNome.getText());
        servico.setValor(campoValor.getValor());
        servico.setAtivo(campoAtivo.getValor());
        servico.getCategoria().setId((Integer) campoCategoria.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(servico.getId());
        System.out.println(campoCodigo.getValor());
        campoNome.setText(servico.getNome());
        campoValor.setValor(servico.getValor());
        campoAtivo.setValor(servico.isAtivo());
        campoCategoria.setValor(servico.getCategoria().getId());
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno = daoServico.incluir();
        getPersistencia();
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoServico.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoServico.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        servico.setId(pk);
        daoServico.consultar();
        getPersistencia();
    }

    @Override
    public void consultar() {
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Serviço",
                DaoServico.SQLPESQUISAR,
                new String[]{"Código", "Nome", "Categoria", "Situação"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "SERVICO_NOME", String.class),
                        new FiltroPesquisa("Categoria", "CATEGORIA_NOME", String.class),
                        new FiltroPesquisa("Situação", "SERVICO_ATIVO", String.class)
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                        new RenderizadorTexto(), new RenderizadorTexto()},
                this, this, false, false, false)
        );
    }
}
