package com.cursoandroid.instagramclone.model;

import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.cursoandroid.instagramclone.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable, Comparable<Usuario> {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String foto;

    public Usuario() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void salvar(){
        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabse();
        firebase.child("usuarios").child(this.id).setValue(this);
    }

    public void atualizar(){
        String id = UsuarioFirebase.getIdUsuario();
        DatabaseReference database = ConfigFirebase.getFirebaseDatabse();

        DatabaseReference usuariosRef = database.child("usuarios").child(id);

        usuariosRef.updateChildren(converterParaMap());
    }

    @Exclude
    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();

        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("foto", getFoto());

        return usuarioMap;
    }

    @Override
    public int compareTo(Usuario usuario) {
        return this.nome.compareToIgnoreCase(usuario.getNome());
    }
}
