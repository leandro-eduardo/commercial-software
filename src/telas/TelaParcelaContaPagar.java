package telas;

import com.toedter.calendar.JDateChooser;
import componentes.MeuCampoInteger;
import dao.DaoParcelaContaPagar;
import java.awt.*;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import pojo.ParcelaContaPagar;

public class TelaParcelaContaPagar extends TelaCadastro {

    public static TelaParcelaContaPagar tela;
    public ParcelaContaPagar parcelaContaPagar = new ParcelaContaPagar();
    public DaoParcelaContaPagar daoParcelaContaPagar = new DaoParcelaContaPagar(parcelaContaPagar);
    
    public MeuCampoInteger campoCodigo = new MeuCampoInteger(6, false, false, false, "Código");
    private JLabel labelData = new JLabel("Nova Data: ");
    private JDateChooser campoData = new JDateChooser();
    
    private static Integer auxiliarCodigoContaPagar;


    public TelaParcelaContaPagar() {
        super("Alterar Vecimento Parcela Conta Pagar");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/contapagar.png")));
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

    public static TelaCadastro getTela(int codigoParcelaContaPagar, int codigoContaPagar, Date dataVencimento) {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaParcelaContaPagar();
            tela.addInternalFrameListener(new InternalFrameAdapter() { //Adiciona um listener para verificar quando a tela for fechada, fazendo assim a remoção da mesma junto ao JDesktopPane da TelaSistema e setando a variável tela = null para permitir que a tela possa ser aberta novamente em outro momento. Basicamente o mesmo controle efetuado pela tela de pesquisa, porém de uma forma um pouco diferente.
                @Override
                public void internalFrameClosed(InternalFrameEvent e) {
                    TelaSistema.jdp.remove(tela);
                    tela = null;
                }
            });
            tela.campoCodigo.setValor(codigoParcelaContaPagar);
            tela.campoData.setDate(dataVencimento);
            auxiliarCodigoContaPagar = codigoContaPagar;
            TelaSistema.jdp.add(tela);
        }
        //Depois do teste acima, independentemente dela já existir ou não, ela é selecionada e movida para frente
        TelaSistema.jdp.setSelectedFrame(tela);
        TelaSistema.jdp.moveToFront(tela);
        return tela;
    }

    public void setPersistencia() {
        parcelaContaPagar.setId((int) campoCodigo.getValor());
        parcelaContaPagar.setVencimento(campoData.getDate());
    }

    public void getPersistencia() {
        campoCodigo.setValor(parcelaContaPagar.getId());
        campoData.setDate(parcelaContaPagar.getVencimento());
    }

    @Override
    public boolean alterarBD() {
        setPersistencia();
        return daoParcelaContaPagar.alterar();
    }
    
    @Override
    public void confirmar() {
        if (estadoTela == ALTERANDO) {
            if (validaComponentes() && alterarBD()) {
                JOptionPane.showMessageDialog(null, "Data de vencimento alterada com sucesso!");
                this.dispose();
                TelaContaPagar.tela.tabelaParcela.getTableModel().limparDados();
                System.out.println("ID CONTA PAGAR = " + parcelaContaPagar.getContaPagar().getId());
                TelaContaPagar.tela.consultarBD(auxiliarCodigoContaPagar);
                TelaContaPagar.tela.habilitaComponentes(false);
                TelaContaPagar.tela.botaoGerarParcelas.setEnabled(false);
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
