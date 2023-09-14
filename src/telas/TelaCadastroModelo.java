package telas;

import componentes.*;
import dao.DaoFabricante;
import dao.DaoModelo;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.Modelo;
import pojo.ParametrosConsulta;
import renderizador.InteiroRender;
import renderizador.RenderizadorTexto;
import util.RestricaoCaracteres;

public class TelaCadastroModelo extends TelaCadastro {

    public static TelaCadastroModelo tela;
    public ParametrosConsulta parametrosConsulta =
            new ParametrosConsulta("Consulta de Fabricante",
                    DaoFabricante.SQLPESQUISAR,
                    new String[]{"Código", "Nome", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                            new FiltroPesquisa("Nome", "NOME", String.class),
                            new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                            new RenderizadorTexto(), new RenderizadorTexto()},
                    this, this, false, false, false
            );
    public MeuCampoBuscar campoFabricante = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroFabricante.class, DaoFabricante.SQLCOMBOBOX, DaoFabricante.SQLINATIVOS, parametrosConsulta, true, true, "Fabricante", 30);
    private Modelo modelo = new Modelo();
    private DaoModelo daoModelo = new DaoModelo(modelo);
    private MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuJTextField campoNome = new MeuJTextField(25, true, true, "Nome");
    private MeuCampoCheckBox campoAtivo = new MeuCampoCheckBox(true, true, "Ativo");
    private JPanel painelStatus = new JPanel();

    public TelaCadastroModelo() {
        super("Cadastro de Modelo");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/modelo.png")));
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(4, 1, 0, 0, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoAtivo, painelStatus);

        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        campoNome.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(2, 1, 1, 1, campoNome, jpComponentes);
        adicionaComponente(3, 1, 1, 1, campoFabricante, jpComponentes);
        habilitaComponentes(false);
        //setSize(700, 250);
        pack();
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCadastroModelo();
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
        modelo.setId((Integer) campoCodigo.getValor());
        modelo.setNome(campoNome.getText());
        modelo.setAtivo(campoAtivo.getValor());
        modelo.getFabricante().setId((Integer) campoFabricante.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(modelo.getId());
        campoNome.setText(modelo.getNome());
        campoAtivo.setValor(modelo.isAtivo());
        campoFabricante.setValor(modelo.getFabricante().getId());
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno = daoModelo.incluir();
        getPersistencia();
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoModelo.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoModelo.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        modelo.setId(pk);
        daoModelo.consultar();
        getPersistencia();
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Modelo",
                DaoModelo.SQLPESQUISAR,
                new String[]{"Código", "Nome", "Fabricante", "Situação"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "MODELO_NOME", String.class),
                        new FiltroPesquisa("Fabricante", "FABRICANTE_NOME", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                        new RenderizadorTexto(), new RenderizadorTexto()},
                this, this, false, false, false)
        );
    }
}
