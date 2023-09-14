package componentes;



//classe não está sendo utilizada.. apenas mantida a fim de utilizá-la porteiormente
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class MeuCampoSexo extends JRadioButton implements MeuComponente, ItemListener {
    
    private boolean obrigatorio;
    private String dica;
    ButtonGroup group = new ButtonGroup();
    JRadioButton masculino = new JRadioButton("Masculino");
    JRadioButton feminino = new JRadioButton("Feminino");
    
    public MeuCampoSexo(boolean obrigatorio, String dica) {
        this.obrigatorio = obrigatorio;
        this.dica = dica;
        group.add(masculino);
        group.add(feminino);
        setLayout(new GridLayout(1,2));
        add(masculino);
        add(feminino);
        masculino.addItemListener(this);
        feminino.addItemListener(this);
    }
    
    public String getValor() {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    return button.getText();
                }
            }
        return null;
    }

    public void setValor(Object valor) {
         
    }

    @Override
    public void limpar() {
        masculino.setSelected(true);
    }

    @Override
    public boolean eValido() {
        return true;
    }

    @Override
    public boolean eObrigatorio() {
        return obrigatorio;
    }

    @Override
    public boolean eVazio() {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    return false;
                }
            }
        return true;
    }

    @Override
    public String getDica() {
        return dica;
    }

    @Override
    public void habilitar(boolean status) {
        setEnabled(status);
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        if(masculino.isSelected()){
            System.out.println("Masculino");
        } else{
            System.out.println("Feminino");
        }
    }

}
