package componentes;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

public class MeuJTextField extends JTextField implements FocusListener, MeuComponente {

    public String dica;
    public int colunas;
    private boolean obrigatorio;
    private boolean podeHabilitar;

    public MeuJTextField(int colunas, boolean podeHabilitar, boolean obrigatorio, String dica) {
        super(colunas);

        //setColumns(colunas); pode ser usado tanto o comando acima quanto o setColumns(colunas)
        this.podeHabilitar = podeHabilitar;
        this.obrigatorio = obrigatorio;
        this.dica = dica;
        addFocusListener(this);
        focusLost(null);
        setPreferredSize(new Dimension(colunas, 24));
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

    public String getDica() {
        return dica;
    }

    public void setDica(String dica) {
        this.dica = dica;
    }

    public void limpar() {
        setText("");
    }

    public void habilitar(boolean status) {
        setEnabled(podeHabilitar && status);
    }

    public boolean eObrigatorio() {
        return obrigatorio;
    }

    public boolean eVazio() {
        return getText().trim().isEmpty();
    }

    public boolean eValido() {
        return true;
    }

    @Override
    public void focusGained(FocusEvent fe) {
//      Color azulBrilhante = new Color(207, 228, 247); //retirada a cor azulBrilhante do campo quando ganha foco, para padronizar, pois o look and feel Plain já deixa as bordas azuis quando o campo está com foco
//      setBackground(azulBrilhante);
        int lenght = getText().length();
        setCaretPosition(lenght);
//      if (eObrigatorio() && eVazio()) { //foram retirados para padronizar os campos do sistema (antes deixavam a borda vermelha se fosse obrigatório e não estivesse preenchido
//          Color vermelhoClaro = new Color(255, 61, 61);
//          setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, vermelhoClaro));
//      }
    }

    @Override
    public void focusLost(FocusEvent fe) {
//       setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        if (eObrigatorio()) {
            setBackground(Color.WHITE);
        } else {
            setBackground(Color.WHITE);
        }
    }

    public Object getValor() {
        return getText().trim();
    }

    public void setValor(String valor) {
        setText(valor);
    }

}
