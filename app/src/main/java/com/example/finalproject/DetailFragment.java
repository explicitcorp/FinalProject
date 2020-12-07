package com.example.finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailFragment extends Fragment {

    private Bundle dataFromActivity;
    private String album,artist;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        album = dataFromActivity.getString("Artist");
        artist = dataFromActivity.getString("Album");

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_blank, container, false);

        //show the message
        TextView fragArtist = (TextView)result.findViewById(R.id.fragmentArtist);
        fragArtist.setText(artist);
        TextView fragAlbum = (TextView)result.findViewById(R.id.fragmentAlbum);
        fragAlbum.setText(album);

        parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        parentActivity = (AppCompatActivity)context;
    }
}

