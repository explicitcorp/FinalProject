package com.example.finalproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;

public class AudioDB extends AppCompatActivity {
    ArrayList<AlbumInfo> albumArray = new ArrayList<>();
    Button searchButton;
    myListAdapter myListAdapter = new myListAdapter();
    AlbumQuery albumSearch = null;
    EditText searchText = null;
    String enteredText = null;
    Boolean testNull;
    TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_d_b);

        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "Click for help", Snackbar.LENGTH_INDEFINITE)
        .setAction("HELP", dview -> {
            Builder alertDialog = new Builder(AudioDB.this);
            alertDialog.setTitle("Search Help");
            alertDialog.setMessage("To find the album details of the artist you're looking for please enter their name and click Search.");
            alertDialog.setNegativeButton("OK", (click, arg) -> {
            });
            alertDialog.create();
            alertDialog.show();
        }).setActionTextColor(Color.RED).show();
        searchButton = findViewById(R.id.button);

        ListView myList = findViewById(R.id.listViewLayout);
        myList.setAdapter(myListAdapter);
        errorView = findViewById(R.id.error);
        searchButton.setOnClickListener(click -> {
            errorView.setText(null);
           albumArray.clear();
            myListAdapter.notifyDataSetChanged();

            searchText = findViewById(R.id.searchBar);
            enteredText = searchText.getText().toString();
            albumSearch = new AlbumQuery();
            albumSearch.execute(enteredText);
            searchText.getText().clear();

        });

        myList.setOnItemClickListener((parent, view, position, id)->{
            Intent trackInfo = new Intent(this, AlbumDetails.class);
            trackInfo.putExtra("URL", "https://theaudiodb.com/api/v1/json/1/track.php?m="+albumArray.get(position).getId());
            startActivity(trackInfo);



        });
        myList.setOnItemLongClickListener((parent, view, position, id)->{
Bundle dataPass = new Bundle();
dataPass.putString("Artist", albumArray.get(position).getArtistName());
            dataPass.putString("Album", albumArray.get(position).getAlbumName());
            DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
            dFragment.setArguments( dataPass ); //pass it a bundle for information
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl, dFragment) //Add the fragment in FrameLayout
                    .commit();
return true;
        });


    }


    public class myListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return albumArray.size();
        }

        @Override
        public AlbumInfo getItem(int position) {

            return albumArray.get(position);

        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.listview, parent, false);
            TextView displayArtist = newView.findViewById(R.id.textArtist);
            TextView displayAlbum = newView.findViewById(R.id.textAlbum);
            TextView displayYear = newView.findViewById(R.id.textYear);
            ImageView displayAlbumArt = newView.findViewById(R.id.imageView);
            displayArtist.setText(albumArray.get(position).getArtistName());
            displayAlbum.setText(albumArray.get(position).getAlbumName());
            displayYear.setText(albumArray.get(position).getYear());
displayAlbumArt.setImageBitmap(albumArray.get(position).getAlbumArt());
            return newView;
        }

    }


    private class AlbumQuery extends AsyncTask<String, Integer, String> {
        Bitmap mIcon11;

        String albumString, artistString, yearString, id, albumArt,noneMessage="Sorry, this artist is not found.";
        HttpURLConnection connection;
        ProgressBar progressB;

        public String doInBackground(String... args) {
            try {

                URL url1 = new URL("https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s=" + URLEncoder.encode(args[0], "UTF-8"));

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
                JSONObject nully = new JSONObject((result));//convet string to jsonobject
                 testNull = nully.isNull("album");
                Log.i("#", String.valueOf(testNull));
                JSONArray jArray = resp.getJSONArray("album");
publishProgress(40);



                for (int i = 0; i < jArray.length(); i++)
                    try {
                        JSONObject anObject = jArray.getJSONObject(i);
                        // Pulling items from the array


                        albumString = anObject.getString("strAlbum");
                        id = anObject.getString("idAlbum");
                        Log.i("Test#", albumString);
                        artistString = anObject.getString("strArtist");
                        yearString = anObject.getString("intYearReleased");
                        albumArt = anObject.getString("strAlbumThumb");
                        publishProgress(i+40);
                        String urldisplay = albumArt;
                        mIcon11= null;
                        try {
                            InputStream in = new java.net.URL(urldisplay).openStream();
                            mIcon11 = BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                            e.printStackTrace();
                        }
                        albumArray.add(new AlbumInfo(id, artistString, albumString, yearString, mIcon11));
                        Log.i("##", albumArray.get(i).getAlbumName());
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
if(testNull){

            errorView.setText(noneMessage);}
            myListAdapter.notifyDataSetChanged();
            progressB.setVisibility(View.INVISIBLE);


        }
    }
}

