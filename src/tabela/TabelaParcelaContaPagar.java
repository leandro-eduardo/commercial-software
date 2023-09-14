package tabela;

import componentes.MeuComponente;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import renderizador.MonetarioRender;
import renderizador.RenderizadorCheckBox;
import renderizador.RenderizadorData;
import renderizador.RenderizadorInteiroCenter;
import renderizador.RenderizadorParcelas;
import telas.TelaContaPagar;
import telas.TelaParcelaContaPagar;

public class TabelaParcelaContaPagar extends JPanel implements MeuComponente {

    private JScrollPane jsp = new JScrollPane();
    private TableModelParcelaContaPagar tmp = new TableModelParcelaContaPagar();
    private JTable tabela = new JTable(tmp) {
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

    private Icon iconAdicionar = new ImageIcon("src/icones/adicionar.png");
    private Icon iconRemover = new ImageIcon("src/icones/remover.png");

    public TabelaParcelaContaPagar() {
        jsp.setViewportView(tabela);
        jsp.setPreferredSize(new Dimension(600, 180));
        add(jsp);

        tabela.getColumnModel().getColumn(0).setMaxWidth(45);
        tabela.getColumnModel().getColumn(0).setCellRenderer(new RenderizadorInteiroCenter());
        tabela.getColumnModel().getColumn(1).setPreferredWidth(5);
        tabela.getColumnModel().getColumn(1).setCellRenderer(new RenderizadorParcelas());
        tabela.getColumnModel().getColumn(2).setCellRenderer(new RenderizadorData());
        tabela.getColumnModel().getColumn(3).setCellRenderer(new MonetarioRender());
        tabela.getColumnModel().getColumn(4).setCellRenderer(new MonetarioRender());
        tabela.getColumnModel().getColumn(5).setPreferredWidth(10);
        tabela.getColumnModel().getColumn(5).setCellRenderer(new RenderizadorCheckBox());

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (TelaContaPagar.tela.estadoTela == 2) { //significa que está alterando para não ficar dando erro no console de ArrayIndexOutOfBoundsException: -1
                        int linhaSelecionada = tabela.getSelectedRow();
                        if (tabela.getValueAt(linhaSelecionada, 5).toString().equals("false")) { //significa que a ainda não está quitada
                        //diálogo que mostra a linha da tabela selecionada                            
                        //JOptionPane.showMessageDialog(null,"Você clicou na linha: " + tabela.getSelectedRow());  
                            int codigoParcelaContaPagar = (int) tabela.getValueAt(linhaSelecionada, 0);
                            int codigoContaPagar = (int) TelaContaPagar.campoCodigo.getValor();
                            Date dataVecimento = (Date) tabela.getValueAt(linhaSelecionada, 2);
                            TelaParcelaContaPagar.getTela(codigoParcelaContaPagar, codigoContaPagar, dataVecimento);
                        } else {
                            JOptionPane.showMessageDialog(null, "Não é possível alterar o vencimento de uma conta a pagar já quitada.");
                        }
                    }
                }
            }
        });
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
        return tmp.getRowCount() == 0;
    }

    @Override
    public void limpar() {
        tmp.limparDados();
        //tabela.addRowSelectionInterval(0, 0);
    }

    @Override
    public void habilitar(boolean status) {
        tabela.setEnabled(status);
        //botaoAdicionar.setEnabled(status);
        //botaoRemover.setEnabled(status);
    }

    public Object getValor() {
        return tmp.getDados();
    }

//    public void setValor(Object valor) {
//        tmp.setDados((List<ParcelaContaPagar>) valor);
//    }
    @Override
    public String getDica() {
        return "Parcela(s)";
    }

    public TableModelParcelaContaPagar getTableModel() {
        return tmp;
    }

    public int getLinhaSelecionada() {
        return tabela.getSelectedRow();
    }

    public JTable getTabela() {
        return tabela;
    }

}
