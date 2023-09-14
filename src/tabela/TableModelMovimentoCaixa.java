package tabela;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import pojo.MovimentoCaixa;
import pojo.Pagamento;
import pojo.Recebimento;

public class TableModelMovimentoCaixa extends AbstractTableModel {
    
    public Pagamento pagamento = new Pagamento();
    public Recebimento recebimento = new Recebimento();
    public MovimentoCaixa movimentosCaixa = new MovimentoCaixa();

    private List<MovimentoCaixa> movimentos = new ArrayList();
    private String[] colunas = {"ID", "Descrição", "Tipo", "Valor", "Data", "Hora", "Pagamento", "Recebimento"};

    public TableModelMovimentoCaixa() {
        //   movimentos.add(new MovimentoCaixa());
    }

    @Override
    public int getRowCount() {
        return movimentos.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int coluna) {
        return colunas[coluna];
    }

    public void adicionaLinha() {
        movimentos.add(new MovimentoCaixa());
        fireTableDataChanged();
    }

    public void removeLinha(int linha) {
        movimentos.remove(linha);
        fireTableDataChanged();
    }

    public void limparDados() {
        movimentos = new ArrayList();
        //movimentos.add(new MovimentoCaixa()); adiciona uma linha na tabela
        fireTableDataChanged();
    }

    public List<MovimentoCaixa> getDados() {
        return movimentos;
    }

    public void setDados(List<MovimentoCaixa> movimentos) {
        this.movimentos = movimentos;
        fireTableDataChanged();
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        switch (coluna) {
            case 0:
                return movimentos.get(linha).getId();
            case 1:
                return movimentos.get(linha).getDescricao();
            case 2:
                return movimentos.get(linha).getDebitoCredito();
            case 3:
                return movimentos.get(linha).getValor();
            case 4:
                return movimentos.get(linha).getData();
            case 5:
                return movimentos.get(linha).getHoraTransacao();
            case 6:
                //System.out.println("VALOR GETPGTO " + movimentos.get(linha).getPagamento().getId());
                if (movimentos.get(linha).getPagamento().getId() == 0) {
                    return "";
                } else {
                    return movimentos.get(linha).getPagamento().getId();
                }
            case 7:
                //System.out.println("VALOR GETRECBIMENTO " + movimentos.get(linha).getRecebimento().getId());
                if (movimentos.get(linha).getRecebimento().getId() == 0) {
                    return "";
                } else {
                    return movimentos.get(linha).getRecebimento().getId();
                }
            default:
                JOptionPane.showMessageDialog(null, "Coluna não tratada em TableModelMovimentoCaixa (getValueAt).");
                return null;
        }
    }

    @Override
    public void setValueAt(Object valor, int linha, int coluna) {
        switch (coluna) {
            case 0:
                movimentos.get(linha);
            case 1:
                movimentos.get(linha).setDescricao((String) valor);
            case 2:
                movimentos.get(linha).setDebitoCredito((String) valor);
            case 3:
                movimentos.get(linha).setValor((BigDecimal) valor);
            case 4:
                movimentos.get(linha).setData((Date) valor);
            case 5:
                movimentos.get(linha).setHoraTransacao((Date) valor);
            case 6:
                movimentos.get(linha).setPagamento((Pagamento) valor);
            case 7:
                movimentos.get(linha).setRecebimento((Recebimento) valor); //setRecebimento((Recebimento) valor);
            default:
                JOptionPane.showMessageDialog(null, "Coluna não tratada em TableModelMovimentoCaixa (setValueAt).");
        }
    }

    public MovimentoCaixa getMovimentoCaixa(int linha) {
        return movimentos.get(linha);
    }

}