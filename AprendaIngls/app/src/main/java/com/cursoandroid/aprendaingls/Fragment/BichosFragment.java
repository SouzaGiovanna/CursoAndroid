package com.cursoandroid.aprendaingls.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cursoandroid.aprendaingls.R;
import com.cursoandroid.aprendaingls.helper.PlayerAudio;

public class BichosFragment extends Fragment {
    private ImageView btnDog, btnCat, btnCow, btnLion, btnMonkey, btnSheep;
    private MediaPlayer mediaPlayer;
    private PlayerAudio player = new PlayerAudio();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bichos, container, false);

        btnDog = root.findViewById(R.id.btnDog);
        btnCat = root.findViewById(R.id.btnCat);
        btnCow = root.findViewById(R.id.btnCow);
        btnLion = root.findViewById(R.id.btnLion);
        btnMonkey = root.findViewById(R.id.btnMonkey);
        btnSheep = root.findViewById(R.id.btnSheep);

        btnCow.setImageResource(R.drawable.cow);
        btnLion.setImageResource(R.drawable.lion);
        btnMonkey.setImageResource(R.drawable.monkey);
        btnSheep.setImageResource(R.drawable.sheep);

        btnDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.dog);

                player.executarSom(mediaPlayer);
            }
        });

        btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.cat);

                player.executarSom(mediaPlayer);
            }
        });

        btnCow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.cow);

                player.executarSom(mediaPlayer);
            }
        });

        btnLion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.lion);

                player.executarSom(mediaPlayer);
            }
        });

        btnMonkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.monkey);

                player.executarSom(mediaPlayer);
            }
        });

        btnSheep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.sheep);

                player.executarSom(mediaPlayer);
            }
        });

        return root;
    }
}