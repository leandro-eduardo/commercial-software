package telas;

import componentes.FiltroPesquisa;
import componentes.MeuCampoBuscar;
import componentes.MeuCampoCEL;
import componentes.MeuCampoCEP;
import componentes.MeuCampoCNPJ;
import componentes.MeuCampoCPF;
import componentes.MeuCampoCheckBox;
import componentes.MeuCampoData;
import componentes.MeuCampoInteger;
import componentes.MeuCampoTEL;
import componentes.MeuJComboBox;
import componentes.MeuJTextArea;
import componentes.MeuJTextField;
import dao.DaoCidade;
import dao.DaoPessoa;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import pojo.ParametrosConsulta;
import pojo.Pessoa;
import renderizador.InteiroRender;
import renderizador.RenderizadorTexto;
import util.RestricaoCaracteres;

public class TelaCadastroPessoa extends TelaCadastro {

    public static TelaCadastroPessoa tela;
    public ParametrosConsulta parametrosConsulta =
            new ParametrosConsulta("Consulta de Cidade",
                    DaoCidade.SQLPESQUISAR2,
                    new String[]{"Código", "Nome", "Estado", "Situação"},
                    new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                            new FiltroPesquisa("Nome", "NOME", String.class),
                            new FiltroPesquisa("Estado", "ESTADO_NOME", String.class),
                            new FiltroPesquisa("Situação", "ATIVO", String.class)
                    },
                    new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(),
                            new RenderizadorTexto(), new RenderizadorTexto()},
                    this, this, false, false, false
            );
    public MeuCampoBuscar campoCidade = new MeuCampoBuscar(TelaConsultaFiltro.class, TelaCadastroCidade.class, DaoCidade.SQLCOMBOBOX, DaoCidade.SQLINATIVOS, parametrosConsulta, true, true, "Cidade", 30);
    private Pessoa pessoa = new Pessoa();
    private DaoPessoa daoPessoa = new DaoPessoa(pessoa);
    public MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private MeuJTextField campoNomeRazaoSocial = new MeuJTextField(40, true, true, "Nome/Razão Social");
    private MeuJTextField campoApelidoNomeFantasia = new MeuJTextField(20, true, false, "Apelido/Fantasia");
    private MeuJComboBox campoTipo = new MeuJComboBox(true, "Tipo", new String[][]{{"F", "Física"}, {"J", "Jurídica"}});
    private MeuJComboBox campoSexo = new MeuJComboBox(true, "Sexo", new String[][]{{"M", "Masculino"}, {"F", "Feminino"}});
    private MeuCampoCPF campoCPF = new MeuCampoCPF(11, true, "CPF/CNPJ");
    private MeuCampoCNPJ campoCNPJ = new MeuCampoCNPJ(11, true, "CPF/CNPJ");
    private MeuCampoData campoDataNascimento = new MeuCampoData(6, false, "Nascimento");
    private MeuJTextField campoRGIE = new MeuJTextField(8, true, false, "RG/IE");
    private MeuJTextField campoEndereco = new MeuJTextField(29, true, false, "Endereço");
    private MeuJTextField campoNumero = new MeuJTextField(4, true, false, "Número");
    private MeuJTextField campoComplemento = new MeuJTextField(15, true, false, "Comp");
    private MeuJTextField campoBairro = new MeuJTextField(15, true, false, "Bairro");
    private MeuCampoCEP campoCEP = new MeuCampoCEP(6, false, "CEP");
    private MeuCampoCEL campoFone1 = new MeuCampoCEL(9, false, "Celular");
    private MeuCampoTEL campoFone2 = new MeuCampoTEL(8, false, "Telefone");
    private MeuJTextField campoEmail = new MeuJTextField(20, true, false, "E-mail");
    private MeuJTextField campoSite = new MeuJTextField(20, true, false, "Site");
    private MeuCampoCheckBox campoECliente = new MeuCampoCheckBox(true, true, "Cliente");
    private MeuCampoCheckBox campoEFornecedor = new MeuCampoCheckBox(false, true, "Fornecedor");
    private MeuCampoCheckBox campoEFuncionario = new MeuCampoCheckBox(false, true, "Funcionário");
    private MeuCampoCheckBox campoAtivo = new MeuCampoCheckBox(true, true, "Ativo");
    private MeuJTextArea campoObservacao = new MeuJTextArea("Observação", false, 450, 60);
    private JPanel painelStatus = new JPanel();
    private JPanel painelCFF = new JPanel();   
    

    public TelaCadastroPessoa() {
        super("Cadastro de Pessoa");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pessoa.png")));
       
        adicionaComponente(1, 0, 1, 1, campoCodigo, jpComponentes);
        adicionaComponente(3, 0, 1, 1, campoTipo, jpComponentes);
        campoNomeRazaoSocial.setDocument(new RestricaoCaracteres(55));
        adicionaComponente(4, 0, 1, 1, campoNomeRazaoSocial, jpComponentes);
        campoApelidoNomeFantasia.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(5, 0, 1, 1, campoApelidoNomeFantasia, jpComponentes);
        adicionaComponente(6, 0, 1, 1, campoSexo, jpComponentes);
        adicionaComponente(7, 0, 1, 1, campoDataNascimento, jpComponentes);
        adicionaComponente(8, 0, 1, 1, campoCPF, jpComponentes);
        campoRGIE.setDocument(new RestricaoCaracteres(15));
        adicionaComponente(9, 0, 1, 1, campoRGIE, jpComponentes);
        campoEndereco.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(10, 0, 1, 1, campoEndereco, jpComponentes);
        campoNumero.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(11, 0, 1, 1, campoNumero, jpComponentes);
        campoComplemento.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(12, 0, 1, 1, campoComplemento, jpComponentes);
        campoBairro.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(13, 0, 1, 1, campoBairro, jpComponentes);
        adicionaComponente(14, 0, 1, 1, campoCEP, jpComponentes);
        adicionaComponente(15, 0, 1, 1, campoCidade, jpComponentes);
        adicionaComponente(16, 0, 1, 1, campoFone1, jpComponentes);
        adicionaComponente(17, 0, 1, 1, campoFone2, jpComponentes);

        campoEmail.setDocument(new RestricaoCaracteres(45));
        adicionaComponente(18, 0, 1, 1, campoEmail, jpComponentes);    
        adicionaComponente(22, 0, 1, 1, campoObservacao, jpComponentes);     
        
        //painelCFF
        painelCFF.setBorder(BorderFactory.createTitledBorder("CFF"));
        adicionaComponente(2, 0, 1, 1, painelCFF, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoECliente, painelCFF);
        adicionaComponente(2, 1, 1, 1, campoEFornecedor, painelCFF);
        adicionaComponente(3, 1, 1, 1, campoEFuncionario, painelCFF);
        
        //painelStatus
        painelStatus.setBorder(BorderFactory.createTitledBorder("Situação"));
        adicionaComponente(1, 3, 1, 1, painelStatus, jpComponentes);
        adicionaComponente(1, 1, 1, 1, campoAtivo, painelStatus);
      
        habilitaComponentes(false);
        adicionaListeners();
        adicionaScrollPane();
        setSize(800, 630);          
        //pack();
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMaximumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();
    }

    public static TelaCadastro getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaCadastroPessoa();
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
        pessoa.setId((Integer) campoCodigo.getValor());
        pessoa.setNomerazaosocial(campoNomeRazaoSocial.getText());
        pessoa.setApelidonomefantasia(campoApelidoNomeFantasia.getText());
        pessoa.setTipo((String) campoTipo.getValor());
        if (campoTipo.getValor() == "F") {
            pessoa.setCpfcnpj((String) campoCPF.getValor());

        } else if (campoTipo.getValor() == "J") {
            pessoa.setCpfcnpj((String) campoCNPJ.getValor());
        }
        pessoa.setRgie(campoRGIE.getText());
        pessoa.setSexo((String) campoSexo.getValor());
        pessoa.setDatanascimento(campoDataNascimento.getValorDate());
        pessoa.setEndereco(campoEndereco.getText());
        pessoa.setNumero(campoNumero.getText());
        pessoa.setComplemento(campoComplemento.getText());
        pessoa.setBairro(campoBairro.getText());
        pessoa.setCep(campoCEP.getText());
        pessoa.getCidade().setId((Integer) campoCidade.getValor());
        pessoa.setFone1(campoFone1.getText());
        pessoa.setFone2(campoFone2.getText());
        pessoa.setEmail(campoEmail.getText());
        pessoa.setSite(campoSite.getText());
        pessoa.setEcliente(campoECliente.getValor());
        pessoa.setEfornecedor(campoEFornecedor.getValor());
        pessoa.setEfuncionario(campoEFuncionario.getValor());
        pessoa.setAtivo(campoAtivo.getValor());
        pessoa.setObservacao(campoObservacao.getValor());
    }

    public void getPersistencia() {
        campoCodigo.setValor(pessoa.getId());
        campoNomeRazaoSocial.setText(pessoa.getNomerazaosocial());
        campoApelidoNomeFantasia.setText(pessoa.getApelidonomefantasia());
        campoTipo.setValor(pessoa.getTipo());
        if (campoTipo.getValor() == "F") {
            campoCPF.setText(pessoa.getCpfcnpj());
        } else if (campoTipo.getValor() == "J") {
            campoCNPJ.setText(pessoa.getCpfcnpj());
        }
        campoRGIE.setText(pessoa.getRgie());
        campoSexo.setValor(pessoa.getSexo());
        campoDataNascimento.setValor(pessoa.getDatanascimento());
        campoEndereco.setText(pessoa.getEndereco());
        campoNumero.setText(pessoa.getNumero());
        campoComplemento.setText(pessoa.getComplemento());
        campoBairro.setText(pessoa.getBairro());
        campoCEP.setText(pessoa.getCep());
        campoCidade.setValor(pessoa.getCidade().getId());
        campoFone1.setText(pessoa.getFone1());
        campoFone2.setText(pessoa.getFone2());
        campoEmail.setText(pessoa.getEmail());
        campoSite.setText(pessoa.getSite());
        campoECliente.setValor(pessoa.isEcliente());
        campoEFornecedor.setValor(pessoa.isEfornecedor());
        campoEFuncionario.setValor(pessoa.isEfuncionario());
        campoAtivo.setValor(pessoa.isAtivo());
        campoObservacao.setValor(pessoa.getObservacao());
    }

    private void adicionaListeners() {
        campoTipo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                removeComponente(campoCNPJ);
                removeComponente(campoCPF);
                if (estadoTela == PADRAO) {
                    campoCNPJ.habilitar(false);
                } else {
                    campoCNPJ.habilitar(true);
                }
                if (campoTipo.getSelectedIndex() == 0) {
                     adicionaComponente(8, 0, 1, 1, campoCPF, jpComponentes);
                    if (estadoTela == INCLUINDO) {
                        campoCPF.setEnabled(true);
                        //campoDataNascimento.setEnabled(true);
                    } else {
                        campoDataNascimento.limpar();
                        //campoDataNascimento.setEnabled(false);
                        campoCPF.setEnabled(false);
                    }
                } else if (campoTipo.getSelectedIndex() == 1) {
                    adicionaComponente(8, 0, 1, 1, campoCNPJ, jpComponentes);
                    //campoDataNascimento.setEnabled(false);
                    campoCNPJ.limpar();
                }
                setSize(800, 630); 
                //pack();
            }
        });
