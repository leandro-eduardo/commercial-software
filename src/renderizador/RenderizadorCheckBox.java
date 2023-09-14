package renderizador;

import componentes.MeuCampoCheckBox;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorCheckBox extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
        MeuCampoCheckBox mccb = new MeuCampoCheckBox(false, true, "");
        mccb.setValor((Boolean) o);
        mccb.setBorder(null);
        mccb.setHorizontalAlignment(SwingConstants.CENTER);
        return mccb;
    }
}