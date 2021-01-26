package com.cursoandroid.vivadecorafilmes.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.vivadecorafilmes.R;
import com.cursoandroid.vivadecorafilmes.model.Filme;
import com.cursoandroid.vivadecorafilmes.service.DownloadImageTask;

import java.util.List;

public class AdapterFilmes extends RecyclerView.Adapter<AdapterFilmes.MyViewHolder>{
    private List<Filme> filmes;
    private Boolean mostrarAnuncio;
    private static final int anuncio = 4;
    private static final int ITEMTYPEFILME = 0;
    private static final int ITEMTYPEANUNCIO = 1;
    private int contador = 0;

    public AdapterFilmes() {
    }

    public AdapterFilmes(List<Filme> filmes, Boolean mostrarAnuncio) {
        this.filmes = filmes;
        this.mostrarAnuncio = mostrarAnuncio;
    }

    public int getAnuncio(){
        return anuncio;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = null;

        if(mostrarAnuncio) {
            if (contador % anuncio == 0 && contador != 0) {
                viewType = 1;
            }
        }

        switch (viewType){
            case ITEMTYPEFILME:
                itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filmes, parent, false);
                break;
            case ITEMTYPEANUNCIO:
                itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_promocional, parent, false);
                break;
        }

        contador++;

        return new MyViewHolder(itemLista);
    }

    @Override
    public int getItemCount() {
        return filmes.size();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(mostrarAnuncio) {
            if (position % anuncio != 0 || position == 0) {
                Filme filme = filmes.get(position);

                holder.nome.setText(filme.getTitle());
                holder.avaliacao.setText(filme.getVoteAverage().toString());

                if (filme.getRelaceDate().length() != 0) {
                    holder.ano.setText(filme.getRelaceDate().substring(0, 4));
                } else {
                    holder.ano.setText("Não Informado");
                }

                recuperarImagens(holder.foto, holder.progressBar, filme.getPosterPath());
            }
        }
        else{
            Filme filme = filmes.get(position);

            holder.nome.setText(filme.getTitle());
            holder.avaliacao.setText(filme.getVoteAverage().toString());

            if (filme.getRelaceDate().length() != 0) {
                holder.ano.setText(filme.getRelaceDate().substring(0, 4));
            } else {
                holder.ano.setText("Não Informado");
            }

            recuperarImagens(holder.foto, holder.progressBar, filme.getPosterPath());
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome, ano, avaliacao;
        ImageView foto;
        ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);

            //Filme 1
            nome = itemView.findViewById(R.id.nomeFilme);
            ano = itemView.findViewById(R.id.anoPublicacao);
            avaliacao = itemView.findViewById(R.id.avaliacaoFilme);
            foto = itemView.findViewById(R.id.imgFilme);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void recuperarImagens(ImageView image, ProgressBar progressBar, String nomeImagem){
        new DownloadImageTask(image, progressBar).execute("https://image.tmdb.org/t/p/w500/"+nomeImagem);
    }
}
