package renderizador;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

public class RenderizadorMonetario extends DefaultTableCellRenderer {
    private DecimalFormat df = new DecimalFormat(",##0.00");

    @Override
    public Component getTableCellRendererComponent(JTable tabela, Object valor, boolean isSelected, boolean temFocu, int linha, int coluna) {
        if (valor == null) {
            valor = 0;
        }

        if (isSelected) {
            setBackground(tabela.getSelectionBackground());
            setForeground(tabela.getSelectionForeground());
        } else {
            setBackground(tabela.getBackground());
            setForeground(tabela.getForeground());
        }

        if (valor instanceof String) {
            setText(df.format(Double.parseDouble((String) valor)) + " ");
        } else {
            setText(df.format(valor) + " ");
        }
        setHorizontalAlignment(SwingConstants.RIGHT);
        return this;
    }
}

//public class RenderizadorMonetario extends DefaultTableCellRenderer {
//    @Override
//    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
//        MeuCampoMonetario$MeuDocument mcm =  new MeuCampoMonetario$MeuDocument(15, false, true, true, "");
//        mcm.setValor((BigDecimal) o);
//        mcm.setBorder(null);
//        return mcm;
//    }    
//}