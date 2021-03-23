package com.cursoandroid.whatsappclone.model;

import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.helper.Base64Custom;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Grupo implements Serializable {
    private String id;
    private String nome;
    private String foto;
    private List<Usuario> integrantes;

    public Grupo() {
        DatabaseReference database = ConfigFirebase.getFirebaseDatabse();
        DatabaseReference grupoRef = database.child("grupos");

        String idGrupoFirebase = grupoRef.push().getKey();
        setId(idGrupoFirebase);
    }

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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Usuario> integrantes) {
        this.integrantes = integrantes;
    }

    public void salvar(){
        DatabaseReference database = ConfigFirebase.getFirebaseDatabse();
        DatabaseReference grupoRef = database.child("grupos");

        grupoRef.child(getId()).setValue(this);

        salvarConversaGrupo();
    }

    private void salvarConversaGrupo(){
        for(Usuario integrante : getIntegrantes()){
            String idRemetente = Base64Custom.codificarBase64(integrante.getEmail());
            String idDestinatario = getId();

            Conversa conversa = new Conversa();
            conversa.setIdRemetente(idRemetente);
            conversa.setIdDestinatario(idDestinatario);
            conversa.setUltimaMensagem("");
            conversa.setIsGroup("true");
            conversa.setGrupo(this);

            conversa.salvar();
        }
    }
}
