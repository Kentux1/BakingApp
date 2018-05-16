package com.example.tiago.bakingapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tiago.bakingapp.DetailActivity;
import com.example.tiago.bakingapp.MainActivity;
import com.example.tiago.bakingapp.R;
import com.example.tiago.bakingapp.adapters.RecipesAdapter;
import com.example.tiago.bakingapp.interfaces.RecipeRetrofitInterface;
import com.example.tiago.bakingapp.models.Recipe;
import com.example.tiago.bakingapp.utils.CustomIdlingResource;
import com.example.tiago.bakingapp.utils.NoConnectivityException;
import com.example.tiago.bakingapp.utils.RetrofitUtils;

import java.util.ArrayList;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RecipeFragment extends Fragment implements RecipesAdapter.RecipeOnClickHandler {

    private MainActivity mMainActivity;
    private RecipesAdapter mRecipesAdapter;
    private ArrayList<Recipe> mRecipes;
    CustomIdlingResource mIdlingResource;
    private Unbinder mUnbinder;
    public static final String KEY_SELECTED_RECIPE = "selected_recipe";

    @BindView(R.id.recipe_recycler_view)   RecyclerView mRecipeRecyclerView;
    @BindView(R.id.no_recipes_tv) TextView mEmptyStateTextView;
    @BindView(R.id.loading_indicator) ProgressBar mLoadingIndicator;
    @BindString(R.string.error_loading_recipe) String mErrorLoadingRecipe;
    @BindString(R.string.failed_to_load_recipes) String mRecipeLoadingFailed;
    @BindInt(R.integer.grid_column_count) int mGridColumnCount;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mMainActivity, mGridColumnCount);
        mRecipeRecyclerView.setLayoutManager(layoutManager);
        mRecipeRecyclerView.setHasFixedSize(true);

        mRecipesAdapter = new RecipesAdapter( this);
        mRecipeRecyclerView.setAdapter(mRecipesAdapter);

        mRecipesAdapter.setRecipe(mRecipes);
        mRecipesAdapter.notifyDataSetChanged();

        mIdlingResource = (CustomIdlingResource) mMainActivity.getIdlingResource();
        mIdlingResource.setIdleSate(false);

        loadRecipes();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void loadRecipes() {
        try {
            mEmptyStateTextView.setVisibility(View.GONE);

            RecipeRetrofitInterface recipeInterface = RetrofitUtils.getClient(mMainActivity)
                    .create(RecipeRetrofitInterface.class);

            final Call<ArrayList<Recipe>> recipeArrayListCall = recipeInterface.getRecipe();

            recipeArrayListCall.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    int responseCode = response.code();

                    if (response.isSuccessful()) {
                        postDataLoad(true, "");

                        mRecipes = response.body();
                        MainActivity.mRecipes = mRecipes;
                        mRecipesAdapter.setRecipe(mRecipes);
                        mRecipesAdapter.notifyDataSetChanged();
                    } else {
                        mEmptyStateTextView.setText("No internet connection");
                        postDataLoad(false, mRecipeLoadingFailed);
                        Timber.e(mErrorLoadingRecipe, responseCode);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    postDataLoad(false, mRecipeLoadingFailed);
                    Timber.e(t.getMessage());
                }
            });
        } catch (NoConnectivityException e) {
            postDataLoad(false, mErrorLoadingRecipe);
            Timber.e(e.getMessage());
        }
    }

    @Override
    public void onClick(Recipe recipe) {
        Bundle recipeBundle = new Bundle();
        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
        selectedRecipe.add(recipe);
        recipeBundle.putParcelableArrayList(KEY_SELECTED_RECIPE, selectedRecipe);

        Intent intent = new Intent(mMainActivity, DetailActivity.class);
        intent.putExtras(recipeBundle);
        startActivity(intent);
    }

    public void postDataLoad(boolean isLoadSuccessful, String message) {
        if (mLoadingIndicator.getVisibility() == View.VISIBLE) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }

        if (isLoadSuccessful) {
            mEmptyStateTextView.setVisibility(View.GONE);
        } else {
            mEmptyStateTextView.setText(message);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }

        if (mIdlingResource != null) {
            mIdlingResource.setIdleSate(isLoadSuccessful);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
