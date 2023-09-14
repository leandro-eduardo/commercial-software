package pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ContaPagar {

    private int id;
    private Date data;
    private BigDecimal valortotal = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal valorliquido = BigDecimal.ZERO;
    private BigDecimal valorpendente = BigDecimal.ZERO;
    private boolean quitada;
    private String descricao;
    private Compra compra = new Compra();
    private Pessoa pessoa = new Pessoa();
    private CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
    private List<ParcelaContaPagar> itensContaPagar;
    private List<Integer> controlePksItens;

    public List<ParcelaContaPagar> getItensContaPagar() {
        return itensContaPagar;
    }

    public void setItensContaPagar(List<ParcelaContaPagar> itensContaPagar) {
        this.itensContaPagar = itensContaPagar;
    }

    public List<Integer> getControlePksItens() {
        return controlePksItens;
    }

    public void setControlePksItens(List<Integer> controlePksItens) {
        this.controlePksItens = controlePksItens;
    }

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

    public BigDecimal getValortotal() {
        return valortotal;
    }

    public void setValortotal(BigDecimal valortotal) {
        this.valortotal = valortotal;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getValorliquido() {
        return valorliquido;
    }

    public void setValorliquido(BigDecimal valorliquido) {
        this.valorliquido = valorliquido;
    }

    public BigDecimal getValorpendente() {
        return valorpendente;
    }

    public void setValorpendente(BigDecimal valorpendente) {
        this.valorpendente = valorpendente;
    }

    public boolean isQuitada() {
        return quitada;
    }

    public void setQuitada(boolean quitada) {
        this.quitada = quitada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public CondicaoPagamento getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }


}
