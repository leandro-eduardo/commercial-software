package renderizador;

import componentes.MeuCampoHora;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorHora extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
        MeuCampoHora mch = new MeuCampoHora(6, false, "");
        mch.setValor(o);
        mch.setBorder(null);
        mch.setHorizontalAlignment(SwingConstants.CENTER);
        return mch;
    }
}