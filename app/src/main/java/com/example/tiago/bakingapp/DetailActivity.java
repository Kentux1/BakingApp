package com.example.tiago.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.tiago.bakingapp.fragments.RecipeDetailFragment;
import com.example.tiago.bakingapp.fragments.RecipeStepDetailFragment;
import com.example.tiago.bakingapp.interfaces.OnListItemClickListener;
import com.example.tiago.bakingapp.models.Recipe;

import java.util.ArrayList;

import butterknife.BindBool;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements OnListItemClickListener {

    public static ArrayList<Recipe> mRecipe;
    private Bundle mRecipeBundle;
    private FragmentManager mFragmentManager;

    @BindBool(R.bool.two_pane) boolean mIsTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            mRecipeBundle = getIntent().getExtras();
            mRecipe = mRecipeBundle.getParcelableArrayList("selected_recipe");
        }

        if ((mRecipe == null) || (mRecipe.get(0).getmRecipeName().isEmpty())) return;
        setTitle(mRecipe.get(0).getmRecipeName());

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            if (mIsTwoPane) {
                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                mRecipeBundle.putInt("selected_step", 0);
                recipeDetailFragment.setArguments(mRecipeBundle);
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.container_recipe_detail, recipeDetailFragment)
                        .addToBackStack("STACK_RECIPE_DETAIL")
                        .commit();

                RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                mRecipeBundle.putInt("selected_step", 0);
                recipeStepDetailFragment.setArguments(mRecipeBundle);
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.container_recipe_step_detail, recipeStepDetailFragment)
                        .addToBackStack("STACK_RECIPE_DETAIL")
                        .commit();
            } else {

                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                recipeDetailFragment.setArguments(mRecipeBundle);
                mFragmentManager.beginTransaction()
                        .replace(R.id.container_recipe_detail, recipeDetailFragment)
                        .addToBackStack("STACK_RECIPE_DETAIL")
                        .commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(int recipeStepId) {
        int stepsCount = mRecipe.get(0).getmRecipeStep().size();

        if (mIsTwoPane) {
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelableArrayList("selected_recipe", mRecipe);
            stepBundle.putInt("selected_step", recipeStepId);
            stepBundle.putInt("step_count", stepsCount);

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(stepBundle);
            mFragmentManager.beginTransaction()
                    .replace(R.id.container_recipe_detail, recipeDetailFragment)
                    .commit();

            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeDetailFragment.setArguments(stepBundle);
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.container_recipe_step_detail, recipeStepDetailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra("selected_step", recipeStepId);
            intent.putExtra("step_count", stepsCount);
            startActivity(intent);
        }
    }
}
