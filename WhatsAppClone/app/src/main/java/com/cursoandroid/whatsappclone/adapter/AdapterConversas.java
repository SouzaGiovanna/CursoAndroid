package com.cursoandroid.whatsappclone.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cursoandroid.whatsappclone.R;
import com.cursoandroid.whatsappclone.model.Conversa;
import com.cursoandroid.whatsappclone.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterConversas extends RecyclerView.Adapter<AdapterConversas.MyViewHolder>{
    private List<Conversa> conversas;
    private Context context;

    public AdapterConversas(List<Conversa> conversas, Context context) {
        this.conversas = conversas;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_conversas, parent, false);

        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Conversa conversa = conversas.get(position);
        Usuario usuario = conversa.getUsuarioExibicao();

        holder.nome.setText(usuario.getNome());
        holder.ultimaMsg.setText(conversa.getUltimaMensagem());

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
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome, ultimaMsg;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txtNomeUser);
            ultimaMsg = itemView.findViewById(R.id.txtUltimaMensagem);
            foto = itemView.findViewById(R.id.imgPerfilUser);
        }
    }
}
