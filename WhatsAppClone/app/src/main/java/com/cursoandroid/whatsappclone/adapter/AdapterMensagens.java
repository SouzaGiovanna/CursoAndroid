package com.cursoandroid.whatsappclone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cursoandroid.whatsappclone.R;
import com.cursoandroid.whatsappclone.helper.UsuarioFirebase;
import com.cursoandroid.whatsappclone.model.Mensagem;

import java.util.List;

public class AdapterMensagens extends RecyclerView.Adapter<AdapterMensagens.MyViewHolder> {
    private List<Mensagem> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    public AdapterMensagens(List<Mensagem> mensagens, Context context) {
        this.mensagens = mensagens;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = null;

        switch (viewType){
            case TIPO_REMETENTE:
                item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente, parent, false);
                break;
            case TIPO_DESTINATARIO:
                item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario, parent, false);
                break;
        }

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getMensagem();
        String img = mensagem.getImagem();

        if(img != null){
            Uri url = Uri.parse(img);
            Glide.with(context).load(url).into(holder.imagem);

            holder.mensagem.setVisibility(View.GONE);
        }
        else{
            holder.mensagem.setText(msg);

            holder.imagem.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensagem mensagem = mensagens.get(position);
        String idUsuario = UsuarioFirebase.getIdentificadorUsuario();

        if(idUsuario.equals(mensagem.getIdUsuario())){
            return TIPO_REMETENTE;
        }

        return TIPO_DESTINATARIO;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mensagem;
        ImageView imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mensagem = itemView.findViewById(R.id.txtMensagem);
            imagem = itemView.findViewById(R.id.imgMensagem);
        }
    }
}
