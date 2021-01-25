package com.cursoandroid.vivadecorafilmes.service;

import android.util.Log;

import com.cursoandroid.vivadecorafilmes.model.Filme;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceFilme {
    private static final int QTD_FILMES = 16;

    public static List<Filme> dadosFilmes(String conteudo){
        try{
            List<Filme> filmes = new ArrayList<>();
            List<Integer> idGeneros = new ArrayList<>();
            JSONArray jsonArray;
            JSONArray jsonArrayIds;
            JSONObject jsonObject;

            jsonObject = new JSONObject(conteudo);
            jsonArray = jsonObject.getJSONArray("results");

            for(int i = 0; i < QTD_FILMES; i++){
                JSONObject resultadoFilme = jsonArray.getJSONObject(i);

                Filme filme = new Filme();

                filme.setBackdropPath(resultadoFilme.get("backdrop_path").toString());
                filme.setPosterPath(resultadoFilme.get("poster_path").toString());
                filme.setTitle(resultadoFilme.get("title").toString());
                filme.setVoteAverage(Double.parseDouble(resultadoFilme.get("vote_average").toString()));
                filme.setRelaceDate(resultadoFilme.get("release_date").toString());
                filme.setOverview(resultadoFilme.get("overview").toString());

                jsonArrayIds = resultadoFilme.getJSONArray("genre_ids");

                for (int j = 0; j < jsonArrayIds.length(); j++) {
                    idGeneros.add(jsonArrayIds.getInt(j));
                }

                filme.setGenreIds(idGeneros);

                filmes.add(filme);
            }
            Log.d("teste", "deu certo");
            return filmes;
        }catch (Exception e){

            Log.d("teste", "erro");
            e.printStackTrace();
            return null;
        }
    }
}
