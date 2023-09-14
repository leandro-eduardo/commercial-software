package telas;

import componentes.MeuComponente;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class TelaCadastro extends JInternalFrame implements ActionListener {

    public final int PADRAO = 0;
    public final int INCLUINDO = 1;
    public final int ALTERANDO = 2;
    public final int EXCLUINDO = 3;
    public final int CONSULTANDO = 4;
    public int estadoTela = PADRAO;
    public boolean temDadosNaTela = false;
    public JPanel jpComponentes = new JPanel();
    public JPanel jpBotoes = new JPanel();
    public List<JLabel> rotulos = new ArrayList();
    public List<MeuComponente> componentes = new ArrayList();
    private Icon iconIncluir = new ImageIcon(getClass().getResource("/icones/incluir.png"));
    public JButton jbIncluir = new JButton("Incluir", iconIncluir);
    private Icon iconAlterar = new ImageIcon(getClass().getResource("/icones/alterar.png"));
    public JButton jbAlterar = new JButton("Alterar", iconAlterar);
    private Icon iconExcluir = new ImageIcon(getClass().getResource("/icones/excluir.png"));
    public JButton jbExcluir = new JButton("Excluir", iconExcluir);
    private Icon iconConsulta = new ImageIcon(getClass().getResource("/icones/consultar.png"));
    public JButton jbConsultar = new JButton("Consultar", iconConsulta);
    private Icon iconConfirmar = new ImageIcon(getClass().getResource("/icones/confirmar.png"));
    public JButton jbConfirmar = new JButton("Confirmar", iconConfirmar);
    private Icon iconCancelar = new ImageIcon(getClass().getResource("/icones/cancelar.png"));
    public JButton jbCancelar = new JButton("Cancelar", iconCancelar);   
    
    public TelaCadastro(String titulo) {
        super(titulo, true, true, true, true);
        //setTitle(titulo) mesma coisa que o comando anterior
        getContentPane().add("West", jpComponentes);
        getContentPane().add("South", jpBotoes);
        jpComponentes.setLayout(new GridBagLayout());
        jpBotoes.setLayout(new GridLayout());
        adicionaBotao(jbIncluir);
        adicionaBotao(jbAlterar);
        adicionaBotao(jbExcluir);
        adicionaBotao(jbConsultar);
        adicionaBotao(jbConfirmar);
        adicionaBotao(jbCancelar);
        setVisible(true);
        habilitaBotoes();
        //pack(); //calcula automaticamente o tamanho conforme os elementos da tela
        
    }

    public void centralizaTela() { //método chamado dentro do construtor de todas as telas, pois se chamado no construtor da TelaCadastro a tela não fica no completamente no meio
        Dimension tamanhoTela = getSize();
        Dimension tamanhoJDesktopPane = TelaSistema.jdp.getSize();
        int x = (tamanhoJDesktopPane.width - tamanhoTela.width) / 2;
        int y = (tamanhoJDesktopPane.height - tamanhoTela.height) / 2; //se quiser deixar totalmente no meio da tela basta dividir por 2 em vez de 20. 20 é utilizado para deixar a 
        setLocation(x, y);                                              //tela um pouco mais para cima
    }

    private void adicionaBotao(JButton botao) {
        jpBotoes.add(botao);
        botao.addActionListener(this);
    }

    public void adicionaComponente(int linha, int coluna, int linhas, int colunas, JComponent componente, JPanel painel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridy = linha;
        gbc.gridx = coluna;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        if (componente instanceof MeuComponente) {
            String texto = "<html><body>" + ((MeuComponente) componente).getDica() + ": ";
            if (((MeuComponente) componente).eObrigatorio()) {
                texto = texto + "<font color=red>*</font>";
            }
            texto = texto + "</body></html>";
            JLabel rotulo = new JLabel(texto);
            painel.add(rotulo, gbc);
            rotulos.add(rotulo);
            componentes.add((MeuComponente) componente);
        }
        gbc.gridheight = linhas;
        gbc.gridwidth = colunas;
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(componente, gbc);
    }
    
    public void adicionaComponenteArray(JComponent componente) {
        componentes.add((MeuComponente) componente);
    }

    public void habilitaComponentes(boolean status) {
        for (int i = 0; i < componentes.size(); i++) {
            componentes.get(i).habilitar(status);
        }
    }

    public void removeComponente(JComponent componente) {
        for (int i = 0; i < componentes.size(); i++) {
            if (componentes.get(i) == componente) {
                jpComponentes.remove(rotulos.get(i));
                jpComponentes.remove((JComponent) componentes.get(i));
                rotulos.remove(i);
                componentes.remove(i);
            }
        }
    }
    
    public void removeComponente(JComponent componente, JPanel painel) {
        for (int i = 0; i < componentes.size(); i++) {
            if (componentes.get(i) == componente) {
                painel.remove(rotulos.get(i));
                painel.remove((JComponent) componentes.get(i));
                rotulos.remove(i);
                componentes.remove(i);
            }
        }
    }
    
    public void removeBotao(JButton botao) {
        jpBotoes.remove((JButton) botao);
    }

    public JLabel getRotulo(JComponent componente) {
        for (int i = 0; i < componentes.size(); i++) {
            if (componentes.get(i) == componente) {
                return rotulos.get(i);
            }
        }
        return null;
    }

    public boolean validaComponentes() {
        String errosObrigatorios = "", errosInvalidos = "", errosTotal = "";
        for (int i = 0; i < componentes.size(); i++) {
            if (componentes.get(i).eObrigatorio() && componentes.get(i).eVazio()) {
                errosObrigatorios = errosObrigatorios + componentes.get(i).getDica() + "\n";
            }
            if (!componentes.get(i).eValido()) {
                errosInvalidos = errosInvalidos + componentes.get(i).getDica() + "\n";
            }
        }
        if (!errosObrigatorios.isEmpty()) {
            errosTotal = "Os campos abaixo são obrigatórios e não foram preenchidos: \n\n" + errosObrigatorios;

        }
        if (!errosInvalidos.isEmpty()) {
            errosTotal = errosTotal + "\n\n\n" + "Os campos abaixo estão inválidos: \n\n" + errosInvalidos;
        }
        if (!errosTotal.isEmpty()) {
            JOptionPane.showMessageDialog(null, errosTotal);
        }
        return errosTotal.isEmpty();
    }

    public void habilitaBotoes() {
        jbIncluir.setEnabled(estadoTela == PADRAO);
        jbAlterar.setEnabled(estadoTela == PADRAO && temDadosNaTela);
        jbExcluir.setEnabled(estadoTela == PADRAO && temDadosNaTela);
        jbConsultar.setEnabled(estadoTela == PADRAO);
        jbConfirmar.setEnabled(estadoTela != PADRAO);
        jbCancelar.setEnabled(estadoTela != PADRAO);
    }
    
    public void adicionaScrollPane() {
        //adicionar JScrollPane
        JScrollPane scroller = new JScrollPane(jpComponentes); // Create a new scroll Pane with the subpanel a param
        //scroller.setBounds(50, 50, 800, 600); // Set the bounds of the scrollPane
        getContentPane().add(scroller); // Adds the JScrollPane to the container

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == jbIncluir) {
            incluir();
        } else if (ae.getSource() == jbAlterar) {
            alterar();
        } else if (ae.getSource() == jbExcluir) {
            excluir();
        } else if (ae.getSource() == jbConsultar) {
            consultar();
        } else if (ae.getSource() == jbConfirmar) {
            confirmar();
        } else if (ae.getSource() == jbCancelar) {
            cancelar();
        }
    }

    public void incluir() {
        estadoTela = INCLUINDO;
        habilitaComponentes(true);
        limpar();
        habilitaBotoes();
    }

    public void alterar() {
        System.out.println("STAYHR");
        estadoTela = ALTERANDO;
        habilitaComponentes(true);
        habilitaBotoes();
    }

    public void excluir() {
        estadoTela = EXCLUINDO;
        habilitaBotoes();
    }

    public void consultar() {
        estadoTela = PADRAO; //modificado o estado de CONSULTANDO para PADRAO pois não há necessidade de confirmar ou cancelar a consulta,
        habilitaBotoes();    //já que os dados são carregados caso ocorra um clique duplo na linha da tabela (confirmar) e o usuário pode fechar
                             //a tela de consulta (cancelar)
    }

    public void confirmar() {
        if (estadoTela == INCLUINDO) {
            if (validaComponentes() && incluirBD()) {
                JOptionPane.showMessageDialog(null, "Cadastro efetuado com sucesso!");
            } else {
                return;
            }
            temDadosNaTela = true;
        } else if (estadoTela == ALTERANDO) {
            if (validaComponentes() && alterarBD()) {
                JOptionPane.showMessageDialog(null, "Cadastro alterado com sucesso!");
            } else {
                return;
            }
        } else if (estadoTela == EXCLUINDO) {
            int opcao = JOptionPane.showConfirmDialog(null, "Confirma a exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if ((opcao != JOptionPane.OK_OPTION) || (!excluirBD())) {
                return;
            }
            limpar();
            temDadosNaTela = false;
        }
        estadoTela = PADRAO;
        habilitaComponentes(false);
        habilitaBotoes();
    }

    public void limpar() {
        for (int i = 0; i < componentes.size(); i++) {
            componentes.get(i).limpar();
        }
    }

    public void cancelar() {
        estadoTela = PADRAO;
        habilitaComponentes(false);
        limpar();
        temDadosNaTela = false;
        habilitaBotoes();
    }

    public boolean incluirBD() {
        return true;
        //Método a ser redefinido nas sub-classes
    }

    public boolean alterarBD() {
        return true;
        //Método a ser redefinido nas sub-classes
    }

    public boolean excluirBD() {
        return true;
        //Método a ser redefinido nas sub-classes
    }

    public void pesquisaSemDados() {
        estadoTela = PADRAO;
        habilitaBotoes();
    }

    public void consultarBD(int pk) {
        estadoTela = PADRAO;
        temDadosNaTela = true;
        habilitaBotoes();
        jbCancelar.setEnabled(true);
    }

}
    

