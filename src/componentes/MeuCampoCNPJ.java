package componentes;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class MeuCampoCNPJ extends MeuJFormattedTextField implements MeuComponente {

    private boolean obrigatorio;
    private String dica;

    public MeuCampoCNPJ(int colunas, boolean obrigatorio, String dica) {
        super(colunas, obrigatorio, dica);
        this.obrigatorio = obrigatorio;
        this.dica = dica;

        try {
            MaskFormatter mf = new MaskFormatter("##.###.###/####-##");
            mf.setPlaceholderCharacter(' ');
            mf.install(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível criar o campo MeuCampoCNPJ");
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
        return validaCNPJ(getText().replace(".", "").replace("-", "").replace("/", "").trim());
    }

    @Override
    public boolean eObrigatorio() {
        return obrigatorio;
    }

    @Override
    public boolean eVazio() {
        //return getText().trim().isEmpty();
        return getText().replace("-", "").replace("/", "").replace(".", "").replace("_", "").trim().isEmpty();
    }

    @Override
    public String getDica() {
        return dica;
    }

    @Override
    public void habilitar(boolean status) {
        setEnabled(status);
    }

    //-------- Valida CPF ou CNPJ
    public boolean validaCNPJ(String s_aux) {
//------- Rotina para CPF
        if (s_aux.length() == 14) {
            if (s_aux.equals("11111111111111")
                    || s_aux.equals("22222222222222")
                    || s_aux.equals("33333333333333")
                    || s_aux.equals("44444444444444")
                    || s_aux.equals("55555555555555")
                    || s_aux.equals("66666666666666")
                    || s_aux.equals("77777777777777")
                    || s_aux.equals("88888888888888")
                    || s_aux.equals("99999999999999")
                    || s_aux.equals("00000000000000")) {
                return false;
            } else {
                String str_cnpj = s_aux;
                int soma = 0, aux, dig;
                String cnpj_calc = str_cnpj.substring(0, 12);
                if (str_cnpj.length() != 14) {
                    return false;
                }
                char[] chr_cnpj = str_cnpj.toCharArray();
                /* Primeira parte */
                for (int i = 0; i
                        < 4; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
                    }
                }
                for (int i = 0; i
                        < 8; i++) {
                    if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
                        soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);
                cnpj_calc += (dig == 10 || dig == 11)
                        ? "0" : Integer.toString(dig);
                /* Segunda parte */
                soma = 0;
                for (int i = 0; i
                        < 5; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
                    }
                }
                for (int i = 0; i
                        < 8; i++) {
                    if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
                        soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);
                cnpj_calc += (dig == 10 || dig == 11)
                        ? "0" : Integer.toString(dig);
                return str_cnpj.equals(cnpj_calc);
            }
        } else {
            return false;
        }
    }

}
