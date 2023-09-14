package componentes;

import bd.Conexao;
import tabela.TabelaItemCompra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MeuDBJComboBox extends JPanel implements MeuComponente, FocusListener {

    public JComboBox jcb = new JComboBox();
    public TabelaItemCompra tabelaItem = new TabelaItemCompra();
    private Icon iconExtend = new ImageIcon("src/icones/extend.png");
    private JButton mjb = new JButton("", iconExtend);
    private boolean obrigatorio;
    private String sql;
    private String dica;
    private List<Integer> dados;

    public MeuDBJComboBox(final Class tela, boolean obrigatorio, String sql, String dica) {
        this.obrigatorio = obrigatorio;
        this.dica = dica;
        this.sql = sql;
        setLayout(new FlowLayout());
        add(jcb);
        jcb.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                preencher();
            }

            @Override
            public void focusLost(FocusEvent fe) {

            }
        });
        add(mjb);
        mjb.setToolTipText("Incluir novo cadastro.");
        preencher();
        mjb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    Method metodoGetTela = tela.getMethod("getTela");
                    metodoGetTela.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void preencher() {
        try {
            jcb.removeAllItems();
            jcb.addItem("Selecione...");
            dados = new ArrayList();
            dados.add(-1);
            ResultSet rs = Conexao.getConexao().createStatement().executeQuery(sql);
            System.out.println(sql);
            while (rs.next()) {
                System.out.println(rs.getString(2));
                dados.add(rs.getInt(1));
                jcb.addItem(rs.getString(2));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher o MeuDBJComboBox");
            e.printStackTrace();
        }
    }

    public Object getValor() {
        return dados.get(jcb.getSelectedIndex());
    }

    public void setValor(Object valor) {
        int v = (Integer) valor;
        System.out.println(valor + " - " + dados.size());
        for (int i = 0; i < dados.size(); i++) {
            if (dados.get(i) == v) {
                jcb.setSelectedIndex(i);
                return;
            }
        }
        //JOptionPane.showMessageDialog(null, dica + " (" + v + ") não encontrado(a)");
    }

    public String getValorTexto() {
        return (String) jcb.getSelectedItem();
    }

    @Override
    public String getDica() {
        return dica;
    }

    @Override
    public boolean eObrigatorio() {
        return obrigatorio;
    }

    @Override
    public void limpar() {
        jcb.setSelectedIndex(0);
    }

    @Override
    public void habilitar(boolean status) {
        jcb.setEnabled(status);
        mjb.setEnabled(status);
    }

    @Override
    public boolean eVazio() {
        return (jcb.getSelectedIndex() == 0);
    }

    @Override
    public boolean eValido() {
        return true;
    }

    public void addItemListener(ItemListener il) {
        jcb.addItemListener(il);
    }

    @Override
    public void focusGained(FocusEvent fe) {
        setBackground(Color.lightGray);
    }

    @Override
    public void focusLost(FocusEvent fe) {
        if (eObrigatorio()) {
            setBackground(new Color(1, 1, 1, 255));
        } else {
            setBackground(Color.WHITE);
        }
    }

}
