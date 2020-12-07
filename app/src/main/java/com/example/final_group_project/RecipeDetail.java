package com.example.final_group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class RecipeDetail extends AppCompatActivity {

    private TextView titleTv;
    private TextView hrefTv;
    private TextView ingredientsTv;
    private Button saveBtn;
    private ImageView thumbnailView;
    private String thumbnail;
    private SqlHelper sqlHelper;
    private Recipe recipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        titleTv = findViewById(R.id.titlevalue);
        hrefTv = findViewById(R.id.hrefvalue);
        ingredientsTv = findViewById(R.id.ingredientvalue);
        saveBtn = findViewById(R.id.saveBtn);
        thumbnailView = findViewById(R.id.thumbnail);
        sqlHelper = new SqlHelper(getApplicationContext());

        Bundle dataFromPre = getIntent().getBundleExtra(RecipeSearchPage.DATA_FROM_RSP);
        recipe = (Recipe) dataFromPre.getSerializable(RecipeSearchPage.RECIPE_KEY);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = sqlHelper.addRecipe(recipe);
                if(id<0){
                    Toast.makeText(RecipeDetail.this, "Failed to save ...", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RecipeDetail.this, "Save this recipe to your favorites id = "+id, Toast.LENGTH_SHORT).show();
                    Intent goToFav = new Intent(RecipeDetail.this,FavRecipes.class);
                    startActivity(goToFav);
                }
            }
        });

        titleTv.setText(recipe.getTitle());
        hrefTv.setText(recipe.getHref());
        ingredientsTv.setText(recipe.getIngredients());
        thumbnail = recipe.getThumbnail();
//        Bitmap thumbnailPic = getBitmapFromURL(thumbnail);
//        thumbnailView.setImageBitmap(thumbnailPic);
        Log.e("Thumbnail",thumbnail);
        ImageLoader imageLoader = new ImageLoader(thumbnail);
        imageLoader.execute();

    }

    private class ImageLoader extends AsyncTask<String , Integer ,String>{

        private String url;
        private Bitmap myBitmap;

        public ImageLoader(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... strings) {
            myBitmap = getBitmapFromURL(url);
            return "Done";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            thumbnailView.setImageBitmap(myBitmap);

        }

        private Bitmap getBitmapFromURL(String src) {
            try {
                java.net.URL url = new java.net.URL(src);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}