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

    public AdapterFilmes(List<Filme> filmes) {
        this.filmes = filmes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filmes, parent, false);
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
        Filme filme = filmes.get(position);

        holder.nome.setText(filme.getTitle());
        holder.avaliacao.setText(filme.getVoteAverage().toString());

        if(filme.getRelaceDate().length() != 0){
            holder.ano.setText(filme.getRelaceDate().substring(0, 4));
        }
        else{
            holder.ano.setText("Não Informado");
        }

        recuperarImagens(holder.foto, holder.progressBar, filme.getPosterPath());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome, ano, avaliacao;
        ImageView foto;
        ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);

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
