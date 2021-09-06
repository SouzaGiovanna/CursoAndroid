package com.cursoandroid.instagramclone.model;

import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

public class Postagem {
    private String id, idUsuario, descricao, foto;
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabse();
    private DatabaseReference postagemRef = firebaseRef.child("postagens");

    public Postagem() {
        setId();
    }

    public String getId() {
        return id;
    }

    private void setId() {
        this.id = postagemRef.push().getKey();
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean salvar(){
        postagemRef.child(idUsuario).child(id).setValue(this);

        return true;
    }
}