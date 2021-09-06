package com.cursoandroid.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.instagramclone.R;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class AdapterMiniaturasFiltros extends RecyclerView.Adapter<AdapterMiniaturasFiltros.MyViewHolder> {
    private List<ThumbnailItem> listaFiltros;
    private Context context;

    public AdapterMiniaturasFiltros(List<ThumbnailItem> listaFiltros, Context context) {
        this.listaFiltros = listaFiltros;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterMiniaturasFiltros.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filtros, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ThumbnailItem item = listaFiltros.get(position);

        holder.foto.setImageBitmap(item.image);
        holder.nomeFiltro.setText(item.filterName);
    }

    @Override
    public int getItemCount() {
        return listaFiltros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView nomeFiltro;

        public MyViewHolder(View itemView){
            super(itemView);

            foto = itemView.findViewById(R.id.img_miniatura_filtro);
            nomeFiltro = itemView.findViewById(R.id.txt_nome_filtro);
        }
    }
}
