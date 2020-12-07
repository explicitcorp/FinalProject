package com.example.final_group_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeAdapter extends BaseAdapter {

    private ArrayList<Recipe> recipes;
    private Context context;

    public RecipeAdapter(ArrayList<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Recipe getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return recipes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.recipe_layout,null);
        TextView titleTv = convertView.findViewById(R.id.rcpTitle);
        titleTv.setText(getItem(position).getTitle());

        return convertView;
    }
}
