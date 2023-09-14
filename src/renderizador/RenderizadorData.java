package renderizador;

import componentes.MeuCampoData;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorData extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
        MeuCampoData mcd = new MeuCampoData(6, false, "");
        mcd.setValor(o);
        mcd.setBorder(null);
        mcd.setHorizontalAlignment(SwingConstants.CENTER);
        return mcd;
    }
}