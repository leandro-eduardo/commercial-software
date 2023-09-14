package pojo;

import java.math.BigDecimal;
import java.util.Date;

public class MovimentoCaixa {

    private int id;
    private String descricao;
    private String debitoCredito;
    private BigDecimal valor = BigDecimal.ZERO;
    private Date data;
    private Date horaTransacao;
    private Caixa caixa = new Caixa();
    private Pagamento pagamento = new Pagamento();
    private Recebimento recebimento = new Recebimento();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDebitoCredito() {
        return debitoCredito;
    }

    public void setDebitoCredito(String debitoCredito) {
        this.debitoCredito = debitoCredito;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getHoraTransacao() {
        return horaTransacao;
    }

    public void setHoraTransacao(Date horaTransacao) {
        this.horaTransacao = horaTransacao;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Recebimento getRecebimento() {
        return recebimento;
    }

    public void setRecebimento(Recebimento recebimento) {
        this.recebimento = recebimento;
    }
   
}
