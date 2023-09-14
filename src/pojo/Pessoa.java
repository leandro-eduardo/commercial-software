package pojo;

import java.util.Date;

public class Pessoa {

    private int id;
    private String nomerazaosocial;
    private String apelidonomefantasia;
    private String tipo;
    private String cpfcnpj;
    private Date datanascimento;
    private String rgie;
    private String sexo;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cep;
    private Cidade cidade = new Cidade();
    private String fone1;
    private String fone2;
    private String email;
    private String site;
    private boolean ecliente;
    private boolean efornecedor;
    private boolean efuncionario;
    private boolean ativo;
    private String observacao;

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomerazaosocial() {
        return nomerazaosocial;
    }

    public void setNomerazaosocial(String nomerazaosocial) {
        this.nomerazaosocial = nomerazaosocial;
    }

    public String getApelidonomefantasia() {
        return apelidonomefantasia;
    }

    public void setApelidonomefantasia(String apelidonomefantasia) {
        this.apelidonomefantasia = apelidonomefantasia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCpfcnpj() {
        return cpfcnpj;
    }

    public void setCpfcnpj(String cpfcnpj) {
        this.cpfcnpj = cpfcnpj;
    }

    public Date getDatanascimento() {
        return datanascimento;
    }

    public void setDatanascimento(Date datanascimento) {
        this.datanascimento = datanascimento;
    }

    public String getRgie() {
        return rgie;
    }

    public void setRgie(String rgie) {
        this.rgie = rgie;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getFone1() {
        return fone1;
    }

    public void setFone1(String fone1) {
        this.fone1 = fone1;
    }

    public String getFone2() {
        return fone2;
    }

    public void setFone2(String fone2) {
        this.fone2 = fone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public boolean isEcliente() {
        return ecliente;
    }

    public void setEcliente(boolean ecliente) {
        this.ecliente = ecliente;
    }

    public boolean isEfornecedor() {
        return efornecedor;
    }

    public void setEfornecedor(boolean efornecedor) {
        this.efornecedor = efornecedor;
    }

    public boolean isEfuncionario() {
        return efuncionario;
    }

    public void setEfuncionario(boolean efuncionario) {
        this.efuncionario = efuncionario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}
