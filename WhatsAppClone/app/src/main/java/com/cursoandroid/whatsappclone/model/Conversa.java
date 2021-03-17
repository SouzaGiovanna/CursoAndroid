package com.cursoandroid.whatsappclone.model;

import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

public class Conversa {
    private String idRemetente;
    private String idDestinatario;
    private String ultimaMensagem;
    private Usuario usuarioExibicao;

    public Conversa() {
    }

    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public Usuario getUsuarioExibicao() {
        return usuarioExibicao;
    }

    public void setUsuarioExibicao(Usuario usuarioExibicao) {
        this.usuarioExibicao = usuarioExibicao;
    }

    public void salvar(){
        DatabaseReference database = ConfigFirebase.getFirebaseDatabse();
        DatabaseReference conversaRef = database.child("conversas");

        conversaRef.child(this.getIdRemetente()).child(this.getIdDestinatario()).setValue(this);
    }
}
