package com.cursoandroid.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.adapter.AdapterMiniaturasFiltros;
import com.cursoandroid.instagramclone.helper.RecyclerItemClickListener;
import com.cursoandroid.instagramclone.helper.SalvarFotoFirebase;
import com.cursoandroid.instagramclone.helper.UsuarioFirebase;
import com.cursoandroid.instagramclone.model.Postagem;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FiltroActivity extends AppCompatActivity {
    static{
        System.loadLibrary("NativeImageProcessor");
    }
    private ImageView imgFotoPostagem;
    private TextView txtLegendaPostagem;
    private Bitmap imagem, imagemComFiltro;
    private List<ThumbnailItem> listaFiltros;
    private RecyclerView recyclerFiltros;
    private AdapterMiniaturasFiltros adapterMiniaturasFiltros;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        imgFotoPostagem = findViewById(R.id.imgFotoPostagem);
        txtLegendaPostagem = findViewById(R.id.txt_legenda_publicacao);
        recyclerFiltros = findViewById(R.id.recycler_filtros_postagem);
        listaFiltros = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle(R.string.filtros);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        //Recuperando imagem escolhida pelo usuário
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            byte[] dadosImagem = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dadosImagem, 0, dadosImagem.length);
            imgFotoPostagem.setImageBitmap(imagem);
            imagemComFiltro = imagem.copy(imagem.getConfig(), true);

            configuracaoRecyclerView();

            recuperarFiltros();
        }
    }

    private void recuperarFiltros(){
        //Limpar itens
        ThumbnailsManager.clearThumbs();
        listaFiltros.clear();

        //Configurar filtro noemal
        ThumbnailItem item = new ThumbnailItem();
        item.image = imagem;
        item.filterName = "Normal";
        ThumbnailsManager.addThumb(item);

        //Listar todos os filtros
        List<Filter> filtros = FilterPack.getFilterPack(getApplicationContext());
        for(Filter filtro : filtros){
            ThumbnailItem itemFiltro = new ThumbnailItem();
            itemFiltro.image = imagem;
            itemFiltro.filter = filtro;
            itemFiltro.filterName = filtro.getName();

            ThumbnailsManager.addThumb(itemFiltro);
        }
        listaFiltros.addAll(ThumbnailsManager.processThumbs(getApplicationContext()));
        adapterMiniaturasFiltros.notifyDataSetChanged();
    }

    private void configuracaoRecyclerView(){
        adapterMiniaturasFiltros = new AdapterMiniaturasFiltros(listaFiltros, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerFiltros.setLayoutManager(layoutManager);
        recyclerFiltros.setAdapter(adapterMiniaturasFiltros);

        recyclerFiltros.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerFiltros, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ThumbnailItem item = listaFiltros.get(position);

                Filter filtro = item.filter;
                imgFotoPostagem.setImageBitmap(filtro.processFilter(imagemComFiltro));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtro, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.salvar_postagem :
                publicarPostagem();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void publicarPostagem() {
        Postagem postagem = new Postagem();
        postagem.setIdUsuario(UsuarioFirebase.getIdUsuario());
        postagem.setDescricao(txtLegendaPostagem.getText().toString());

        SalvarFotoFirebase salvarFotoFirebase = new SalvarFotoFirebase("postagens", postagem.getId());
        salvarFotoFirebase.salvar(imgFotoPostagem, this, postagem);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}