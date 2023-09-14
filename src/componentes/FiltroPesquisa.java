package componentes;

public class FiltroPesquisa {

    public String nomeFiltro;
    public String nomeAtributo;
    public Class tipo;

    public FiltroPesquisa(String nomeFiltro, String nomeAtributo, Class tipo) {
        this.nomeFiltro = nomeFiltro;
        this.nomeAtributo = nomeAtributo;
        this.tipo = tipo;
    }
}
