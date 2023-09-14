package componentes;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MeuCampoData1 extends JDateChooser implements MeuComponente {
    private boolean obrigatorio;
    private String nome;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public MeuCampoData1(boolean obrigatorio, String nome) {
        this.obrigatorio = obrigatorio;
        this.nome = nome;
    }

    @Override
    public boolean eVazio() {
        return getDateFormatString().replace("-", "").replace("/", "").replace(".", "").replace("_", "").trim().isEmpty();
    }

    @Override
    public void limpar() {
        setDateFormatString("");
    }

    @Override
    public void habilitar(boolean status) {
        setEnabled(status);
    }

    public void setValor(Object valor) {
        if (valor == null) {
            setDateFormatString("");
        } else {
            setDateFormatString(sdf.format((Date) valor));
        }
    }

    public Date getValorDate() {
        if (eVazio()) {
            return null;
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.parse(getDateFormatString());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Não foi possível obter a data.");
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public boolean eValido() {
        if (eVazio()) {
            return true;
        } else {
            try {
                Date dataRemota = new SimpleDateFormat("dd/mm/yyyy").parse("00/01/1917");
                Date dataAtual = new Date();
                if (sdf.parse(getDateFormatString()).after(dataAtual)) {
                    return false;
                } else if (sdf.parse(getDateFormatString()).before(dataRemota)) {
                    return false;
                }
                sdf.setLenient(false);
                sdf.parse(getDateFormatString());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public String getDica() {
        return nome;
    }

    @Override
    public boolean eObrigatorio() {
        return obrigatorio;
    }
}
