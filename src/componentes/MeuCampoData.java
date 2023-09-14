package componentes;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class MeuCampoData extends MeuJFormattedTextField implements MeuComponente {
    private boolean obrigatorio;
    private String nome;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public MeuCampoData(int colunas, boolean obrigatorio, String nome) {
        super(colunas, obrigatorio, nome);
        this.obrigatorio = obrigatorio;
        this.nome = nome;
        setPreferredSize(new Dimension(colunas, 24));
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.setPlaceholderCharacter(' ');
            mf.install(this);
        } catch (Exception e) {
            e.printStackTrace(); //mostra um relatório de onde ocorreu o erro
        }
    }

    @Override
    public boolean eVazio() {
        return getText().replace("-", "").replace("/", "").replace(".", "").replace("_", "").trim().isEmpty();
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
            setText(sdf.format((Date) valor));
        }
    }

    public Date getValorDate() {
        if (eVazio()) {
            return null;
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.parse(getText());
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
                if (sdf.parse(getText()).after(dataAtual)) {
                    return false;
                } else if (sdf.parse(getText()).before(dataRemota)) {
                    return false;
                }
                sdf.setLenient(false);
                sdf.parse(getText());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
