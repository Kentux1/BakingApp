package com.example.tiago.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CoordinatorLayout;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tiago.bakingapp.fragments.RecipeFragment;
import com.example.tiago.bakingapp.models.Recipe;
import com.example.tiago.bakingapp.utils.CustomIdlingResource;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    final Context mContext = this;
    private RecipeFragment mRecipesFragment;
    @Nullable
    private CustomIdlingResource mIdlingResource;
    public static ArrayList<Recipe> mRecipes;
    @BindView(R.id.coordinator_layout) CoordinatorLayout mCoordinatorLayout;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new CustomIdlingResource();
        }
        return mIdlingResource;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRecipesFragment = (RecipeFragment) getFragmentManager().findFragmentById(R.id.fragment_recipes);

        getIdlingResource();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_button:
                mRecipesFragment.loadRecipes();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
