package tabela;

import dao.DaoProduto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import pojo.ItemCompra;
import pojo.Produto;

public class TableModelItemCompra extends AbstractTableModel {
    private List<ItemCompra> dados = new ArrayList();
    private String[] colunas = {"Produto", "Quantidade",
            "Valor Unitário", "Desconto", "Valor Total"};

    public TableModelItemCompra() {
        dados.add(new ItemCompra());
    }

    @Override
    public int getRowCount() {
        return dados.size();
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
        dados.add(new ItemCompra());
        fireTableDataChanged();
    }

    public void removeLinha(int linha) {
        dados.remove(linha);
        fireTableDataChanged();
    }

    public void limparDados() {
        dados = new ArrayList();
        dados.add(new ItemCompra());
        fireTableDataChanged();
    }

    public List<ItemCompra> getDados() {
        return dados;
    }

    public void setDados(List<ItemCompra> dados) {
        this.dados = dados;
        fireTableDataChanged();
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        switch (coluna) {
            case 0:
                if (dados.get(linha).getProduto().getId() == 0) {
                        return dados.get(linha).getProduto().getNOMEVAZIO();
                    }
                    return dados.get(linha).getProduto().getNome();
            case 1:
                return dados.get(linha).getQuantidade();
            case 2:
                return dados.get(linha).getValorUnitario();
            case 3:
                return dados.get(linha).getDesconto();
            case 4:
                return dados.get(linha).getValorTotal();
            default:
                JOptionPane.showMessageDialog(null,
                        "Coluna não tratada em TableModelItem (getValueAt).");
                return null;
        }
    }

    @Override
    public void setValueAt(Object valor, int linha, int coluna) {
        switch (coluna) {
            case 0:
                dados.get(linha).getProduto().setId((Integer) valor);
            case 1:
                dados.get(linha).setQuantidade((Integer) valor);
            case 2:
                dados.get(linha).setValorUnitario((BigDecimal) valor);
            case 3:
                dados.get(linha).setDesconto((BigDecimal) valor);
            case 4:
                dados.get(linha).setValorTotal((BigDecimal) valor);
            default:
                JOptionPane.showMessageDialog(null,
                        "Coluna não tratada em TableModelItem (setValueAt).");
        }
    }

    public ItemCompra getItemCompra(int linha) {
        return dados.get(linha);
    }

    public void alteraProduto(Object valor, int linha) {
        Produto produto = dados.get(linha).getProduto();
        DaoProduto daoProduto = new DaoProduto(produto);
        produto.setId((int) valor);
        daoProduto.consultar();
        fireTableRowsUpdated(linha, linha);
    }

    public void alteraValorUnitario(Object valor, int linha) {
        dados.get(linha).setValorUnitario((BigDecimal) valor);
        fireTableRowsUpdated(linha, linha);
    }

    public void alteraValorTotal(Object valor, int linha) {
        dados.get(linha).setValorTotal((BigDecimal) valor);
        fireTableRowsUpdated(linha, linha);
    }

    public Produto getProduto(int linha) {
        return dados.get(linha).getProduto();
    }

    public void alteraQuantidade(Object valor, int linha) {
        dados.get(linha).setQuantidade((int) valor);
        fireTableRowsUpdated(linha, linha);
    }

    public void alteraDesconto(Object valor, int linha) {
        dados.get(linha).setDesconto((BigDecimal) valor);
        fireTableRowsUpdated(linha, linha);
    }
}