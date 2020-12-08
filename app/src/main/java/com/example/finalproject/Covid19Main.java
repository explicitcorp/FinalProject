package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Covid19Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final String URL_BASE = "https://api.covid19api.com/country/";
    final String URL_MIDDLE = "T00:00:00Z";
    final String URL_END = "T23:59:59Z";

    CovidProperties covidProperties;
    List<CovidProperties> covidList = new ArrayList<>();
    List<CovidSavedInfo> covidSavedList = new ArrayList<>();
    ListView covidListView;
    private MyListAdapter myAdapter = new MyListAdapter();
    String countryName;
    String caseDate;
    ProgressBar progressBar;
    SQLiteDatabase db;
    private boolean isTabletMode, isError = false;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    CovidFragment cFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid19_main);
        isTabletMode = findViewById(R.id.covidFrame) != null;

        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(tBar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        Button searchButton = findViewById(R.id.covidSearch);

        final EditText country = findViewById(R.id.covidCountryName);
        country.setText("Canada");
        final EditText dateInfo = findViewById(R.id.covidDate);
        dateInfo.setText("2020-12-06");

        Date dateT = Calendar.getInstance().getTime();
        dateInfo.setText(dateFormat.format(dateT));
        covidListView = findViewById(R.id.covidList);
        covidListView.setAdapter(myAdapter);
        getSavedInfo();
        progressBar = findViewById(R.id.progressBar);
        SwipeRefreshLayout refresher = findViewById(R.id.swipeRefresher);
        SharedPreferences prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        countryName = prefs.getString("country", "");
        caseDate = prefs.getString("date", "");
        if (countryName != null) country.setText(countryName);
        if (caseDate != null) dateInfo.setText(caseDate);
        myAdapter.notifyDataSetChanged();

        searchButton.setOnClickListener(v -> {
            isError = false;
            countryName = country.getText().toString();
            caseDate = dateInfo.getText().toString();
            try {
                Date dateObject = dateFormat.parse(caseDate);
                dateFormat.setLenient(false);
            } catch (ParseException e) {
                isError = true;
                Toast.makeText(this, "Please enter date in YYYY-MM-DD format", Toast.LENGTH_LONG).show();
            }

            countryName = countryName.replaceAll(" ", "%20");

            String link = caseDate + URL_MIDDLE + "&to=" + caseDate + URL_END;
            if (countryName.trim() == "" || countryName == null) {
                Toast.makeText(this, "Please enter Country name", Toast.LENGTH_LONG).show();
                isError = true;
            }
            Log.e ("Covid",URL_BASE + countryName + "/status/confirmed/live?from=" + link);
            if (!isError) {
                link = URL_BASE + countryName + "/status/confirmed/live?from=" + link;
                Bundle dataToPass = new Bundle();
                dataToPass.putString("countryName", countryName);
                dataToPass.putString("date", caseDate);
                dataToPass.putString("link", link);
                dataToPass.putBoolean("isTabletMode", isTabletMode);
                dataToPass.putBoolean("fromDb", false);
                //dataToPass.putSerializable ("covidList", (Serializable) covidList);
                if(isTabletMode) {
                    cFragment = new CovidFragment();
                    cFragment.setArguments( dataToPass );
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.covidFrame, cFragment)
                            .commit();
                }else {
                    Intent nextActivity = new Intent(Covid19Main.this, CovidEmptyActivity.class);
                    nextActivity.putExtras(dataToPass);
                    startActivity(nextActivity);
                }
            }

        });

        covidListView.setOnItemLongClickListener( (parent, view, position, id) -> {
            Bundle dataToPass = new Bundle();
            CovidSavedInfo covid = (CovidSavedInfo) covidSavedList.get(position);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to show information?")
                    .setMessage("The selected row is: " + (position+1) + "\nThe database id is: " + (myAdapter.getItemId(position)+1) )
                    .setPositiveButton("Yes", (click, arg) -> {
                        dataToPass.putString("countryName", covid.getCountry());
                        dataToPass.putString("date", covid.getDate());
                        dataToPass.putString("link", covid.getLink());
                        dataToPass.putLong("id", covid.getId());
                        dataToPass.putBoolean("isTabletMode", isTabletMode);
                        dataToPass.putBoolean("fromDb", true);

                        if(isTabletMode) {
                            cFragment = new CovidFragment();
                            cFragment.setArguments( dataToPass );
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.covidFrame, cFragment)
                                    .commit();
                        }else {
                            Intent nextActivity = new Intent(Covid19Main.this, CovidEmptyActivity.class);
                            nextActivity.putExtras(dataToPass);
                            startActivity(nextActivity);
                        }
                    })
                    .setNegativeButton("No",  (click, arg) -> {
                        Toast.makeText(Covid19Main.this, "will not showed", Toast.LENGTH_LONG).show();})
                    .create().show();
            return true;
        }   );
    }
    private void getSavedInfo()
    {
        db = openOrCreateDatabase(CovidFragment.DATABASE_NAME,MODE_PRIVATE,null);;
        covidSavedList.clear();
        String [] columns = {"_id","country","date","link"};
        try {
            Cursor results = db.query(false, CovidFragment.TABLE_NAME, columns, null, null, null, null, null, null);
            results.moveToFirst();
            do {
                String country = results.getString(1);
                String date = results.getString(2);
                String link = results.getString(3);
                long id = results.getLong(0);

                covidSavedList.add(new CovidSavedInfo(id, country, date, link));
            } while (results.moveToNext());
        } catch (SQLiteException | CursorIndexOutOfBoundsException ex) {
            Toast.makeText(this, "There is no saved information", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("country", countryName);
        editor.putString("date", caseDate);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.home).setVisible(true);
        menu.findItem(R.id.covid).setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Intent goToMainActivity = new Intent(this, MainActivity.class);
                startActivity(goToMainActivity);
                break;

        }
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Intent goToMainActivity = new Intent(this, MainActivity.class);
                startActivity(goToMainActivity);
                break;

        }
        return false;
    }


    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return covidSavedList.size();
        }

        public Object getItem(int position) {
            return covidSavedList.get(position);
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            CovidSavedInfo casesInformation = (CovidSavedInfo) getItem(position);
            View newView = inflater.inflate(R.layout.covid_list_layout, null);
            TextView tView = newView.findViewById(R.id.showInfo);
            tView.setText(casesInformation.getCountry()+" "+ casesInformation.getDate() );
            return newView;
        }

        public long getItemId(int position) {
            return (long) position;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getSavedInfo();
        myAdapter.notifyDataSetChanged();
    }
}