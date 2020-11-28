package com.example.finalproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AlbumDetails extends AppCompatActivity {
ArrayList<TrackInfo> trackInfo = new ArrayList<TrackInfo>();
TrackQuery trackSearch = new TrackQuery();
myListAdapter myListAdapter = new myListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
String passedURL = getIntent().getStringExtra("URL");
trackSearch.execute(passedURL);


    }
    private class TrackQuery extends AsyncTask<String, Integer, String> {
        String albumString, artistString, trackString, id;
        HttpURLConnection connection;
        ProgressBar progressB;

        public String doInBackground(String... args) {
        try {

            URL url1 = new URL(args[0]);

            Log.i("#", url1.toString());

            HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();

            //wait for data:
            InputStream response = urlConnection.getInputStream();

            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            String result = sb.toString();
            Log.i("l#ongStuff", result);
            JSONObject resp = new JSONObject((result));//convet string to jsonobject
            JSONArray jArray = resp.getJSONArray("track");
            publishProgress(40);



            for (int i = 0; i < jArray.length(); i++)
                try {
                    JSONObject anObject = jArray.getJSONObject(i);
                    id = anObject.getString("idAlbum");
                     trackString= anObject.getString("strTrack");
                    Log.i("Test#", albumString);
                    albumString = anObject.getString("strAlbum");
                 artistString  = anObject.getString("strArtist");
                    publishProgress(i+40);
                    trackInfo.add(new TrackInfo(id, artistString, albumString, trackString ));
                    Log.i("##", trackInfo.get(i).getAlbumName());
                    //add to array list. IN of message, create an Album class
                } catch (JSONException e) {
                    // handle the exception
                    Log.e("Crash!!", e.getMessage());
                }

        } catch (Exception e) {
            Log.e("Crash!!", e.getMessage());
        }
        publishProgress(100);
        return "Done";
    }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onProgressUpdate(Integer... args) {
        progressB = findViewById(R.id.progressBar);
        progressB.setVisibility(View.VISIBLE);
        progressB.setProgress(args[0]);
    }

        //Type3
        public void onPostExecute(String fromDoInBackground) {

        myListAdapter.notifyDataSetChanged();
        progressB.setVisibility(View.INVISIBLE);


    }
    }
    public class myListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return trackInfo.size();
        }

        @Override
        public TrackInfo getItem(int position) {

            return trackInfo.get(position);

        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.listview1, parent, false);
            TextView displayArtist = newView.findViewById(R.id.textArtist);
            TextView displayAlbum = newView.findViewById(R.id.textAlbum);
            TextView displaySong = newView.findViewById(R.id.textSong);
            displayArtist.setText(trackInfo.get(position).getArtistName());
            displayAlbum.setText(trackInfo.get(position).getAlbumName());
            displaySong.setText(trackInfo.get(position).getSongName());
            return newView;
        }

    }
}