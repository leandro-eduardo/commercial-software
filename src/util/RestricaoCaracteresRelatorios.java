/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author leand
 */
public class RestricaoCaracteresRelatorios extends PlainDocument {

    private int quantCaracteres;

    public RestricaoCaracteresRelatorios(int tamanho) {
        this.quantCaracteres = tamanho;
    }

    @Override
    public void insertString(int offset, String str, javax.swing.text.AttributeSet attr)
            throws BadLocationException {

        if (str.length() < quantCaracteres) {
            super.insertString(offset, str.replaceAll("[^0-9|^,]", ""), attr); // ACEITA SOMENTE NUMEROS E PONTO
        }

    }
}
