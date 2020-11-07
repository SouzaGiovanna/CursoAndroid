package com.cursoandroid.organizze.helper;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class DateCustom {
    public static String dataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;
    }

    public static String mesAnoData(String data){
        List<String> retornoData = Arrays.asList(data.split("/"));

        String dia = retornoData.get(0);
        String mes = retornoData.get(1);
        String ano = retornoData.get(2);

        String mesAno = mes + ano;

        return mesAno;
    }
}
