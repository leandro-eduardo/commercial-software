package renderizador;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

public class MonetarioRender extends DefaultTableCellRenderer {

    private DecimalFormat df = new DecimalFormat(",##0.00");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null) {
            value = 0;
        }
        if (value instanceof String) {
            setText("R$ " + df.format(Double.parseDouble((String) value)) + " ");
        } else {
            setText("R$ " + df.format(value) + " ");
        }
        setHorizontalAlignment(SwingConstants.RIGHT);
        return this;
    }
}
