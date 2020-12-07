package com.example.final_group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class FavRecipes extends AppCompatActivity {
    private ListView favList;
    private ArrayList<Recipe>  recipes;
    private SqlHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_recipes);
        helper = new SqlHelper(getApplicationContext());
        recipes= helper.getAllRecipes();
        Log.e("Recipes", recipes.toString());
        RecipeAdapter adapter = new RecipeAdapter(recipes, getApplicationContext());
        favList = findViewById(R.id.favList);
        favList.setAdapter(adapter);
    }
}