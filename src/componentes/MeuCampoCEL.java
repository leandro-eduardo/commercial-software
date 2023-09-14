package componentes;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class MeuCampoCEL extends MeuJFormattedTextField implements MeuComponente {

    public MeuCampoCEL(int colunas, boolean obrigatorio, String dica) {
        super(colunas, obrigatorio, dica);
        try {
            MaskFormatter mf = new MaskFormatter("(##) # ####-####");
            mf.setPlaceholderCharacter(' ');
            mf.install(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar o celular");
            e.printStackTrace();
        }

    }

    @Override
    public void limpar() {
        setText("");
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
        return getText().replace("_", "").replace("-", "").replace("(", "").replace(")", "").trim().isEmpty();
    }

}
