package pojo;

import java.math.BigDecimal;

public class Servico {

    private int id;
    private String nome;
    private BigDecimal valor;
    private boolean ativo;
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }


}
