package tabela;

import dao.DaoCondicaoPagamento;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import pojo.CondicaoPagamento;
import pojo.ContaPagar;
import pojo.ParcelaContaPagar;
import telas.TelaContaPagar;

public class TableModelParcelaContaPagar extends AbstractTableModel {
    public CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
    public ContaPagar contaPagar = new ContaPagar();
    public ParcelaContaPagar parcelaContaPagar = new ParcelaContaPagar();

    private List<ParcelaContaPagar> parcelas = new ArrayList();
    private String[] colunas = {"ID", "Parcela", "Vencimento",
            "Valor", "Valor Pendente", "Quitada"};

    public TableModelParcelaContaPagar() {
        //   parcelas.add(new ParcelaContaPagar());
    }

    @Override
    public int getRowCount() {
        return parcelas.size();
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
        parcelas.add(new ParcelaContaPagar());
        fireTableDataChanged();
    }

    public void removeLinha(int linha) {
        parcelas.remove(linha);
        fireTableDataChanged();
    }

    public void limparDados() {
        parcelas = new ArrayList();
        //parcelas.add(new ParcelaContaPagar()); adiciona uma linha na tabela
        fireTableDataChanged();
    }

    public List<ParcelaContaPagar> getDados() {
        return parcelas;
    }

    public void setDados(List<ParcelaContaPagar> parcelas) {
        this.parcelas = parcelas;
        fireTableDataChanged();
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        switch (coluna) {
            case 0:
                return parcelas.get(linha).getId();
            case 1:
                return linha + 1 + "/" + getRowCount();
            case 2:
                return parcelas.get(linha).getVencimento();
            case 3:
                return parcelas.get(linha).getValor();
            case 4:
                return parcelas.get(linha).getValorPendente();
            case 5:
                return parcelas.get(linha).isQuitada();
            default:
                JOptionPane.showMessageDialog(null,
                        "Coluna não tratada em TableModelParcela (getValueAt).");
                return null;
        }
    }

    @Override
    public void setValueAt(Object valor, int linha, int coluna) {
        switch (coluna) {
            case 0:
                parcelas.get(linha);
            case 1:
                parcelas.get(linha).setVencimento((Date) valor);
            case 2:
                parcelas.get(linha).setValor((BigDecimal) valor);
            case 3:
                parcelas.get(linha).setValorPendente((BigDecimal) valor);
            case 4:
                parcelas.get(linha).setQuitada((boolean) valor);
            default:
                JOptionPane.showMessageDialog(null,
                        "Coluna não tratada em TableModelParcela (setValueAt).");
        }
    }

    public ParcelaContaPagar getParcelaContaPagar(int linha) {
        return parcelas.get(linha);
    }

    public void gerarParcelas(int idCondicaoPagamento) {
        CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
        DaoCondicaoPagamento daoCondicaoPagamento = new DaoCondicaoPagamento(condicaoPagamento);
        condicaoPagamento.setId(idCondicaoPagamento);
        daoCondicaoPagamento.consultar();
        ParcelaContaPagar parcela = null;
        Calendar vencimento = Calendar.getInstance();
        BigDecimal somaparcela = BigDecimal.ZERO;
        if (condicaoPagamento.getCarencia() == 30) {
            vencimento.add(Calendar.MONTH, 1);
        } else {
            vencimento.add(Calendar.DAY_OF_MONTH, condicaoPagamento.getCarencia());
        }
        for (int i = 0; i < condicaoPagamento.getParcelas(); i++) {
            parcela = new ParcelaContaPagar();
            parcela.setId(parcelaContaPagar.getId());
            parcela.setVencimento(vencimento.getTime());
            if (condicaoPagamento.getPrazo() == 30) {
                vencimento.add(Calendar.MONTH, 1);
            } else {
                vencimento.add(Calendar.DAY_OF_MONTH, condicaoPagamento.getPrazo());
            }
            BigDecimal numeroParcelas = new BigDecimal(condicaoPagamento.getParcelas());
            parcela.setValor(TelaContaPagar.campoValorLiquido.getValor().divide(numeroParcelas, BigDecimal.ROUND_HALF_EVEN));
            parcela.setValorPendente(TelaContaPagar.campoValorLiquido.getValor().divide(numeroParcelas, BigDecimal.ROUND_HALF_EVEN)); //será mudado posteriormente quando for criado o pagamento, pois é necessário subtrair o valor referente de cada parcela paga            
            parcela.setParcelas((i + 1) + "/" + condicaoPagamento.getParcelas());
            parcelas.add(parcela);
            somaparcela = somaparcela.add(parcela.getValor());
        }

        BigDecimal diferenca = BigDecimal.ZERO;

        if (somaparcela != TelaContaPagar.campoValorLiquido.getValor()) {
            diferenca = TelaContaPagar.campoValorLiquido.getValor().subtract(somaparcela);
            System.out.println("valor diferenca" + diferenca);
            parcela.setValor(parcela.getValor().add(diferenca));
            parcela.setValorPendente(parcela.getValor());
        }

        setDados(parcelas);
    }
    
}