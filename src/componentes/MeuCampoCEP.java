package componentes;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class MeuCampoCEP extends MeuJFormattedTextField implements MeuComponente {

    public MeuCampoCEP(int colunas, boolean obrigatorio, String dica) {
        super(colunas, obrigatorio, dica);
        try {
            MaskFormatter mf = new MaskFormatter("#####-###");
            mf.setPlaceholderCharacter(' ');
            mf.install(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar o CEP");
            e.printStackTrace();
        }
    }

    @Override
    public void limpar() {
        setText("");
    }

    @Override
    public boolean eValido() {
        if (!eObrigatorio() && eVazio()) {
            return true;
        }
        return getText().replace("_", "").replace("-", "").trim().length() == 8;
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
