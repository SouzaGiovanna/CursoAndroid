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
    private List<Usuario> usuarios;
    private Context context;

    public AdapterContatos(List<Usuario> usuarios, Context context) {
        this.usuarios = usuarios;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);
        boolean grupo = usuario.getEmail().isEmpty();

        holder.nome.setText(usuario.getNome());
        holder.email.setText(usuario.getEmail());

        if(usuario.getFoto() != null){
            Uri uri = Uri.parse(usuario.getFoto());

            Glide.with(context).load(uri).into(holder.foto);
        }
        else{
            if(grupo){
                holder.foto.setImageResource(R.drawable.icone_grupo);
                holder.email.setVisibility(View.GONE);
            }
            else{
                holder.foto.setImageResource(R.drawable.profile);
            }
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
