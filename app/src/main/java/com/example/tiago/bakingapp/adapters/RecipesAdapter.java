package com.example.tiago.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tiago.bakingapp.R;
import com.example.tiago.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private RecipeOnClickHandler mOnClickHandler;
    private Context mContext;
    private ArrayList<Recipe> mRecipes;

    public interface RecipeOnClickHandler {

        void onClick(Recipe recipe);
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.recipe_list_item, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        if (position < getItemCount()) {
            Recipe recipe = mRecipes.get(position);
            String recipeName = recipe.getmRecipeName();
            int servings = recipe.getmRecipeServings();
            String recipeImage = recipe.getmRecipeImage();
            String numOfServings = mContext.getString(R.string.servings) + " " + String.valueOf(servings);

            if (!recipeName.isEmpty()) {
                holder.recipeName.setText(recipeName);
            }

            if (servings != 0) {
                holder.recipeServings.setText(numOfServings);
            } else {
                holder.recipeServings.setText(mContext.getString(R.string.servings_error));
            }

            if (!recipeImage.isEmpty()) {
                Picasso.get()
                        .load(recipeImage)
                        .placeholder(getImageId(recipeName))
                        .error(getImageId(recipeName))
                        .into(holder.recipeIcon);
            } else {
                holder.recipeIcon.setImageResource(getImageId(recipeName));
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mRecipes == null) ? 0 : mRecipes.size();
    }

    public RecipesAdapter(RecipeOnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;
    }


    public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_icon)
        ImageView recipeIcon;
        @BindView(R.id.recipe_name_tv)
        TextView recipeName;
        @BindView(R.id.recipe_servings_tv)
        TextView recipeServings;

        public RecipesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Recipe recipe = mRecipes.get(position);
            mOnClickHandler.onClick(recipe);
        }
    }

    public void setRecipe(ArrayList<Recipe> recipes) {
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }

    public int getImageId(String recipeName) {
        int imageResId;

        if (recipeName.contains("Pie")) {
            imageResId = R.drawable.pie;
        } else if (recipeName.contains("Brownie")) {
            imageResId = R.drawable.brownie;
        } else if (recipeName.contains("Cake")) {
            imageResId = R.drawable.yellowcake;
        } else if (recipeName.contains("Cheesecake")) {
            imageResId = R.drawable.cheesecake;
        } else {
            imageResId = R.drawable.norecipe;
        }

        return imageResId;
    }

    public static boolean isEmptyString(String stringToCheck) {
        return (stringToCheck == null || stringToCheck.trim().length() == 0);
    }

    public static boolean isNumZero(int numToCheck) {
        return (numToCheck == 0);
    }
}
