package com.example.finalproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    ArrayList<TrackInfo> trackInfo1 = new ArrayList<TrackInfo>();
    TrackQuery trackSearch = new TrackQuery();
    myListAdapter myListAdapter1 = new myListAdapter();
    TextView titleCard;
    Button webButton;
    long dbId;
    MyOpener dbOpener;
    Button saveButton;
    Button loadButton;
    ContentValues newRowValues = new ContentValues();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        Toast.makeText(getApplicationContext(),"Displaying Album Info", Toast.LENGTH_SHORT).show();
        ListView myListView = findViewById(R.id.listViewLayout1);
        dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();
        webButton = findViewById(R.id.webButton1);
        loadButton = findViewById(R.id.loadSongs);
        saveButton = findViewById(R.id.saveSongs);
        myListView.setAdapter(myListAdapter1);
        String passedURL = getIntent().getStringExtra("URL");
        trackSearch.execute(passedURL);
        titleCard = findViewById(R.id.albumNameDetails);
        loadButton.setOnClickListener(click->{
            trackInfo1.clear();
            myListAdapter1.notifyDataSetChanged();
            loadDataFromDatabase();
            myListAdapter1.notifyDataSetChanged();
        });

    }

    private void goToUrl(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private class TrackQuery extends AsyncTask<String, Integer, String> {
        String albumString, artistString, trackString, id;
        HttpURLConnection connection;
        ProgressBar progressBar;

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
                publishProgress(30);
                for (int i = 0; i < jArray.length(); i++)
                    try {
                        publishProgress(30 + i);
                        JSONObject anObject = jArray.getJSONObject(i);
                        id = anObject.getString("idAlbum");
                        trackString = anObject.getString("strTrack");
                        albumString = anObject.getString("strAlbum");
                        artistString = anObject.getString("strArtist");
                        Log.i("##", trackString);
                        trackInfo1.add(new TrackInfo(id, artistString, albumString, trackString));
                        //add to array list. IN of message, create an Album class
                    } catch (JSONException e) {
                        // handle the exception
                        Log.e("Crash!!!", e.getMessage());
                    }

            } catch (Exception e) {
                Log.e("Crash!!", e.getMessage());
            }
            publishProgress(100);
            return "Done";
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onProgressUpdate(Integer... args) {
            progressBar = findViewById(R.id.prog);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);
        }

        //Type3
        public void onPostExecute(String fromDoInBackground) {
            titleCard.setText("Track list from album " + albumString);
            myListAdapter1.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
saveButton.setOnClickListener(click->{
  for (int i=0;i<trackInfo1.size();i++){
      newRowValues.put(MyOpener.COL_ALBUM,trackInfo1.get(i).getAlbumName());
      newRowValues.put(MyOpener.COL_ARTIST,trackInfo1.get(i).getArtistName());
      newRowValues.put(MyOpener.COL_SONG,trackInfo1.get(i).getSongName());
          dbId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
      }

});

        }
    }

    public class myListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return trackInfo1.size();
        }

        @Override
        public TrackInfo getItem(int position) {

            return trackInfo1.get(position);

        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.listview1, parent, false);
            webButton = newView.findViewById(R.id.webButton1);
            webButton.setText(trackInfo1.get(position).getSongName());
            webButton.setOnClickListener(click -> {
                goToUrl("http://www.google.com/search?q=" + trackInfo1.get(position).getArtistName() + " " + trackInfo1.get(position).getSongName());
            });
            TextView displayArtist = newView.findViewById(R.id.textArtist);
            TextView displayAlbum = newView.findViewById(R.id.textAlbum);
            TextView displaySong = newView.findViewById(R.id.textSong);
            displayArtist.setText(trackInfo1.get(position).getArtistName());
            displayAlbum.setText(trackInfo1.get(position).getAlbumName());
            displaySong.setText(trackInfo1.get(position).getSongName());

            return newView;
        }

    }


    private void loadDataFromDatabase() {

        //get a database connection:

 //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:

        String[] columns = {MyOpener.COL_ID, MyOpener.COL_ARTIST, MyOpener.COL_ALBUM, MyOpener.COL_SONG};

        //query all the results from the database:

        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);


        //Now the results object has rows of results that match the query.

        //find the column indices:
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int artistColumnIndex = results.getColumnIndex(MyOpener.COL_ARTIST);
        int albumColumnIndex = results.getColumnIndex(MyOpener.COL_ALBUM);
        int songColIndex = results.getColumnIndex(MyOpener.COL_SONG);

        //iterate over the results, return true if there is a next item:

        while (results.moveToNext()) {
            long index = results.getLong(idColIndex);
            String artist = results.getString(artistColumnIndex);
            String album = results.getString(albumColumnIndex);
            String song = results.getString(songColIndex);


            //add the new Contact to the array list:

            trackInfo1.add(new TrackInfo(index,artist, album, song));

        }   myListAdapter1.notifyDataSetChanged();

        //At this point, the contactsList array has loaded every row from the cursor.

    }
}