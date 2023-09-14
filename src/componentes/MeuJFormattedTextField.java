package componentes;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

public class MeuJFormattedTextField extends JFormattedTextField implements MeuComponente, FocusListener {

    public boolean obrigatorio;
    public String dica;
    public int colunas;

    public MeuJFormattedTextField(int colunas, boolean obrigatorio, String dica) {
        setColumns(colunas);
        this.obrigatorio = obrigatorio;
        this.dica = dica;
        addFocusListener(this);
        focusLost(null);
//        addKeyListener(new KeyAdapter() { //foram retirados para padronizar os campos do sistema (antes deixavam a borda vermelha se fosse obrigatório e não estivesse preenchido
//            @Override
//            public void keyTyped(KeyEvent e) {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!eVazio()) {
//                            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
//                        }
//                        if (eVazio() && eObrigatorio()) {
//                            Color vermelhoClaro = new Color(255, 61, 61);
//                            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, vermelhoClaro));
//                        }
//                    }
//                });
//            }
//        });
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
        setText("");
    }

    @Override
    public boolean eVazio() {
        return getText().replace("_", "").replace("-", "").replace("(", "").replace(")", "").replace(".", "").trim().isEmpty();
    }

    @Override
    public boolean eValido() {
        return true;
    }

    @Override
    public void focusGained(FocusEvent fe) {
//        Color azulBrilhante = new Color(207, 228, 247); //retirada a cor azulBrilhante do campo quando ganha foco, para padronizar, pois o look and feel Plain já deixa as bordas azuis quando o campo está com foco
//        setBackground(azulBrilhante);
        if (eVazio()) {
            setCaretPosition(0);
        } else {
            int length = getText().length();
            setCaretPosition(length);
        }
//        if (eObrigatorio() && eVazio()) { //foram retirados para padronizar os campos do sistema (antes deixavam a borda vermelha se fosse obrigatório e não estivesse preenchido
//            Color vermelhoClaro = new Color(255, 61, 61);
//            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, vermelhoClaro));
//        }
    }

    @Override
    public void focusLost(FocusEvent fe) {
//        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); 
        if (eObrigatorio()) {
            setBackground(Color.WHITE);
        } else {
            setBackground(Color.WHITE);
        }
    }

    public Object getValor() {
        return getText();
    }

    public void setValor(Object valor) {
        setText("" + valor);
    }

}
