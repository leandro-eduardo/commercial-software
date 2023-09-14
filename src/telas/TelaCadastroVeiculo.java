package telas;

import componentes.*;
import dao.DaoCor;
import dao.DaoModelo;
import dao.DaoVeiculo;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.ParametrosConsulta;
import pojo.Veiculo;
import renderizador.InteiroRender;
import renderizador.RenderizadorTexto;
import util.RestricaoCaracteres;

public class TelaCadastroVeiculo extends TelaCadastro {

    public static TelaCadastroVeiculo tela;
    public ParametrosConsulta parametrosConsulta
            = new ParametrosConsulta("Consulta de Cor",
                    DaoCor.SQLPESQUISAR2,
                    new String[]{"Código", "Nome", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "NOME", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                        new RenderizadorTexto()},
                    this, this, false, false, false
            );
    public ParametrosConsulta parametrosConsulta2
            = new ParametrosConsulta("Consulta de Modelo",
                    DaoModelo.SQLPESQUISAR2,
                    new String[]{"Código", "Modelo", "Fabricante", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Modelo", "NOME", String.class),
                        new FiltroPesquisa("Fabricante", "FABRICANTE.NOME", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                        new RenderizadorTexto(), new RenderizadorTexto()},
                    this, this, false, false, false
            );
    public MeuCampoBuscar campoCor = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroCor.class, DaoCor.SQLCOMBOBOX, DaoCor.SQLINATIVOS, parametrosConsulta, true, true, "Cor", 20);
    public MeuCampoBuscar campoModelo = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroModelo.class, DaoModelo.SQLCOMBOBOX, DaoModelo.SQLINATIVOS, parametrosConsulta2, true, true, "Modelo", 30);
    private Veiculo veiculo = new Veiculo();
    private DaoVeiculo daoVeiculo = new DaoVeiculo(veiculo);
    private MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuCampoPlaca campoPlaca = new MeuCampoPlaca(6, true, "Placa");
    private MeuJTextField campoAnoFabricacao = new MeuJTextField(5, true, true, "Ano de Fabricação");
    private MeuJTextField campoChassi = new MeuJTextField(25, true, false, "Chassi");
    private MeuCampoCheckBox campoAtivo = new MeuCampoCheckBox(true, true, "Ativo");
    private JPanel painelStatus = new JPanel();

    public TelaCadastroVeiculo() {
        super("Cadastro de Veiculo");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/veiculo.png")));
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(7, 1, 0, 0, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoAtivo, painelStatus);

        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        adicionaComponente(2, 1, 1, 1, campoModelo, jpComponentes);
        adicionaComponente(3, 1, 1, 1, campoPlaca, jpComponentes);
        campoChassi.setDocument(new RestricaoCaracteres(17));
        adicionaComponente(4, 1, 1, 1, campoChassi, jpComponentes);
        campoAnoFabricacao.setDocument(new RestricaoCaracteres(4));
        adicionaComponente(5, 1, 1, 1, campoAnoFabricacao, jpComponentes);
        adicionaComponente(6, 1, 1, 1, campoCor, jpComponentes);
        habilitaComponentes(false);
        //setSize(700, 350);
        pack();
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCadastroVeiculo();
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
        veiculo.setId((Integer) campoCodigo.getValor());
        veiculo.setPlaca(campoPlaca.getText());
        veiculo.setChassi(campoChassi.getText());
        veiculo.setAnofabricacao(campoAnoFabricacao.getText());
        veiculo.setAtivo(campoAtivo.getValor());
        veiculo.getCor().setId((Integer) campoCor.getValor());
        veiculo.getModelo().setId((Integer) campoModelo.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(veiculo.getId());
        campoPlaca.setText(veiculo.getPlaca());
        campoChassi.setText(veiculo.getChassi());
        campoAnoFabricacao.setText(veiculo.getAnofabricacao());
        campoAtivo.setValor(veiculo.isAtivo());
        campoCor.setValor(veiculo.getCor().getId());
        campoModelo.setValor(veiculo.getModelo().getId());
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno = daoVeiculo.incluir();
        getPersistencia();
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoVeiculo.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoVeiculo.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        veiculo.setId(pk);
        daoVeiculo.consultar();
        getPersistencia();
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Veículo",
                DaoVeiculo.SQLPESQUISAR,
                new String[]{"Código", "Modelo", "Placa", "Fabricante", "Cor", "Ano", "Situação"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                    new FiltroPesquisa("Modelo", "MODELO_NOME", String.class),
                    new FiltroPesquisa("Placa", "PLACA", String.class),
                    new FiltroPesquisa("Fabricante", "FABRICANTE_NOME", String.class),
                    new FiltroPesquisa("Cor", "COR_NOME", String.class),
                    new FiltroPesquisa("Ano", "ANOFABRICACAO", String.class),
                    new FiltroPesquisa("Situação", "VEICULO_ATIVO", String.class),},
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new RenderizadorTexto(),
                    new RenderizadorTexto(), new RenderizadorTexto(), new RenderizadorTexto(), new RenderizadorTexto()},
                this, this, false, false, false)
        );
    }
}
