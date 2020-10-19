package com.cursoandroid.listadetarefas.model;

import java.io.Serializable;

public class Tarefa implements Serializable {
    private long id;
    private String tarefa;

    public Tarefa(){

    }

    public Tarefa(String tarefa){
        this.tarefa = tarefa;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTarefa() {
        return tarefa;
    }

    public void setTarefa(String tarefa) {
        this.tarefa = tarefa;
    }
}