package com.cursoandroid.whatsappclone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cursoandroid.whatsappclone.R;
import com.cursoandroid.whatsappclone.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterContatos extends RecyclerView.Adapter<AdapterContatos.MyViewHolder>{
    private final int GRUPO = 0;
    private final int CONTATOS = 1;
    private List<Usuario> usuarios;
    private Context context;

    public AdapterContatos(List<Usuario> usuarios, Context context) {
        this.usuarios = usuarios;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = null;

        switch (viewType){
            case GRUPO:
                itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_novo_grupo, parent, false);
                break;
            case CONTATOS:
                itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);
                break;
        }

        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case CONTATOS:
                Usuario usuario = usuarios.get(position);

                holder.nome.setText(usuario.getNome());
                holder.email.setText(usuario.getEmail());

                if(usuario.getFoto() != null){
                    Uri uri = Uri.parse(usuario.getFoto());

                    Glide.with(context).load(uri).into(holder.foto);
                }
                else{
                    holder.foto.setImageResource(R.drawable.profile);
                }
                break;
            case GRUPO:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return GRUPO;
        }
        else{
            return CONTATOS;
        }
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome, email;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txtNomeUser);
            email = itemView.findViewById(R.id.txtEmailUser);
            foto = itemView.findViewById(R.id.imgPerfilUser);
        }
    }
}
