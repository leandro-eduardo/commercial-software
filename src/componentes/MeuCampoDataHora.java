package componentes;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class MeuCampoDataHora extends MeuJFormattedTextField implements MeuComponente {
    private boolean obrigatorio;
    private String nome;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public MeuCampoDataHora(int colunas, boolean obrigatorio, String nome) {
        super(colunas, obrigatorio, nome);
        this.obrigatorio = obrigatorio;
        this.nome = nome;
        setPreferredSize(new Dimension(colunas, 24));
//        try {
//            MaskFormatter mf = new MaskFormatter("##:##:####"); //try catch comentado pois não há necessidade de colocar máscara no campo de hora na tela de movimento de caixa
//            mf.setPlaceholderCharacter(' ');
//            mf.install(this);
//        } catch (Exception e) {
//            e.printStackTrace(); //mostra um relatório de onde ocorreu o erro
//        }
    }

    @Override
    public boolean eVazio() {
        return getText().replace(":", "").replace("/", "").replace(".", "").replace("_", "").trim().isEmpty();
    }

    @Override
    public void limpar() {
        setText("");
    }

    @Override
    public void habilitar(boolean status) {
        setEnabled(status);
    }

    @Override
    public void setValor(Object valor) {
        if (valor == null) {
            setText("");
        } else {
            setText(sdf.format((Object) valor));
        }
    }

    public Date getValorHora() {
        if (eVazio()) {
            return null;
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                return sdf.parse(getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Não foi possível obter a hora.");
                e.printStackTrace();
                return null;
            }
        }
    }

}
