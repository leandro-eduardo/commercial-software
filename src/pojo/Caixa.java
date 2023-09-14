package pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Caixa {

    private int id;
    private Date data;
    private Date dataHrFechamento;
    private BigDecimal saldoInicial = BigDecimal.ZERO;
    private BigDecimal saldoFinal = BigDecimal.ZERO;
    private BigDecimal saldoAntesFechamento = BigDecimal.ZERO;
    private BigDecimal saldo = BigDecimal.ZERO;
    private boolean aberto;
    private List<MovimentoCaixa> itensCaixa;
    private List<Integer> controlePksItens;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataHrFechamento() {
        return dataHrFechamento;
    }

    public void setDataHrFechamento(Date dataHrFechamento) {
        this.dataHrFechamento = dataHrFechamento;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public BigDecimal getSaldoAntesFechamento() {
        return saldoAntesFechamento;
    }

    public void setSaldoAntesFechamento(BigDecimal saldoAntesFechamento) {
        this.saldoAntesFechamento = saldoAntesFechamento;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public List<MovimentoCaixa> getItensCaixa() {
        return itensCaixa;
    }

    public void setItensCaixa(List<MovimentoCaixa> itensCaixa) {
        this.itensCaixa = itensCaixa;
    }

    public List<Integer> getControlePksItens() {
        return controlePksItens;
    }

    public void setControlePksItens(List<Integer> controlePksItens) {
        this.controlePksItens = controlePksItens;
    }

}
