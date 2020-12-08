package com.example.final_group_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SqlHelper extends SQLiteOpenHelper {

    public static final int DEFAULT_VERSION = 1;
    public static final String TABLE_NAME="recipes";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_HREF = "href";
    public static final String COL_INGREDIENTS = "ingredients";
    public static final String COL_THUMBNAIL = "thumbnail";
    public static final String CREATE_RECIPES_TABLE_SQL = "CREATE TABLE "+TABLE_NAME+" ( "
                                                        +COL_ID+" INTEGER PRIMARY KEY, "
                                                        +COL_TITLE+" TEXT, "
                                                        +COL_HREF+ " TEXT, "
                                                        +COL_INGREDIENTS+" TEXT, "
                                                        +COL_THUMBNAIL+" TEXT )"  ;


    public SqlHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SqlHelper(Context context){
        this(context,null,null, DEFAULT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECIPES_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL(CREATE_RECIPES_TABLE_SQL);
    }

    public long addRecipe(Recipe recipe){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE,recipe.getTitle());
        cv.put(COL_HREF,recipe.getHref());
        cv.put(COL_INGREDIENTS,recipe.getIngredients());
        cv.put(COL_THUMBNAIL,recipe.getThumbnail());
        long id = db.insert(TABLE_NAME,null,cv);
        return id;
    }

    public ArrayList<Recipe> getAllRecipes(){
        ArrayList<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COL_ID,COL_TITLE,COL_HREF,COL_INGREDIENTS,COL_THUMBNAIL};
        Cursor cursor = db.query(TABLE_NAME,columns,null,null,null,null,null);
        Log.e("Cursor column count : ", cursor.getColumnCount()+"");
        while(cursor.moveToNext()){
            int colIdIndex = cursor.getColumnIndex(COL_ID);
            int colTitleIndex = cursor.getColumnIndex(COL_TITLE);
            int colHrefIndex =  cursor.getColumnIndex(COL_HREF);
            int colIngredientsIndex = cursor.getColumnIndex(COL_INGREDIENTS);
            int colThumbNailIndex = cursor.getColumnIndex(COL_THUMBNAIL);

            long id = cursor.getLong(colIdIndex);
            String title = cursor.getString(colTitleIndex);
            String href = cursor.getString(colHrefIndex);
            String ingredients = cursor.getString(colIngredientsIndex);
            String thumbnail = cursor.getString(colThumbNailIndex);
            Recipe recipe = new Recipe(title,href,ingredients,thumbnail);
            recipe.setId(id);
            Log.e("Recipe "+recipe.getId(), recipe.toString());
            recipes.add(recipe);
        }
        return recipes;
    }

}
