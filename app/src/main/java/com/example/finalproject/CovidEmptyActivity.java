package com.example.finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class CovidEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_empty_activity);

        Bundle dataToPass = getIntent().getExtras();

        CovidFragment cFragment = new CovidFragment();
        cFragment.setArguments( dataToPass );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.covidFrame, cFragment)
                .commit();
    }
}
