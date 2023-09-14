package telas;

import componentes.*;
import dao.DaoCategoria;
import dao.DaoFabricante;
import dao.DaoProduto;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.ParametrosConsulta;
import pojo.Produto;
import renderizador.InteiroRender;
import renderizador.InteiroRenderRight;
import renderizador.RenderizadorTexto;
import util.RestricaoCaracteres;

public class TelaCadastroProduto extends TelaCadastro {

    public static TelaCadastroProduto tela;
    public ParametrosConsulta parametrosConsulta =
            new ParametrosConsulta("Consulta de Fabricante",
                    DaoFabricante.SQLPESQUISAR2,
                    new String[]{"Código", "Nome", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                            new FiltroPesquisa("Nome", "NOME", String.class),
                            new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                            new RenderizadorTexto()},
                    this, this, false, false, false
            );
    public ParametrosConsulta parametrosConsulta2 =
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
    public MeuCampoBuscar campoFabricante = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroFabricante.class, DaoFabricante.SQLCOMBOBOX, DaoFabricante.SQLINATIVOS, parametrosConsulta, true, true, "Fabricante", 30);
    public MeuCampoBuscar campoCategoria = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroCategoria.class, DaoCategoria.SQLCOMBOBOX, DaoCategoria.SQLINATIVOS, parametrosConsulta2, true, true, "Categoria", 30);
    private Produto produto = new Produto();
    private DaoProduto daoProduto = new DaoProduto(produto);
    private MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuJTextField campoNome = new MeuJTextField(40, true, true, "Nome");
    private MeuCampoMonetario campoPrecoCompra = new MeuCampoMonetario(8, true, false, false, "Preco de Compra");
    private MeuCampoMonetario campoPrecoVenda = new MeuCampoMonetario(8, true, false, false, "Preco de Venda");
    private MeuCampoMonetario campoValorLucro = new MeuCampoMonetario(8, true, true, false, "Valor de Lucro");
    private MeuCampoInteger campoQuantidade = new MeuCampoInteger(3, false, false, true, "Quantidade");
    private MeuCampoCheckBox campoAtivo = new MeuCampoCheckBox(true, true, "Ativo");
    private JPanel painelStatus = new JPanel();

    public TelaCadastroProduto() {
        super("Cadastro de Produto");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/produto.png")));
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(9, 1, 0, 0, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoAtivo, painelStatus);

        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        adicionaComponente(2, 1, 1, 1, campoNome, jpComponentes);
        campoNome.setDocument(new RestricaoCaracteres(55));
        adicionaComponente(3, 1, 1, 1, campoFabricante, jpComponentes);
        adicionaComponente(4, 1, 1, 1, campoQuantidade, jpComponentes);
        adicionaComponente(5, 1, 1, 1, campoPrecoCompra, jpComponentes);
        adicionaComponente(6, 1, 1, 1, campoPrecoVenda, jpComponentes);
        adicionaComponente(7, 1, 1, 1, campoValorLucro, jpComponentes);
        adicionaComponente(8, 1, 1, 1, campoCategoria, jpComponentes);
        adicionaListeners();
        habilitaComponentes(false);
        setSize(770, 460);
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMaximumSize(new Dimension(getWidth(), getHeight()));
        //pack();
        centralizaTela();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCadastroProduto();
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
        produto.setId((Integer) campoCodigo.getValor());
        produto.setNome(campoNome.getText());
        produto.setPrecocompra(campoPrecoCompra.getValor());
        produto.setPrecovenda(campoPrecoVenda.getValor());
        produto.setValorlucro(campoValorLucro.getValor());
        produto.setQuantidade((Integer) campoQuantidade.getValor());
        produto.setAtivo(campoAtivo.getValor());
        produto.getFabricante().setId((Integer) campoFabricante.getValor());
        produto.getCategoria().setId((Integer) campoCategoria.getValor());

    }

    public void getPersistencia() {
        campoCodigo.setValor(produto.getId());
        campoNome.setText(produto.getNome());
        campoPrecoCompra.setValor(produto.getPrecocompra());
        campoPrecoVenda.setValor(produto.getPrecovenda());
        campoValorLucro.setValor(produto.getValorlucro());
        campoQuantidade.setValor(produto.getQuantidade());
        campoAtivo.setValor(produto.isAtivo());
        campoFabricante.setValor(produto.getFabricante().getId());
        campoCategoria.setValor(produto.getCategoria().getId());
    }

    public void calcular() {
        BigDecimal precoCompra = campoPrecoCompra.getValor();
        BigDecimal valorLucro = campoPrecoVenda.getValor().subtract(precoCompra);
        campoValorLucro.setValor(valorLucro);
        if (valorLucro.compareTo(BigDecimal.ZERO) == -1) {
            JOptionPane.showMessageDialog(null, "Valor de lucro negativo, verifique!!!");
            campoPrecoVenda.requestFocus();
        }
    }

    public void adicionaListeners() {
        campoPrecoVenda.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent fe) {
                calcular();
            }
        });
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno;
        retorno = daoProduto.incluir();
        getPersistencia();
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoProduto.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoProduto.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        produto.setId(pk);
        daoProduto.consultar();
        getPersistencia();
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Produto",
                DaoProduto.SQLPESQUISAR,
                new String[]{"Código", "Nome", "Quantidade", "Fabricante", "Categoria", "Situação"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "PRODUTO_NOME", String.class),
                        new FiltroPesquisa("Quantidade", "PRODUTO_QUANTIDADE", String.class),
                        new FiltroPesquisa("Fabricante", "FABRICANTE_NOME", String.class),
                        new FiltroPesquisa("Categoria", "CATEGORIA_NOME", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new InteiroRenderRight(),
                        new RenderizadorTexto(), new RenderizadorTexto(), new RenderizadorTexto()},
                this, this, false, false, false)
        );
    }

}
