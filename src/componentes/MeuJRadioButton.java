package componentes;

import javax.swing.JLabel;
import javax.swing.JRadioButton;

public class MeuJRadioButton extends JRadioButton implements MeuComponente {

    private boolean podeHabilitar;
    private boolean obrigatorio;

    public MeuJRadioButton(String texto, boolean obrigatorio, boolean podeHabilitar, boolean selected) {
        super(texto, selected);
        this.podeHabilitar = podeHabilitar;
        this.obrigatorio = obrigatorio;
    }

    @Override
    public void limpar() {

    }

    @Override
    public void habilitar(boolean status) {
        setEnabled(status && podeHabilitar); // habilitar ou desabilitar
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
        return getText().trim().isEmpty();
    }

    public void setValor(Object valor) {
        setText("" + valor);
    }

    public Object getValor() {
        return "" + getText();

    }

    @Override
    public String getDica() {
        return "";
    }

    public void setLabel(JLabel rotulo) {

    }

    public void setLabelVisible(boolean status) {

    }

    public void recebeFoco() {
        this.requestFocus();
    }

}
