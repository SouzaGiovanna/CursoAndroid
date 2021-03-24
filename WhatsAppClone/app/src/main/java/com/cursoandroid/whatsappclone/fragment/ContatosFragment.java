package com.cursoandroid.whatsappclone.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cursoandroid.whatsappclone.R;
import com.cursoandroid.whatsappclone.activity.ChatActivity;
import com.cursoandroid.whatsappclone.activity.GrupoActivity;
import com.cursoandroid.whatsappclone.adapter.AdapterContatos;
import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.config.RecyclerItemClickListener;
import com.cursoandroid.whatsappclone.helper.UsuarioFirebase;
import com.cursoandroid.whatsappclone.model.Conversa;
import com.cursoandroid.whatsappclone.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContatosFragment extends Fragment {
    private List<Usuario> listUsuarios = new ArrayList<>();
    private RecyclerView recyclerContatos;
    private DatabaseReference usuariosRef;
    private AdapterContatos adapter;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser usuarioAtual = UsuarioFirebase.getUsuarioAtual();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contatos, container, false);

        recyclerContatos = root.findViewById(R.id.recyclerContatos);
        usuariosRef = ConfigFirebase.getFirebaseDatabse().child("usuarios");

        //recuperarContatos();

        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperarContatos();
    }

    public void recuperarContatos(){
        valueEventListenerContatos = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                limparListaContatos();

                for(DataSnapshot dados : snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    if(!usuarioAtual.getEmail().equals(usuario.getEmail())) {
                        listUsuarios.add(usuario);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        configurarRecyclerView(listUsuarios);
    }

    public void limparListaContatos(){
        listUsuarios.clear();

        adicionarMenuNovoGrupo();
    }

    public void adicionarMenuNovoGrupo(){
        Usuario itemGrupo = new Usuario();
        itemGrupo.setNome("Novo Grupo");
        itemGrupo.setEmail("");

        listUsuarios.add(itemGrupo);
    }

    private void configurarRecyclerView(final List<Usuario> contatos){
        adapter = new AdapterContatos(contatos, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerContatos.setLayoutManager(layoutManager);
        recyclerContatos.setHasFixedSize(true);
        recyclerContatos.setAdapter(adapter);

        recyclerContatos.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerContatos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuarioSelecionado = contatos.get(position);
                boolean grupo = usuarioSelecionado.getEmail().isEmpty();
                Intent intent;

                if(grupo){
                    intent = new Intent(getActivity(), GrupoActivity.class);
                }
                else {
                    intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("chatContato", usuarioSelecionado);
                }

                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }

    public void pesquisarContatos(String pesquisa){
        List<Usuario> listContatosBusca = new ArrayList<>();

        for(Usuario contato : listUsuarios){
            if(contato.getNome() != null) {
                String nome = contato.getNome().toLowerCase();
                String email = contato.getEmail().toLowerCase();

                if (nome.contains(pesquisa) || email.contains(pesquisa)) {
                    listContatosBusca.add(contato);
                }
            }
        }

        configurarRecyclerView(listContatosBusca);
        adapter.notifyDataSetChanged();
    }

    public void recarregarContatos(){
        configurarRecyclerView(listUsuarios);
        adapter.notifyDataSetChanged();
    }
}