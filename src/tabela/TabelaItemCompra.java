package tabela;

import componentes.MeuComponente;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import pojo.ItemCompra;
import renderizador.RenderizadorMonetario;
import renderizador.RenderizadorQtde;

public class TabelaItemCompra extends JPanel implements MeuComponente {
    private JScrollPane jsp = new JScrollPane();
    public static final TableModelItemCompra tmi = new TableModelItemCompra();
    public static final JTable tabela = new JTable(tmi) {
        @Override
        public Component prepareRenderer(TableCellRenderer renderer,
                                         int linha, int coluna) {
            Component c = super.prepareRenderer(renderer, linha, coluna);
            if (linha % 2 == 0) {
                c.setBackground(Color.LIGHT_GRAY);
            } else {
                c.setBackground(getBackground());
            }
            if (isCellSelected(linha, coluna)) {
                c.setBackground(Color.decode("#4286f4"));
            }
            return c;
        }
    };

//    private Icon iconAdicionar = new ImageIcon("src/icones/adicionar.png"); //removidos os botões adicionar e remover do lado da tabela e adicionados ao dentro do painel do item
//    private Icon iconRemover = new ImageIcon("src/icones/remover.png");
//
//    private JButton botaoAdicionar = new JButton("", iconAdicionar);
//    private JButton botaoRemover = new JButton("", iconRemover);

    public TabelaItemCompra() {
        jsp.setViewportView(tabela);
        jsp.setPreferredSize(new Dimension(800, 200));
        add(jsp);
//        add(botaoAdicionar); //removidos os botões adicionar e remover do lado da tabela e adicionados ao dentro do painel do item
//        add(botaoRemover);
//        botaoAdicionar.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                tmi.adicionaLinha();
//                tabela.addRowSelectionInterval(tmi.getRowCount() - 1, tmi.getRowCount() - 1);
//            }
//        });
//        botaoRemover.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                if (tabela.getSelectedRow() >= 0) {
//                    if (tmi.getRowCount() > 1) {
//                        tmi.removeLinha(tabela.getSelectedRow());
//                        tabela.addRowSelectionInterval(tmi.getRowCount() - 1, tmi.getRowCount() - 1);
//                        TelaCompra.atualizaCamposTotal(null);
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null,
//                            "Selecione uma linha para poder excluí-la.");
//                }
//            }
//        });
        tabela.getColumnModel().getColumn(1).setCellRenderer(new RenderizadorQtde());
        tabela.getColumnModel().getColumn(2).setCellRenderer(new RenderizadorMonetario());
        tabela.getColumnModel().getColumn(3).setCellRenderer(new RenderizadorMonetario());
        tabela.getColumnModel().getColumn(4).setCellRenderer(new RenderizadorMonetario());
        tabela.getColumnModel().getColumn(0).setPreferredWidth(380);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(60);
    }

    @Override
    public boolean eObrigatorio() {
        return true;
    }

    @Override
    public boolean eValido() {
        return true;
    }

    @Override
    public boolean eVazio() {
        return tmi.getRowCount() == 0;
    }

    @Override
    public void limpar() {
        tmi.limparDados();
        tabela.addRowSelectionInterval(0, 0);
    }

    public void limparCancelar() {
        tabela.clearSelection();
    }

    @Override
    public void habilitar(boolean status) {
        tabela.setEnabled(status);
//        botaoAdicionar.setEnabled(status); //removidos os botões adicionar e remover do lado da tabela e adicionados ao dentro do painel do item
//        botaoRemover.setEnabled(status);
    }

    public Object getValor() {
        return tmi.getDados();
    }

    public void setValor(Object valor) {
        tmi.setDados((List<ItemCompra>) valor);
    }

    @Override
    public String getDica() {
        return "Itens";
    }

    public TableModelItemCompra getTableModel() {
        return tmi;
    }

    public int getLinhaSelecionada() {
        return tabela.getSelectedRow();
    }

    public JTable getTabela() {
        return tabela;
    }
}