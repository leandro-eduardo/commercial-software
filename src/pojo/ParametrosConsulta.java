package pojo;

import componentes.FiltroPesquisa;

import javax.swing.table.DefaultTableCellRenderer;

public class ParametrosConsulta {
    private String tituloConsulta;
    private String sql;
    private String[] colunas;
    private FiltroPesquisa[] filtros;
    private DefaultTableCellRenderer[] renderizadores;
    private Object chamador;
    private Object telaChamadora;
    private boolean podeHabilitarData;
    private boolean podeHabilitarPessoa;
    private boolean podeHabilitarContaPagar;

    public ParametrosConsulta(String tituloConsulta, String sql, String[] colunas, FiltroPesquisa[] filtros, DefaultTableCellRenderer[] renderizadores, Object chamador, Object telaChamadora, boolean podeHabilitarData, boolean podeHabilitarPessoa, boolean podeHabilitarContaPagar) {
        this.tituloConsulta = tituloConsulta;
        this.sql = sql;
        this.colunas = colunas;
        this.filtros = filtros;
        this.renderizadores = renderizadores;
        this.chamador = chamador;
        this.telaChamadora = telaChamadora;
        this.podeHabilitarData = podeHabilitarData;
        this.podeHabilitarPessoa = podeHabilitarPessoa;
        this.podeHabilitarContaPagar = podeHabilitarContaPagar;
    }

    public String getTituloConsulta() {
        return tituloConsulta;
    }

    public void setTituloConsulta(String tituloConsulta) {
        this.tituloConsulta = tituloConsulta;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String[] getColunas() {
        return colunas;
    }

    public void setColunas(String[] colunas) {
        this.colunas = colunas;
    }

    public FiltroPesquisa[] getFiltros() {
        return filtros;
    }

    public void setFiltros(FiltroPesquisa[] filtros) {
        this.filtros = filtros;
    }

    public DefaultTableCellRenderer[] getRenderizadores() {
        return renderizadores;
    }

    public void setRenderizadores(DefaultTableCellRenderer[] renderizadores) {
        this.renderizadores = renderizadores;
    }

    public Object getChamador() {
        return chamador;
    }

    public void setChamador(Object chamador) {
        this.chamador = chamador;
    }

    public Object getTelaChamadora() {
        return telaChamadora;
    }

    public void setTelaChamadora(Object telaChamadora) {
        this.telaChamadora = telaChamadora;
    }


    public boolean isPodeHabilitarData() {
        return podeHabilitarData;
    }

    public void setPodeHabilitarData(boolean podeHabilitarData) {
        this.podeHabilitarData = podeHabilitarData;
    }

    public boolean isPodeHabilitarPessoa() {
        return podeHabilitarPessoa;
    }

    public void setPodeHabilitarPessoa(boolean podeHabilitarPessoa) {
        this.podeHabilitarPessoa = podeHabilitarPessoa;
    }

    public boolean isPodeHabilitarContaPagar() {
        return podeHabilitarContaPagar;
    }

    public void setPodeHabilitarContaPagar(boolean podeHabilitarContaPagar) {
        this.podeHabilitarContaPagar = podeHabilitarContaPagar;
    }

}
