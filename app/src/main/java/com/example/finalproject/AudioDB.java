package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AudioDB extends AppCompatActivity {
    ArrayList<AlbumInfo> albumInfo = new ArrayList<>();
    Button searchButton;
    myListAdapter myListAdapter= new myListAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_d_b);
       searchButton=findViewById(R.id.button) ;
        ListView myList = findViewById(R.id.listViewLayout);

        myList.setAdapter(myListAdapter);
        searchButton.setOnClickListener(click->{
            AlbumInfo newAlbum = new AlbumInfo("cool");
            albumInfo.add(newAlbum);

            myListAdapter.notifyDataSetChanged();
        });

        }


    public class myListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return albumInfo.size();
        }

        @Override
        public AlbumInfo getItem(int position) {

            return albumInfo.get(position);

        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

               View newView = inflater.inflate(R.layout.activity_audio_d_b, parent, false);
                TextView display = newView.findViewById(R.id.textView2);
              display.setText(albumInfo.get(position).getAlbumName());
                return newView;
            }





        }


    }