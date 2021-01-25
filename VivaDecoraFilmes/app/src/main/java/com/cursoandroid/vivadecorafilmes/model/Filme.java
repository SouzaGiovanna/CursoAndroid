package com.cursoandroid.vivadecorafilmes.model;

import java.io.Serializable;
import java.util.List;

public class Filme implements Serializable {
    private String backdropPath; //banner nome da Imagem
    private String posterPath; //poster nome da Imagem
    private String title; //Titulo do filme
    private Double voteAverage; //Avaliação média
    private String relaceDate; //Data de publicação do filme
    private String overview; //Sinopse
    private List<Integer> genreIds; //Ids dos gêneros do filme

    public Filme(String backdropPath, String posterPath, String title, Double votEAverage, String relaceDate, String overview, List<Integer> genreIds) {
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.title = title;
        this.voteAverage = votEAverage;
        this.relaceDate = relaceDate;
        this.overview = overview;
        this.genreIds = genreIds;
    }

    public Filme() {
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getRelaceDate() {
        return relaceDate;
    }

    public void setRelaceDate(String relaceDate) {
        this.relaceDate = relaceDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }
}