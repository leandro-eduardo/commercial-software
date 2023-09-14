package componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MeuJComboBox extends JComboBox implements FocusListener, MeuComponente {

    private boolean obrigatorio;
    private String dica;
    private String[][] valores;

    public MeuJComboBox(boolean obrigatorio, String dica, String[][] valores) {
        this.obrigatorio = obrigatorio;
        this.dica = dica;
        this.valores = valores;
        addFocusListener(this);
        montaComboBox();

    }

    private void montaComboBox() {
        //addItem("Selecione...");
        for (String[] valor : valores) {
            addItem(valor[1]);
        }
    }

    public Object getValor() {
        return valores[getSelectedIndex()][0];

    }

    public void setValor(Object valor) {
        for (String[] valorTemp : valores) {
            if (valorTemp[0].equals(valor)) {
                setSelectedItem(valorTemp[1]);
            }
        }
    }

    @Override
    public String getDica() {
        return dica;
    }

    @Override
    public boolean eObrigatorio() {
        return obrigatorio;
    }

    @Override
    public void habilitar(boolean status) {
        setEnabled(status);
    }

    @Override
    public void limpar() {
        setSelectedIndex(0);
    }

    @Override
    public boolean eVazio() {
        //return getSelectedIndex() < 1;
        return false;
    }

    @Override
    public boolean eValido() {
        return true;
    }

    @Override
    public void focusGained(FocusEvent fe) {
        Color azulBrilhante = new Color(192, 217, 217);
        setBackground(azulBrilhante);
    }

    @Override
    public void focusLost(FocusEvent fe) {
        setBackground(Color.WHITE);
    }
}
