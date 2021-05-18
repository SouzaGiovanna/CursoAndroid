package com.cursoandroid.instagramclone.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.adapter.AdapterUsuariosPesquisa;
import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.cursoandroid.instagramclone.helper.UsuarioFirebase;
import com.cursoandroid.instagramclone.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PesquisaFragment extends Fragment {
    private RecyclerView recyclerUsuarios;
    private List<Usuario> listUsuarios = new ArrayList<>();
    private DatabaseReference usuariosRef;
    private ValueEventListener eventListener;
    private AdapterUsuariosPesquisa adapter;
    private FirebaseUser usuarioAtual = UsuarioFirebase.getUsuarioAtual();
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        recyclerUsuarios = root.findViewById(R.id.recycler_usuarios_pesquisa);
        usuariosRef = ConfigFirebase.getFirebaseDatabse().child("usuarios");
        searchView = root.findViewById(R.id.search_view);

        configurarSearchView();

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        usuariosRef.removeEventListener(eventListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarUsuarios();
    }

    private void recuperarUsuarios() {
        eventListener = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUsuarios.clear();
                
                for(DataSnapshot dados : snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);
                    
                    if(!usuarioAtual.getEmail().equals(usuario.getEmail())){
                        listUsuarios.add(usuario);
                    }
                }

                Collections.sort(listUsuarios);
                
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        configurarRecyclerView(listUsuarios);
    }

    private void configurarRecyclerView(List<Usuario> usuarios) {
        adapter = new AdapterUsuariosPesquisa(usuarios, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerUsuarios.setLayoutManager(layoutManager);
        recyclerUsuarios.setHasFixedSize(true);
        recyclerUsuarios.setAdapter(adapter);
    }

    public void pesquisarUsuarios(String pesquisa){
        List<Usuario> listUsuariosPesquisa = new ArrayList<>();

        for(Usuario usuario : listUsuarios){
            if(usuario.getNome() != null){
                String nome = usuario.getNome().toLowerCase();

                if(nome.contains(pesquisa)){
                    listUsuariosPesquisa.add(usuario);
                }
            }
        }

        configurarRecyclerView(listUsuariosPesquisa);
        adapter.notifyDataSetChanged();
    }

    public void recarregarUsuarios(){
        configurarRecyclerView(listUsuarios);
        adapter.notifyDataSetChanged();
    }

    private void configurarSearchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pesquisarUsuarios(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarUsuarios(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                recarregarUsuarios();
                return false;
            }
        });
    }
}