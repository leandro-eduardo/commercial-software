package renderizador;

import componentes.MeuJTextField;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorTexto extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
        MeuJTextField mjtf = new MeuJTextField(5, true, false, "");
        mjtf.setText((String) o);
        mjtf.setBorder(null);
        //mjtf.setHorizontalAlignment(SwingConstants.CENTER);
        return mjtf;
    }
}