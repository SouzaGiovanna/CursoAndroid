package com.cursoandroid.listadetarefas.model;

public class Tarefa {
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