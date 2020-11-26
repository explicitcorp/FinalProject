package com.example.finalproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.ArrayList;

public class AudioDB extends AppCompatActivity {
    ArrayList<AlbumInfo> albumArray = new ArrayList<>();
    Button searchButton;
    myListAdapter myListAdapter= new myListAdapter();
    AlbumQuery albumSearch=null;
    EditText searchText=null;
    String enteredText=null;
    AlbumInfo infoM;
    String jsonAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_d_b);
       searchButton=findViewById(R.id.button) ;

        ListView myList = findViewById(R.id.listViewLayout);
        myList.setAdapter(myListAdapter);


        searchButton.setOnClickListener(click->{

            searchText=findViewById(R.id.searchBar);
            enteredText = searchText.getText().toString();
            albumSearch = new AlbumQuery();
            albumSearch.execute(enteredText);
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

               View newView = inflater.inflate(R.layout.listview ,parent, false);
                TextView display = newView.findViewById(R.id.textView2);
              display.setText(albumArray.get(position).getAlbumName());
                return newView;
            }

        }


    private class AlbumQuery extends AsyncTask< String, Integer, String> {
        TextView albumSet;
        TextView artistSet;
        TextView yearSet;
        String albumString, artistString, yearString;
        HttpURLConnection connection;
        ProgressBar progressB ;

        public String doInBackground(String ... args)
        {
            try {

URL url1 = new URL("https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s="+args[0]);

Log.i("#",url1.toString());

                HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();

                urlConnection.getInputStream();
                InputStream response;


                //open the connection
                urlConnection = (HttpURLConnection) url1.openConnection();

                //wait for data:
                response = urlConnection.getInputStream();

                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                Log.i("longStuff", result);
                JSONObject resp = new JSONObject(result);//convet string to jsonobject

                JSONArray jArray = resp.getJSONArray("album");

                for (int i=0; i < jArray.length(); i++)
                    try {
                        JSONObject anObject = jArray.getJSONObject(i);
                        // Pulling items from the array
                        albumString = anObject.getString("strAlbum");
                        Log.i("Test#", albumString);
                       artistString = anObject.getString("strArtist");
                       yearString =   anObject.getString("intYearReleased");
                    } catch (JSONException e) {
                        // handle the exception
                    }




            }
            catch (Exception e)
            {

            }

            return "Done";
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onProgressUpdate(Integer ... args)
        {
            progressB.setVisibility(View.VISIBLE);
            progressB.setProgress(args[0]);
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
           albumArray.add(new AlbumInfo(albumString));


        }
    }
}

