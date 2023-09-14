package telas;

import com.toedter.calendar.JDateChooser;
import componentes.MeuCampoInteger;
import dao.DaoParcelaContaReceber;
import java.awt.*;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import pojo.ParcelaContaReceber;

public class TelaParcelaContaReceber extends TelaCadastro {

    public static TelaParcelaContaReceber tela;
    public ParcelaContaReceber parcelaContaReceber = new ParcelaContaReceber();
    public DaoParcelaContaReceber daoParcelaContaReceber = new DaoParcelaContaReceber(parcelaContaReceber);
    
    public MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private JLabel labelData = new JLabel("Nova Data: ");
    private JDateChooser campoData = new JDateChooser();
    
    private static Integer auxiliarCodigoContaReceber;


    public TelaParcelaContaReceber() {
        super("Alterar Vecimento Parcela Conta Receber");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/contareceber.png")));
        adicionaComponente(1, 0, 1, 1, campoCodigo, jpComponentes);
        adicionaComponente(1, 1, 1, 1, labelData, jpComponentes);
        adicionaComponente(1, 2, 1, 1, campoData, jpComponentes);

        removeBotao(jbExcluir);
        removeBotao(jbAlterar);
        removeBotao(jbConsultar);
        removeBotao(jbIncluir);
        
        estadoTela = ALTERANDO;
        
        habilitaComponentes(true);
        habilitaBotoes();
        setSize(360, 110);
        //pack();
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setMaximumSize(new Dimension(getWidth(), getHeight()));
        centralizaTela();
    }

    public static TelaCadastro getTela(int codigoParcelaContaReceber, int codigoContaReceber, Date dataVencimento) {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaParcelaContaReceber();
            tela.addInternalFrameListener(new InternalFrameAdapter() { //Adiciona um listener para verificar quando a tela for fechada, fazendo assim a remoção da mesma junto ao JDesktopPane da TelaSistema e setando a variável tela = null para permitir que a tela possa ser aberta novamente em outro momento. Basicamente o mesmo controle efetuado pela tela de pesquisa, porém de uma forma um pouco diferente.
                @Override
                public void internalFrameClosed(InternalFrameEvent e) {
                    TelaSistema.jdp.remove(tela);
                    tela = null;
                }
            });
            tela.campoCodigo.setValor(codigoParcelaContaReceber);
            tela.campoData.setDate(dataVencimento);
            auxiliarCodigoContaReceber = codigoContaReceber;
            TelaSistema.jdp.add(tela);
        }
        //Depois do teste acima, independentemente dela já existir ou não, ela é selecionada e movida para frente
        TelaSistema.jdp.setSelectedFrame(tela);
        TelaSistema.jdp.moveToFront(tela);
        return tela;
    }

    public void setPersistencia() {
        parcelaContaReceber.setId((int) campoCodigo.getValor());
        parcelaContaReceber.setVencimento(campoData.getDate());
    }

    public void getPersistencia() {
        campoCodigo.setValor(parcelaContaReceber.getId());
        campoData.setDate(parcelaContaReceber.getVencimento());
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoParcelaContaReceber.alterar();
    }
    
    @Override
    public void confirmar() {
        if (estadoTela == ALTERANDO) {
            if (validaComponentes() && alterarBD()) {
                JOptionPane.showMessageDialog(null, "Data de vencimento alterada com sucesso!");
                this.dispose();
                TelaContaReceber.tela.tabelaParcela.getTableModel().limparDados();
                System.out.println("ID CONTA RECEBER = " + parcelaContaReceber.getContaReceber().getId());
                TelaContaReceber.tela.consultarBD(auxiliarCodigoContaReceber);
                TelaContaReceber.tela.habilitaComponentes(false);
                TelaContaReceber.tela.botaoGerarParcelas.setEnabled(false);
            } else {
                return;
            }
        }
    }
    
    @Override
    public void cancelar() {
        super.cancelar();
        campoData.setEnabled(false);
        dispose();
    }

}
