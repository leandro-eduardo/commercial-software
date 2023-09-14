package pojo;

import java.math.BigDecimal;

public class Produto {

    private int id;
    private String nome;
    private BigDecimal precocompra = BigDecimal.ZERO;
    private BigDecimal precovenda = BigDecimal.ZERO;
    private BigDecimal valorlucro = BigDecimal.ZERO;
    private int quantidade;
    private boolean ativo;
    private Fabricante fabricante = new Fabricante();
    private Categoria categoria = new Categoria();
    private final String NOMEVAZIO = "";

    public String getNOMEVAZIO() {
        return NOMEVAZIO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrecocompra() {
        return precocompra;
    }

    public void setPrecocompra(BigDecimal precocompra) {
        this.precocompra = precocompra;
    }

    public BigDecimal getPrecovenda() {
        return precovenda;
    }

    public void setPrecovenda(BigDecimal precovenda) {
        this.precovenda = precovenda;
    }

    public BigDecimal getValorlucro() {
        return valorlucro;
    }

    public void setValorlucro(BigDecimal valorlucro) {
        this.valorlucro = valorlucro;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Fabricante getFabricante() {
        return fabricante;
    }

    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }


}
