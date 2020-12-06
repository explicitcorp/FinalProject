package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent audioPage = new Intent(this, AudioDB.class);
        Button audioButton = findViewById(R.id.audioActivityButton);

        audioButton.setOnClickListener(clk -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Are you sure?");
            alertDialog.setMessage("Would you like to continue to AudioDB?");
            alertDialog.setPositiveButton("Yes", (click, arg) -> {
                startActivity(audioPage);
            });
            alertDialog.setNegativeButton("No", (click, arg) -> {
            });
            alertDialog.create();
            alertDialog.show();

        });

    }
}