package tabela;

import dao.DaoProduto;
import dao.DaoServico;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import pojo.ItemVenda;
import pojo.Produto;
import pojo.Servico;

public class TableModelItemVenda extends AbstractTableModel {
    private List<ItemVenda> dadosVenda = new ArrayList();
    private Servico servico = new Servico();
    protected DaoServico daoServico = new DaoServico(servico);
    private String[] colunas = {"Item", "Quantidade",
            "Valor Unitário", "Desconto", "Valor Total"};

    public TableModelItemVenda() {
        dadosVenda.add(new ItemVenda());
    }

    @Override
    public int getRowCount() {
        return dadosVenda.size();
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
        dadosVenda.add(new ItemVenda());
        fireTableDataChanged();
    }

    public void removeLinha(int linha) {
        dadosVenda.remove(linha);
        fireTableDataChanged();
    }

    public void limparDados() {
        dadosVenda = new ArrayList();
        dadosVenda.add(new ItemVenda());
        fireTableDataChanged();
    }

    public List<ItemVenda> getDados() {
        return dadosVenda;
    }

    public void setDados(List<ItemVenda> dadosVenda) {
        this.dadosVenda = dadosVenda;
        fireTableDataChanged();
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        switch (coluna) {
            case 0:
                if (dadosVenda.get(linha).getTipo() == 1 ) {
                    if (dadosVenda.get(linha).getServico().getId() == 0) {
                        return dadosVenda.get(linha).getServico().getNOMEVAZIO();
                    }
                    return dadosVenda.get(linha).getServico().getNome();
                } else {
                    if (dadosVenda.get(linha).getProduto().getId() == 0) {
                        return dadosVenda.get(linha).getProduto().getNOMEVAZIO();
                    }
                    return dadosVenda.get(linha).getProduto().getNome();
                }    
            case 1:
                return dadosVenda.get(linha).getQuantidade();
            case 2:
                return dadosVenda.get(linha).getValorUnitario();
            case 3:
                return dadosVenda.get(linha).getDesconto();
            case 4:
                return dadosVenda.get(linha).getValorTotal();
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
                //dadosVenda.get(linha).getServico().setId((Integer) valor);          
                dadosVenda.get(linha).getServico().setId((Integer) valor);
                //daoServico.setServico(dadosVenda.get(linha).getServico());
                //daoServico.consultar();
            case 1:
                dadosVenda.get(linha).setQuantidade((Integer) valor);
            case 2:
                dadosVenda.get(linha).setValorUnitario((BigDecimal) valor);
            case 3:
                dadosVenda.get(linha).setDesconto((BigDecimal) valor);
            case 4:
                dadosVenda.get(linha).setValorTotal((BigDecimal) valor);
            default:
                JOptionPane.showMessageDialog(null,
                        "Coluna não tratada em TableModelItem (setValueAt).");
        }
    }

    public ItemVenda getItemVenda(int linha) {
        return dadosVenda.get(linha);
    }

    public void alteraProduto(Object valor, int linha) {
        Produto produto = dadosVenda.get(linha).getProduto();
        DaoProduto daoProduto = new DaoProduto(produto);
        produto.setId((int) valor);
        daoProduto.consultar();
        fireTableRowsUpdated(linha, linha);
    }
    
    public void alteraServico(Object valor, int linha) {
        Servico servico = dadosVenda.get(linha).getServico();
        DaoServico daoServico = new DaoServico(servico);
        servico.setId((int) valor);
        daoServico.consultar();
        fireTableRowsUpdated(linha, linha);
    }
    
    public void alteraTipo(Object valor, int linha){
        dadosVenda.get(linha).setTipo((int) valor);
        fireTableRowsUpdated(linha, linha);
    }

    public void alteraValorUnitario(Object valor, int linha) {
        dadosVenda.get(linha).setValorUnitario((BigDecimal) valor);
        fireTableRowsUpdated(linha, linha);
    }

    public void alteraValorTotal(Object valor, int linha) {
        dadosVenda.get(linha).setValorTotal((BigDecimal) valor);
        fireTableRowsUpdated(linha, linha);
    }

    public Produto getProduto(int linha) {
        return dadosVenda.get(linha).getProduto();
    }
    
    public Servico getServico(int linha) {
        return dadosVenda.get(linha).getServico();
    }

    public void alteraQuantidade(Object valor, int linha) {
        dadosVenda.get(linha).setQuantidade((int) valor);
        fireTableRowsUpdated(linha, linha);
    }

    public void alteraDesconto(Object valor, int linha) {
        dadosVenda.get(linha).setDesconto((BigDecimal) valor);
        fireTableRowsUpdated(linha, linha);
    }
}