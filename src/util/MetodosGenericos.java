package util;

import java.util.Date;

public class MetodosGenericos {

    public static java.sql.Date dataParaBanco(Date data) {
        System.out.println("Data:" + data);
        return new java.sql.Date(data.getTime());
    }

    public static Date dataDoBanco(java.sql.Date data) {
        return new Date(data.getTime());
    }
}
