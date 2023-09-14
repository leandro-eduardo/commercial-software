package componentes;

import javax.swing.*;

public class MeuJButton extends JButton implements MeuComponente {
    public MeuJButton(String texto) {
        super(texto);
    }

    public MeuJButton(Icon icone, boolean areaFilled, boolean borderPainted, boolean focusable) {
        setIcon(icone);
        setContentAreaFilled(areaFilled);
        setBorderPainted(borderPainted);
        setFocusable(focusable);
    }

    @Override
    public String getDica() {
        return "";
    }

    @Override
    public boolean eObrigatorio() {
        return false;
    }

    @Override
    public void limpar() {

    }

    @Override
    public void habilitar(boolean status) {
        setEnabled(status);
    }

    @Override
    public boolean eVazio() {
        return true;
    }

    @Override
    public boolean eValido() {
        return true;
    }
}