package com.example.final_group_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton recipeSearch;
    private Toolbar tb;
    private DrawerLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.rootMain);
        tb = findViewById(R.id.mainTb);
        setSupportActionBar(tb);

        recipeSearch = findViewById(R.id.receiptSearch);
        recipeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRecipeSearch = new Intent(MainActivity.this,RecipeSearchPage.class);
                startActivity(goToRecipeSearch);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tb_menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.recipeMenuBtn){
            Intent goToRecipeSearch = new Intent(MainActivity.this,RecipeSearchPage.class);
            startActivity(goToRecipeSearch);
        }
        return true;
    }
}