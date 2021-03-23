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

public class AdapterMembrosGrupo extends RecyclerView.Adapter<AdapterMembrosGrupo.MyViewHolder>{
    private List<Usuario> usuarios;
    private Context context;

    public AdapterMembrosGrupo(List<Usuario> usuarios, Context context) {
        this.usuarios = usuarios;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_membros_grupo, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);
        String nome = usuario.getNome();
        int pos = nome.indexOf(" ");

        try{
            holder.nome.setText(nome.substring(0, pos));
        }
        catch (Exception e){
            holder.nome.setText(nome);
        }


        if(usuario.getFoto() != null){
            Uri uri = Uri.parse(usuario.getFoto());

            Glide.with(context).load(uri).into(holder.foto);
        }
        else{
            holder.foto.setImageResource(R.drawable.profile);
        }
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txt_nome_membro_grupo);
            foto = itemView.findViewById(R.id.img_foto_membro_grupo);
        }
    }
}
