package com.cursoandroid.vivadecorafilmes.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Conexao {
    private static BufferedReader bufferedReader = null;

    public static String getDados(String uri) throws IOException {
        try{
            URL url = new URL(uri);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

            StringBuilder stringBuilder = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

            String linha;
            while((linha = bufferedReader.readLine()) != null){
                stringBuilder.append(linha+"\n");
            }

            return stringBuilder.toString();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(bufferedReader != null){
                bufferedReader.close();
            }
        }
    }
}
