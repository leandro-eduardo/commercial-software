package util;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class RestricaoCaracteresMinusculos extends PlainDocument {

    private int quantidadeMax;

    public RestricaoCaracteresMinusculos(int maxLen) {
        super();
        if (maxLen <= 0) {
            throw new IllegalArgumentException("Especifique a quantidade!");
        }
        quantidadeMax = maxLen;
    }

    @Override
    public void insertString(int offset, String str, javax.swing.text.AttributeSet attr)
            throws BadLocationException {
        if (str == null || (getLength() == quantidadeMax)) {
            return;
        }
        int totalquantia = (getLength() + str.length());
        if (totalquantia <= quantidadeMax) {
            super.insertString(offset, str.replaceAll("[^0-9|^a-z|^A-Z|^ ÉÔÀÁÃÕÓôéõóàãáíçÇ./,_@-]", ""), attr);
            return;
        }
        String nova = str.substring(0, getLength() - quantidadeMax);
        super.insertString(offset, nova, attr);
    }

}
