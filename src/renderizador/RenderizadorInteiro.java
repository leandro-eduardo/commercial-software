package renderizador;

import componentes.MeuCampoInteger;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorInteiro extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
        MeuCampoInteger mci = new MeuCampoInteger(5, false, true, true, "");
        mci.setValor(o);
        mci.setBorder(null);
        setHorizontalAlignment(SwingConstants.LEFT);
        return mci;
    }
}