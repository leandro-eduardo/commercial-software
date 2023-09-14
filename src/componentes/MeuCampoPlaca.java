package componentes;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class MeuCampoPlaca extends MeuJFormattedTextField implements MeuComponente {

    public MeuCampoPlaca(int colunas, boolean obrigatorio, String dica) {
        super(colunas, obrigatorio, dica);
        try {
            MaskFormatter mf = new MaskFormatter("AAA-AAAA");
            mf.setPlaceholderCharacter(' ');
            mf.install(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar a placa.");
            e.printStackTrace();
        }
    }

    @Override
    public void limpar() {
        setText("");
    }

    @Override
    public boolean eValido() {
        return getText().replace("_", "").replace("-", "").trim().length() == 7;
    }

    @Override
    public boolean eObrigatorio() {
        return obrigatorio;
    }

    @Override
    public boolean eVazio() {
        return getText().replace("_", "").replace("-", "").trim().isEmpty();
    }

}
