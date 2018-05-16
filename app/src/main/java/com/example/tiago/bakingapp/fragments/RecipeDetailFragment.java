package com.example.tiago.bakingapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiago.bakingapp.DetailActivity;
import com.example.tiago.bakingapp.R;
import com.example.tiago.bakingapp.adapters.RecipeStepsAdapter;
import com.example.tiago.bakingapp.interfaces.OnListItemClickListener;
import com.example.tiago.bakingapp.models.Ingredient;
import com.example.tiago.bakingapp.models.Recipe;
import com.example.tiago.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment implements RecipeStepsAdapter.StepOnClickHandler {

    private DetailActivity mDetailActivity;
    private Unbinder mUnbinder;
    private Recipe mSelectedRecipe;
    private RecipeStepsAdapter mRecipeStepsAdapter;
    private List<Ingredient> mIngredients;
    private List<Step> mRecipeSteps;
    private OnListItemClickListener mCallback;
    public Integer mSelectedStepId;

    @BindView(R.id.steps_recycler_view) RecyclerView mRecipeStepsRecyclerView;
    @BindView(R.id.ingredients_text_view) TextView mIngredientsTextView;
    @BindBool(R.bool.two_pane) boolean mIsTwoPane;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnListItemClickListener) context;
        } catch (ClassCastException e) {
            String error = context.toString() + " must implement OnViewClickListener";
            throw new ClassCastException(error);
        }
    }

    public RecipeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDetailActivity = (DetailActivity) getActivity();
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            ArrayList<Recipe> recipes = getArguments().getParcelableArrayList("selected_recipe");
            mSelectedStepId = getArguments().getInt("selected_step");
            mSelectedRecipe = recipes.get(0);

            mIngredients = mSelectedRecipe.getmRecipeIngredients();
            mRecipeSteps = mSelectedRecipe.getmRecipeStep();

            displayRecipeIngredients();
            displayRecipeSteps();
        }
        return rootView;
    }

    @Override
    public void onClick(Step step) {
        mSelectedStepId = step.getmStepId();
        mCallback.onItemSelected(mSelectedStepId);
        mRecipeStepsAdapter.setSelectedStep(mSelectedStepId);
    }

    public void displayRecipeIngredients() {
        StringBuilder ingredientString = new StringBuilder();

        for (Ingredient ingredient : mIngredients) {
            String ingredientLowerCase = ingredient.getmIngredient();
            String ingredientFirstLetterUpperCase = ingredientLowerCase.toUpperCase().charAt(0)
                    + ingredientLowerCase.substring(1).toLowerCase();
            String ingredientStringModel = ingredientFirstLetterUpperCase
                    + " ("
                    + Double.toString(ingredient.getmIngredientQuantity())
                    + ingredient.getmIngredientMeasure().toLowerCase()
                    + ")\n";
            ingredientString.append(ingredientStringModel);
        }

        mIngredientsTextView.setText(ingredientString.toString());
    }

    public void displayRecipeSteps() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mDetailActivity);
        mRecipeStepsRecyclerView.setLayoutManager(layoutManager);

        mRecipeStepsAdapter = new RecipeStepsAdapter(mDetailActivity, this);
        mRecipeStepsRecyclerView.setAdapter(mRecipeStepsAdapter);
        mRecipeStepsAdapter.setRecipeSteps(mRecipeSteps);

        if (mSelectedStepId == null) {
            mRecipeStepsAdapter.setSelectedStep(0);
        } else {
            mRecipeStepsAdapter.setSelectedStep(mSelectedStepId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
