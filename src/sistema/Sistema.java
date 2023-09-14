//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sistema;

import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;
import javax.swing.UIManager;
import telas.TelaSistema;

public class Sistema {
    public Sistema() {
    }

    public static void main(String[] args) {
        new TelaSistema();

        try {
            UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}
