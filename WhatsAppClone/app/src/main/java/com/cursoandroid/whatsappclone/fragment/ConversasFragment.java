package com.cursoandroid.whatsappclone.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cursoandroid.whatsappclone.R;
import com.cursoandroid.whatsappclone.activity.ChatActivity;
import com.cursoandroid.whatsappclone.adapter.AdapterConversas;
import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.config.RecyclerItemClickListener;
import com.cursoandroid.whatsappclone.helper.UsuarioFirebase;
import com.cursoandroid.whatsappclone.model.Conversa;
import com.cursoandroid.whatsappclone.model.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ConversasFragment extends Fragment {
    private RecyclerView recyclerConversas;
    private AdapterConversas adapter;
    private List<Conversa> listConversas = new ArrayList<>();
    private ChildEventListener childEventListener;
    private DatabaseReference database = ConfigFirebase.getFirebaseDatabse();
    private DatabaseReference conversaRef;
    private String idUsuario = UsuarioFirebase.getIdentificadorUsuario();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_conversas, container, false);

        recyclerConversas = root.findViewById(R.id.recyclerConversas);

        conversaRef = database.child("conversas").child(idUsuario);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversaRef.removeEventListener(childEventListener);
    }

    private void recuperarConversas(){
        listConversas.clear();

        childEventListener = conversaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Conversa conversa = snapshot.getValue(Conversa.class);

                listConversas.add(conversa);

                adapter.notifyDataSetChanged();

                
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        configurarRecycler(listConversas);
    }

    private void configurarRecycler(final List<Conversa> conversas){
        adapter = new AdapterConversas(conversas, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerConversas.setLayoutManager(layoutManager);
        recyclerConversas.setVerticalScrollBarEnabled(true);
        recyclerConversas.setHasFixedSize(true);
        recyclerConversas.setAdapter(adapter);

        //Configurar evento de click
        recyclerConversas.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerConversas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Conversa conversaSelecionada = conversas.get(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                if(conversaSelecionada.getIsGroup().equals("true")){
                    intent.putExtra("chatGrupo", conversaSelecionada.getGrupo());
                }
                else {
                    intent.putExtra("chatContato", conversaSelecionada.getUsuarioExibicao());
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

    public void pesquisarConversas(String pesquisa){
        List<Conversa> listConversasBusca = new ArrayList<>();

        for(Conversa conversa : listConversas){
            if(conversa.getUsuarioExibicao() != null) {
                String nome = conversa.getUsuarioExibicao().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimaMensagem().toLowerCase();

                if (nome.contains(pesquisa) || ultimaMsg.contains(pesquisa)) {
                    listConversasBusca.add(conversa);
                }
            }
            else{
                String nome = conversa.getGrupo().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimaMensagem().toLowerCase();

                if (nome.contains(pesquisa) || ultimaMsg.contains(pesquisa)) {
                    listConversasBusca.add(conversa);
                }
            }
        }

        configurarRecycler(listConversasBusca);
        adapter.notifyDataSetChanged();
    }

    public void recarregarConversas(){
        configurarRecycler(listConversas);
        adapter.notifyDataSetChanged();
    }
}