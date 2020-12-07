package com.example.final_group_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RecipeSearchPage extends AppCompatActivity {

    private RelativeLayout root;
    private Button searchBtn;
    private ListView recipeLv;
    private EditText searchItem;
    private EditText searchIngredient;
    private Toolbar tb_recipe;
    private ArrayList<Recipe> recipes;
    private RecipeAdapter recipeAdapter;
    private SharedPreferences sp;
    private static final String SEARCH_ITEM = "SEARCH_ITEM";
    private static final String SEARCH_INGREDIENTS = "SEARCH_INGREDIENTS";
    private static final String SP_SEARCH_HISTORY = "search_history";
    public static final String RECIPE_KEY = "RECIPE_KEY";
    public static final String DATA_FROM_RSP = "DATA_FROM_RSP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search_page);

        recipes = new ArrayList<>();
        searchBtn = findViewById(R.id.searchBtn);
        recipeLv = findViewById(R.id.recipeLv);
        searchItem = findViewById(R.id.itemSearchBox);
        searchIngredient =findViewById(R.id.ingredientSearchBox);
        tb_recipe = findViewById(R.id.tb_recipepage);
        root =findViewById(R.id.rootRecipePage);

        setSupportActionBar(tb_recipe);
        sp = getSharedPreferences(SP_SEARCH_HISTORY,Context.MODE_PRIVATE);
        String lastItemSearch = sp.getString(SEARCH_ITEM,"");
        String lastIngredientSearch = sp.getString(SEARCH_INGREDIENTS,"");
        searchItem.setHint("Last search : "+lastItemSearch);
        searchIngredient.setHint("Last search : "+lastIngredientSearch);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredient = searchIngredient.getText().toString();
                String search = searchItem.getText().toString();
                sp= getSharedPreferences(SP_SEARCH_HISTORY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(SEARCH_ITEM,search);
                editor.putString(SEARCH_INGREDIENTS,ingredient);
                editor.commit();
                RecipeQuery query = new RecipeQuery(ingredient,search);
                query.execute();
            }
        });

        recipeAdapter = new RecipeAdapter(recipes,getApplicationContext());
        recipeLv.setAdapter(recipeAdapter);

        recipeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goToDetail = new Intent(RecipeSearchPage.this,RecipeDetail.class);
                Bundle dataToPass = new Bundle();
                Recipe recipeToPass = (Recipe) recipeLv.getAdapter().getItem(position);
                dataToPass.putSerializable(RECIPE_KEY,recipeToPass);
                goToDetail.putExtra(DATA_FROM_RSP, dataToPass);
                startActivity(goToDetail);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tb_menu_recipe_page,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.backBtn){
            Snackbar.make(root,"Do you wanna go back",Snackbar.LENGTH_LONG).setAction("Back", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).show();
        }
        return true;
    }

    private class RecipeQuery extends AsyncTask<String ,Integer, String>{

        private static final  String QUERY_URL
                = "http://www.recipepuppy.com/api/?";
        private static final String INGREDIENT = "i=";
        private static final String AND = "&";
        private static final String ITEM = "q=";
        private static final String P = "p=3";

        private String ingredients;
        private String item;
        private String fullUrl;

        public RecipeQuery(String ingredients, String item) {
            this.ingredients = ingredients;
            this.item = item;
            this.fullUrl = QUERY_URL+INGREDIENT+ingredients+AND+ITEM+item+AND+P;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL myUrl = new URL(this.fullUrl);
                Log.e("Fulll URL", this.fullUrl);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String line = "";
                StringBuilder builder = new StringBuilder();

                while((line=reader.readLine())!=null){
                    builder.append(line);
                }

                String resultStr = builder.toString();
                Log.e("Result string", resultStr);

                JSONObject jsonObject = new JSONObject(resultStr);
                JSONArray resultArr = jsonObject.getJSONArray("results");
                recipes.clear();
                for(int i=0;i<resultArr.length();i++){
                    JSONObject obj = resultArr.getJSONObject(i);
                    String title = obj.getString("title");
                    String href = obj.getString("href");
                    String ingredients = obj.getString("ingredients");
                    String thumbnail = obj.getString("thumbnail");
                    Recipe recipe = new Recipe(title,href,ingredients,thumbnail);
                    Log.e(recipe.getTitle(),recipe.toString());
                    recipes.add(recipe);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Done";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            recipeAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}