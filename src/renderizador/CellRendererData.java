package renderizador;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;

public class CellRendererData extends DefaultTableCellRenderer {
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public Component getTableCellRendererComponent(JTable tabela, Object valor, boolean isSelected, boolean hasFocus, int linha, int coluna) {
        try {
            if (valor == null) {
                valor = 0;
            }

//            if (isSelected)
//            {
//                setBackground(tabela.getSelectionBackground());
//                setForeground(tabela.getSelectionForeground());
//            } 
//            
//            else 
//            {
//                setBackground(tabela.getBackground());
//                setForeground(tabela.getForeground());
//            }

            if (valor instanceof String) {
                //setText(sdf2.format(sdf1.parse((String) valor)) + "");
                setText(new SimpleDateFormat("dd/MM/yyyy").format(sdf1.parse((String) valor)) + "");
                //setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(sdf1.parse((String) valor)) + "");
            }

            setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}