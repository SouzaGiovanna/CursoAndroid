package com.cursoandroid.instagramclone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUsuariosPesquisa extends RecyclerView.Adapter<AdapterUsuariosPesquisa.MyViewHolder>{
    private List<Usuario> usuarios;
    private Context context;

    public AdapterUsuariosPesquisa(List<Usuario> usuarios, Context context) {
        this.usuarios = usuarios;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pesquisa, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);

        holder.nome.setText(usuario.getNome());

        if(usuario.getFoto() != null){
            Uri url = Uri.parse(usuario.getFoto());

            Glide.with(context).load(url).into(holder.foto);
        }
        else{
            holder.foto.setImageResource(R.drawable.raposinha);
        }
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView foto;
        TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.img_usuario_pesquisa);
            nome = itemView.findViewById(R.id.txt_nome_usuario_pesquisa);
        }
    }
}
