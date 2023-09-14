package componentes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MeuJPasswordField extends JPanel implements FocusListener, MeuComponente {

    public String dica;
    public int colunas;
    private boolean obrigatorio;
    private boolean podeHabilitar;
    private JButton botaoRevelar = new JButton("*");
    private JPasswordField campoSenha = new JPasswordField();

    public MeuJPasswordField(boolean podeHabilitar, boolean obrigatorio, String dica) {
        this.podeHabilitar = podeHabilitar;
        this.obrigatorio = obrigatorio;
        this.dica = dica;
        addFocusListener(this);
        focusLost(null);
        setLayout(new FlowLayout());
        add(campoSenha);
        add(botaoRevelar);
        campoSenha.setEchoChar('•');
        campoSenha.setColumns(15);
        campoSenha.setPreferredSize(new Dimension(campoSenha.getColumns(), 24));
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!eVazio()) {
                            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                        }
                        if (eVazio() && eObrigatorio()) {
                            Color vermelhoClaro = new Color(255, 61, 61);
                            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, vermelhoClaro));
                        }
                    }
                });
            }
        });
        botaoRevelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (campoSenha.getEchoChar() == '•') {
                    campoSenha.setEchoChar('\u0000');
                } else {
                    campoSenha.setEchoChar('•');
                }
            }
        });
    }

    public String getDica() {
        return dica;
    }

    public void setDica(String dica) {
        this.dica = dica;
    }

    public void limpar() {
        campoSenha.setText("");
    }

    public void habilitar(boolean status) {
        campoSenha.setEnabled(podeHabilitar && status);
    }

    public boolean eObrigatorio() {
        return obrigatorio;
    }

    public boolean eVazio() {
        return campoSenha.getText().trim().isEmpty();
    }

    public boolean eValido() {
        return true;
    }

    public String getText() {
        return campoSenha.getText();
    }

    public void setText(String valor) {
        campoSenha.setText(valor);
    }

    @Override
    public void focusGained(FocusEvent fe) {
        Color azulBrilhante = new Color(207, 228, 247);
        campoSenha.setBackground(azulBrilhante);
        int lenght = campoSenha.getText().length();
        campoSenha.setCaretPosition(lenght);
        if (eObrigatorio() && eVazio()) {
            Color vermelhoClaro = new Color(255, 61, 61);
            campoSenha.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, vermelhoClaro));
        }
    }

    @Override
    public void focusLost(FocusEvent fe) {
        campoSenha.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        if (eObrigatorio()) {
            campoSenha.setBackground(Color.WHITE);
        } else {
            campoSenha.setBackground(Color.WHITE);
        }
    }


}