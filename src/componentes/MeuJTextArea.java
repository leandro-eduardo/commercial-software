package componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MeuJTextArea extends JScrollPane implements MeuComponente, FocusListener {

    public JTextArea jta = new JTextArea();
    public boolean obrigatorio;
    public String dica;

    public MeuJTextArea(String dica, boolean obrigatorio, int largura, int altura) {
        this.obrigatorio = obrigatorio;
        this.dica = dica;
        setPreferredSize(new Dimension(largura, altura));//Altura e largura em Pixels 
        jta.setLineWrap(true);
        setViewportView(jta);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //setPreferredSize(new Dimension(222, 80));
        //setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        //setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //setAutoscrolls(true);
        jta.addFocusListener(this);
    }

    @Override
    public void limpar() {
        jta.setText("");
    }

    @Override
    public boolean eVazio() {
        return jta.getText().trim().isEmpty();
    }

    @Override
    public boolean eObrigatorio() {
        return obrigatorio;
    }

    @Override
    public boolean eValido() {
        return true;
    }

    @Override
    public String getDica() {
        return dica;
    }

    public String getValor() {
        return jta.getText();
    }

    public void setValor(Object valor) {
        jta.setText("" + valor);
    }

    @Override
    public void habilitar(boolean status) {
        jta.setEnabled(status);
    }

    @Override
    public void focusGained(FocusEvent fe) {
        jta.setBackground(Color.lightGray);
    }

    @Override
    public void focusLost(FocusEvent fe) {
        if (eObrigatorio()) {
            jta.setBackground(Color.WHITE);
        } else {
            jta.setBackground(Color.WHITE);
        }
    }
}