//        campoEFuncionario.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                removeComponente(campoLogin);
//                removeComponente(campoSenha);
//                if (estadoTela == PADRAO) {
//                    campoLogin.habilitar(false);
//                    campoSenha.habilitar(false);
//                } else {
//                    campoLogin.habilitar(true);
//                    campoSenha.habilitar(true);
//                }
//                if (campoEFuncionario.isSelected()) {
//                    adicionaComponente(20, 0, 1, 1, campoLogin, jpComponentes);
//                    adicionaComponente(21, 0, 1, 1, campoSenha, jpComponentes);
//                    campoLogin.limpar();
//                    campoSenha.limpar();
//                } else {
//                    removeComponente(campoLogin);
//                    removeComponente(campoSenha);
//                }
//                setSize(800, 630); 
//                //pack();
//            }
//        });
//        campoEFuncionario.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent ie) {
//                removeComponente(campoLogin); 
//               removeComponente(campoSenha);
//                if (estadoTela == PADRAO) {
//                    campoLogin.habilitar(false);
//                    campoSenha.habilitar(false);
//                } else {
//                    campoLogin.habilitar(true);
//                    campoSenha.habilitar(true);
//                }
//                if (ie.getStateChange() == ItemEvent.SELECTED) {
//                    adicionaComponente(20, 0, 1, 1, campoLogin, jpComponentes);
//                    adicionaComponente(21, 0, 1, 1, campoSenha, jpComponentes);
//                } else {
//                    removeComponente(campoLogin);
//                    removeComponente(campoSenha);
//                }
//                setSize(800, 630); 
//                //pack();
//            }
//        });
        campoEFornecedor.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                removeComponente(campoSite);
                if (estadoTela == PADRAO) {
                    campoSite.habilitar(false);
                } else {
                    campoSite.habilitar(true);
                }
                if (campoEFornecedor.isSelected()) {
                    adicionaComponente(19, 0, 1, 1, campoSite, jpComponentes);
                } else {
                    removeComponente(campoSite);
                }
                setSize(800, 630); 
                //pack();
            }
        });
        campoEFornecedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                removeComponente(campoSite);
                if (campoEFornecedor.isSelected()) {
                    adicionaComponente(19, 0, 1, 1, campoSite, jpComponentes);
                    campoSite.limpar();
                } else {
                    removeComponente(campoSite);
                }
                setSize(800, 630);
                //pack();
            }
        });

    }

    @Override
    public boolean incluirBD() {
        setPersistencia();
        boolean retorno = daoPessoa.incluir();
        getPersistencia();
        return retorno;
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoPessoa.alterar();
    }

    @Override
    public boolean excluirBD() {
        setPersistencia();
        return daoPessoa.excluir();
    }

    @Override
    public void consultarBD(int pk) {
        super.consultarBD(pk);
        pessoa.setId(pk);
        daoPessoa.consultar();
        getPersistencia();
    }

    @Override
    public void consultar() {
        super.consultar();
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Pessoa",
                DaoPessoa.SQLPESQUISAR,
                new String[]{"Código", "Nome", "CPF/CNPJ", "Cidade", "Situação"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                        new FiltroPesquisa("Nome", "NOMERAZAOSOCIAL", String.class),
                        new FiltroPesquisa("CPF/CNPJ", "CPFCNPJ", String.class),
                        new FiltroPesquisa("Cidade", "CIDADE", String.class),
                        new FiltroPesquisa("Situação", "ATIVO", String.class)
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new RenderizadorTexto(),
                        new RenderizadorTexto(), new RenderizadorTexto()
                },
                this, this, false, true, false)
        );
    }

}
