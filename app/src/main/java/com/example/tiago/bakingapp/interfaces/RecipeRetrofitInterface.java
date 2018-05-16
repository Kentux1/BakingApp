package com.example.tiago.bakingapp.interfaces;

import com.example.tiago.bakingapp.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeRetrofitInterface {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
