package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CovidFragment extends Fragment {
    CovidSearchQuery covidSearchquery;
    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;
    protected final static String DATABASE_NAME = "Project";
    protected final static int VERSION_NUM = 1;
    protected final static String TABLE_NAME = "COVID19";
    protected final static String COL_ID = "_id";
    protected final static String COL_COUNTRY = "country";
    protected final static String COL_DATE = "date";
    protected final static String COL_LINK = "link";
    SQLiteDatabase db;
    String countryName, date, link;
    List<CovidProperties> covidList = new ArrayList<>();
    ListView covidListView;
    private MyListAdapter myAdapter = new MyListAdapter();
    long id;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        View result = inflater.inflate(R.layout.covid_fragment, container, false);
        countryName = dataFromActivity.getString("countryName");
        date = dataFromActivity.getString("date");
        link = dataFromActivity.getString("link");

        //covidList = (ArrayList<CovidProperties>)dataFromActivity.getSerializable("covidList");
        /**
        if (!dataFromActivity.getBoolean("isTabletMode")) {
            Toolbar tBar = (Toolbar) result.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(tBar);
            setHasOptionsMenu(true);
            tBar.inflateMenu(R.menu.toolbar_menu);
        }
         */
        progressBar = result.findViewById(R.id.progressBar);
        Button covidSave = result.findViewById(R.id.covidSave);
        Button covidDelete = result.findViewById(R.id.covidDelete);
        covidSearchquery = new CovidSearchQuery();
        covidSearchquery.execute(link);
        covidListView = result.findViewById(R.id.covidList);
        covidListView.setAdapter(myAdapter);

        boolean fromDb = dataFromActivity.getBoolean("fromDb");
        if(fromDb) {
            id = dataFromActivity.getLong("id");
            covidSave.setEnabled(false);
            covidDelete.setEnabled(true);
        }else {
            covidSave.setEnabled(true);
            covidDelete.setEnabled(false);
        }


        covidSave.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("Do you want to save this Covid information?")
                    .setPositiveButton("Yes", (click, arg) -> {
                        ContentValues newRowValues = new ContentValues();

                        MyOpener dbOpener = new MyOpener(getActivity());
                        db = dbOpener.getWritableDatabase();
                        newRowValues.put(COL_COUNTRY, countryName);
                        newRowValues.put(COL_DATE, date);
                        newRowValues.put(COL_LINK, link);
                        long newId = db.insert(TABLE_NAME, null, newRowValues);
                        Toast.makeText(getActivity(), "Covid information saved", Toast.LENGTH_LONG).show();
                        covidSave.setEnabled(false);
                        covidDelete.setEnabled(true);

                    })
                    .setNegativeButton("No",  (click, arg) -> {
                        Toast.makeText(getActivity(), "Not saved", Toast.LENGTH_LONG).show();})
                    .create().show();
        });

        covidDelete.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("Do you want to delete this Covid data?")
                    .setPositiveButton("Yes", (click, arg) -> {
                        MyOpener dbOpener = new MyOpener(getActivity());
                        db = dbOpener.getWritableDatabase();
                        deleteContact((long) id);
                        Toast.makeText(getActivity(), "Covid information deleted", Toast.LENGTH_LONG).show();
                        parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();

                    })
                    .setNegativeButton("No",  (click, arg) -> {
                        Snackbar.make(result, "Covid Information Not Deleted", Snackbar.LENGTH_LONG).show();})
                    .create().show();
        });


        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);

        inflater.inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.home).setVisible(true);
        menu.findItem(R.id.covid).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Intent goToMainActivity = new Intent(getActivity(), Covid19Main.class);
                startActivity(goToMainActivity);
                break;

        }
        return true;
    }

    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return covidList.size();
        }

        public Object getItem(int position) {
            return covidList.get(position);
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            CovidProperties casesInformation = (CovidProperties) getItem(position);
            View newView = inflater.inflate(R.layout.covid_list_layout, null);
            TextView tView = newView.findViewById(R.id.showInfo);
            tView.setText("Date: " + casesInformation.getDate()+ "   Country: " + casesInformation.getCountry()+"   Province:"+casesInformation.getProvince()+"   Cases: "+casesInformation.getCases());
            return newView;
        }

        public long getItemId(int position) {
            return (long) position;
        }
    }

    protected void deleteContact(Long id)
    {
        db.delete(TABLE_NAME, COL_ID + "= ?", new String[] {Long.toString(id)});
    }

    private class MyOpener extends SQLiteOpenHelper {

        public MyOpener(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_COUNTRY + " text,"
                    + COL_DATE + " text,"
                    + COL_LINK + " text);");  // add or remove columns
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
    private class CovidSearchQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... args) {
            covidList.clear();
            try {

                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                sb.append(reader.readLine());
                int proIndex = 0;
                int caseIndex = 0;
                ArrayList<String> proInfo = new ArrayList();

                while (proIndex>=0){
                    proIndex = sb.indexOf("Province",proIndex+1);
                    if (proIndex>=0) {
                        int index1 = sb.indexOf(":", proIndex);
                        int index2 = sb.indexOf(",", proIndex);
                        String provinceName = sb.substring(index1 + 2, index2 - 1);
                        caseIndex = sb.indexOf("Cases", proIndex);
                        index1 = sb.indexOf(":", caseIndex);
                        index2 = sb.indexOf(",", caseIndex);
                        String cases = sb.substring(index1 + 1, index2);
                        CovidProperties covidP = new CovidProperties(countryName, provinceName, cases, date);
                        covidList.add(covidP);
                        publishProgress(100*(proIndex/sb.length()));
                    }
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
            return "Done";
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            progressBar.setProgress(values[0]);
            myAdapter.notifyDataSetChanged();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
        }
    }
}
