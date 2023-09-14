package componentes;

import java.awt.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

public class MeuCampoInteger extends MeuJTextField implements MeuComponente, CaretListener {

    protected boolean permiteNegativo;

    public MeuCampoInteger(Integer tamanho, boolean permiteNegativo, boolean podeHabilitar, boolean obrigatorio, String dica) {
        super((tamanho == null ? 15 : tamanho), podeHabilitar, obrigatorio, dica);
        this.permiteNegativo = permiteNegativo;
        setDocument(new MeuDocument(tamanho)); //setDocument 
        setHorizontalAlignment(RIGHT);
        addCaretListener(this);
        setText("0");
        setPreferredSize(new Dimension(tamanho, 24));
    }

    @Override
    public boolean eVazio() {
        return getText().trim().equals("");
    }

    public Object getValor() {
        return (Integer.parseInt("0" + getText().trim()));
    }

    public void setValor(Object valor) {
        setText(String.valueOf(valor));
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if (getCaret().getMark() != getText().length()) {
            getCaret().setDot(getText().length());
        }
    }

    @Override
    public void limpar() {
        setText("0");
    }

    @Override
    public boolean eValido() {
        try {
            int valor = Integer.parseInt(getText());
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    class MeuDocument extends PlainDocument {

        private Integer tamanho;

        public MeuDocument(Integer tamanho) {
            this.tamanho = tamanho;
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) {
            try {
                if (!str.matches("[0-9-]+") || ((!permiteNegativo) && (str.contains("-")))) {
                    return;
                }
                String valorAnterior = getText(0, getLength()); //Pega o conteúdo do campo
                String novo = (valorAnterior + str).replace("-", ""); //
                long novoValor = Long.parseLong(novo);
                if (valorAnterior.contains("-")) {  //Torna valor negativo se já estava negativo
                    novoValor = novoValor * -1;
                }
                if (str.contains("-")) { //Inverte sinal se recebeu um "-"
                    novoValor = novoValor * -1;
                }
                StringBuffer strBuf = new StringBuffer("" + novoValor); //Converte novoValor de Long para StringBuffer
                if ((tamanho != null) && (strBuf.length() > (novoValor < 0 ? tamanho + 1 : tamanho))) { //Gerencia o tamanho do campo código, caso seja maior que o definido, o número não será apresentado
                    return;
                }
                super.remove(0, getLength());
                super.insertString(0, strBuf.toString(), a);
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
                e.printStackTrace();
            }
        }

        @Override
        public void remove(int offs, int len) {
            try {
                super.remove(offs, len);
                insertString(0, " ", null); //Insere um espaço para não processar a informação em null
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
                e.printStackTrace();
            }
        }
    }

}
