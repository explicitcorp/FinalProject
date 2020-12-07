package com.example.final_group_project;

import java.io.Serializable;

public class Recipe implements Serializable {

//    "title":"Vegetable-Pasta Oven Omelet",
//            "href":"http:\/\/find.myrecipes.com\/recipes\/recipefinder.dyn?action=displayRecipe&recipe_id=520763",
//            "ingredients":"tomato, onions, red pepper, garlic, olive oil, zucchini, cream cheese, vermicelli, eggs, parmesan cheese, milk, italian seasoning, salt, black pepper",
//            "thumbnail":"http:\/\/img.recipepuppy.com\/560556.jpg"
    private String title;
    private String  href;
    private String ingredients;
    private String thumbnail;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Recipe(String title, String href, String ingredients, String thumbnail) {
        this.title = title;
        this.href = href;
        this.ingredients = ingredients;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
