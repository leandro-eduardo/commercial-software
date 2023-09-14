package telas;

import componentes.FiltroPesquisa;
import componentes.MeuCampoCheckBox;
import componentes.MeuCampoInteger;
import componentes.MeuJTextField;
import dao.DaoCategoria;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.Categoria;
import pojo.ParametrosConsulta;
import renderizador.InteiroRender;
import renderizador.RenderizadorTexto;
import util.RestricaoCaracteres;

public class TelaCadastroCategoria extends TelaCadastro {

    public static TelaCadastroCategoria tela;
    public Categoria categoria = new Categoria();
    public DaoCategoria daoCategoria = new DaoCategoria(categoria);
    public ParametrosConsulta parametrosConsulta;
    public MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    public MeuJTextField campoNome = new MeuJTextField(25, true, true, "Nome");
    public MeuCampoCheckBox campoAtivo = new MeuCampoCheckBox(true, true, "Ativo");
    private JPanel painelStatus = new JPanel();

    public TelaCadastroCategoria() {
        super("Cadastro de Categoria");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/categoria.png")));
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(3, 1, 1, 1, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoAtivo, painelStatus);

        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        campoNome.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(2, 1, 1, 1, campoNome, jpComponentes);
        habilitaComponentes(false);
        //setSize(700, 230);
        pack();
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMaximumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();

    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCadastroCategoria();
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
        categoria.setId((int) campoCodigo.getValor());
        categoria.setNome(campoNome.getText());
        categoria.setAtivo(campoAtivo.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(categoria.getId());
        campoNome.setText(categoria.getNome());
        campoAtivo.setValor(categoria.isAtivo());
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno = daoCategoria.incluir();
        getPersistencia();
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoCategoria.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoCategoria.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        categoria.setId(pk);
        daoCategoria.consultar();
        getPersistencia();
        super.consultarBD(pk);
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Categoria",
                DaoCategoria.SQLPESQUISAR,
                new String[]{"Código", "Nome", "Situação"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "NOME", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                        new RenderizadorTexto()},
                this, this, false, false, false)
        );
    }
}
