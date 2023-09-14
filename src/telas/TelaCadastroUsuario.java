package telas;

import componentes.*;
import dao.DaoPessoa;
import dao.DaoUsuario;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.ParametrosConsulta;
import pojo.Usuario;
import renderizador.InteiroRender;
import renderizador.RenderizadorTexto;
import util.RestricaoCaracteresMinusculos;

public class TelaCadastroUsuario extends TelaCadastro {

    public static TelaCadastroUsuario tela;
    public ParametrosConsulta parametrosConsulta
            = new ParametrosConsulta("Consulta de Funcionários",
                    DaoPessoa.SQLFUNCIONARIOSSEMCADASTRO,
                    new String[]{"Código", "Nome", "CPF/CNPJ", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "NOMERAZAOSOCIAL", String.class),
                        new FiltroPesquisa("CPF/CNPJ", "CPFCNPJ", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new RenderizadorTexto(),
                        new RenderizadorTexto(), new RenderizadorTexto(),
                        new RenderizadorTexto()
                    },
                    this, this, false, false, false
            );
    public MeuCampoBuscar campoPessoa = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroPessoa.class, DaoPessoa.SQLFUNCIONARIOS, DaoPessoa.SQLINATIVOS, parametrosConsulta, false, true, "Pessoa", 50);
    private Usuario usuario = new Usuario();
    private DaoUsuario daoUsuario = new DaoUsuario(usuario);
    public MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuJTextField campoUsuario = new MeuJTextField(25, true, true, "Usuário");
    private MeuJPasswordField campoSenha = new MeuJPasswordField(true, true, "Senha");
    private MeuCampoCheckBox campoStatus = new MeuCampoCheckBox(true, true, "Ativo");
    private JPanel painelStatus = new JPanel();

    public TelaCadastroUsuario() {
        super("Cadastro de Usuário");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/man-user.png")));
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(5, 1, 0, 0, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoStatus, painelStatus);

        adicionaComponente(1, 1, 1, 1, campoCodigo, jpComponentes);
        campoUsuario.setDocument(new RestricaoCaracteresMinusculos(45));
        adicionaComponente(2, 1, 1, 1, campoUsuario, jpComponentes);
        adicionaComponente(3, 1, 1, 1, campoSenha, jpComponentes);
        adicionaComponente(4, 1, 1, 1, campoPessoa, jpComponentes);
        habilitaComponentes(false);
        //setSize(700, 300);
        pack();
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMaximumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCadastroUsuario();
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
        usuario.setId((Integer) campoCodigo.getValor());
        usuario.setUsuario(campoUsuario.getText());
        usuario.setSenha(campoSenha.getText());
        usuario.setStatus(campoStatus.getValor());
        usuario.getPessoa().setId((Integer) campoPessoa.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(usuario.getId());
        campoUsuario.setText(usuario.getUsuario());
        campoSenha.setText(usuario.getSenha());
        campoStatus.setValor(usuario.isStatus());
        campoPessoa.setValor(usuario.getPessoa().getId());
    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno = daoUsuario.incluir();
        getPersistencia();
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoUsuario.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoUsuario.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        usuario.setId(pk);
        daoUsuario.consultar();
        getPersistencia();
    }
    
    @Override
    public void confirmar() {
//        if (campoPessoa.verificaDuplicidadeUsuarios() && estadoTela == INCLUINDO) {
//            return;
//        } else {
            super.confirmar();
        //}
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Usuario",
                DaoUsuario.SQLPESQUISAR,
                new String[]{"Código", "Usuário", "Situação"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                    new FiltroPesquisa("Usuário", "USUARIO", String.class),
                    new FiltroPesquisa("Situação", "STATUS", String.class)
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                    new RenderizadorTexto()},
                this, this, false, false, false)
        );
    }
}
