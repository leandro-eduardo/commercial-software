package componentes;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

public class MeuCampoMonetario extends JFormattedTextField implements CaretListener, MeuComponente {
    protected boolean permiteNegativo;
    private boolean podeHabilitar;
    private boolean obrigatorio;
    private String dica;

    public MeuCampoMonetario(int colunas, boolean podeHabilitar, boolean permiteNegativo, boolean obrigatorio, String dica) {
        this.podeHabilitar = podeHabilitar;
        this.obrigatorio = obrigatorio;
        this.dica = dica;
        setColumns(colunas);
        this.permiteNegativo = permiteNegativo;
        this.setDocument(new MeuDocument(colunas));
        setHorizontalAlignment(RIGHT);
        addCaretListener(this);
        setText("0,00");
        getCaret().setDot(getText().length());
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if (getCaret().getMark() != getText().length()) {
            getCaret().setDot(getText().length());
        }
    }

    public BigDecimal getValor() {
        return new BigDecimal(getText().replace(".", "").replace(",", "."));
    }

    //    public void setValor(BigDecimal valor) {
//            if(valor == null){
//                setText("");
//            } else {
//              setText(valor.toString());  
//            }
//    }
    public void setValor(Object valor) {
        String novoValor = "0.00";
        if (valor != null) {
            if (valor instanceof String) {
                novoValor = ((String) valor).replace(".", "").replace(",", ".");
            } else if (valor instanceof Double) {
                novoValor = Double.toString((Double) valor);
            } else if (valor instanceof BigDecimal) {
                DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
                df.setParseBigDecimal(true);
                novoValor = df.format(valor);
            } else {
                JOptionPane.showMessageDialog(null, "Tipo de entrada (" + valor.getClass().getName() + ") de valor inválido. (CampoMonetario)");
            }
        }
        setText(novoValor);
    }

    @Override
    public boolean eObrigatorio() {
        return obrigatorio;
    }

    @Override
    public boolean eValido() {
        return true;
    }

    @Override
    public boolean eVazio() {
        return false;
    }

    @Override
    public void limpar() {
        setText("");
    }

    @Override
    public void habilitar(boolean status) {
        setEnabled(podeHabilitar && status);
    }

//    @Override
//    public void setValor(Object valor) {
//       setText(((BigDecimal) valor).toString());
//    }

    public String getDica() {
        return dica;
    }

    class MeuDocument extends PlainDocument {
        private Integer tamanho;

        public MeuDocument(int tamanho) {
            this.tamanho = tamanho + 1; //tem que contar o separador de decimal (,)
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) {
            try {
                Pattern padrao = Pattern.compile("[-0123456789]");
                Matcher matcher = padrao.matcher(str);
                if ((!permiteNegativo) && (str.contains("-")) || (!matcher.find() && !str.isEmpty())) {   //!str.isEmpty() para permitir o processamento quando se retira um dígito (backspace)
                    return;
                }
                int multiplicador = 1;
                String valorAtual = getText(0, getLength()).trim().replace(".", "").replace(",", "");
                str = str.trim().replace(",", ".");
                if (str.indexOf("-") >= 0) { //indica que foi solicitado a mudança de sinal
                    multiplicador = -1;
                    str = str.replace("-", "");
                }
                if (valorAtual.indexOf("-") >= 0) { //indica que estava negativo
                    multiplicador = multiplicador * (-1);
                    valorAtual = valorAtual.replace("-", "");
                }
                valorAtual = "000" + valorAtual + str;
                Double valor = Double.parseDouble(valorAtual);
                valor = valor * multiplicador;
                if (str.indexOf(".") == -1) //se não tem "." indica que o valor não tem os centavos
                {
                    valor = valor / 100;
                }
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                String valorFormatado = nf.format(valor).replace("R$ ", "");
                if (valorFormatado.trim().equals("-0,00")) {
                    valorFormatado = "0,00";
                }
                StringBuffer strBuf = new StringBuffer(valorFormatado);
                valorFormatado = valorFormatado.replace(".", "").replace(",", "").replace("-", ""); //retira tudo o que não for dígito para poder verificar o tamanho.
                if ((tamanho != null) && (valorFormatado.length() >= tamanho)) {
                    return;
                }
                super.remove(0, getLength());
                super.insertString(0, strBuf.toString(), a);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Não foi possível formatar/insert (MeuDocument-->CampoMonetario)");
            }
        }

        @Override
        public void remove(int offs, int len) {
            try {
                super.remove(offs, len);
                if (getText(0, getLength()).trim().isEmpty()) {
                    insertString(0, "0", null);
                } else {
                    insertString(0, "", null);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Não foi possível formatar/remove (MeuDocument-->CampoMonetario)");
            }
        }
    }
}