package com.example.tiago.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tiago.bakingapp.fragments.RecipeStepDetailFragment;
import com.example.tiago.bakingapp.models.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepActivity extends AppCompatActivity {

    private Bundle mStepBundle;
    private Recipe mRecipe;
    private int mStepId;
    private int mStepCount;
    private ArrayList<Recipe> mSelectedRecipe;
    private FragmentManager mFragmentManager;

    @BindView(R.id.button_previous) ImageButton mButtonPreviousStep;
    @BindView(R.id.button_next) ImageButton mButtonNextStep;
    @BindView(R.id.textview_stepnum) TextView mStepNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            mStepId = extras.getInt("selected_step");
            mStepCount = extras.getInt("step_count");
        }

        mRecipe = DetailActivity.mRecipe.get(0);
        setTitle(mRecipe.getmRecipeName());

        mFragmentManager = getSupportFragmentManager();
        displayStepNumber();

        if (savedInstanceState == null) {
            displayStepFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected_step", mStepId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mStepId = savedInstanceState.getInt("selected_step");
            displayStepNumber();
        } else {
            displayStepFragment();
        }
    }

    @OnClick({R.id.button_previous, R.id.button_next})
    public void setViewOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.button_previous:
                previousStep();
                break;
            case R.id.button_next:
                nextStep();
                break;
        }
    }

    public void previousStep() {
        if (mStepId > 0) {
            mStepId --;
            displayStepNumber();
            displayStepFragment();
        }
    }

    public void nextStep() {
        if (mStepId < (mStepCount -1)) {
            mStepId++;
            displayStepNumber();
            displayStepFragment();
        }
    }

    public void displayStepNumber() {
        String mStepNumber = "Step " + mStepId + " of " + (mStepCount - 1);
        mStepNumberTextView.setText(mStepNumber);
    }

    public void displayStepFragment() {
        mStepBundle = new Bundle();
        mSelectedRecipe = new ArrayList<>();
        mSelectedRecipe.add(mRecipe);
        mStepBundle.putParcelableArrayList("selected_recipe", mSelectedRecipe);
        mStepBundle.putInt("selected_step", mStepId);
        mStepBundle.putInt("step_count", mStepCount);

        RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();
        stepDetailFragment.setArguments(mStepBundle);

        mFragmentManager
                .beginTransaction()
                .replace(R.id.container_recipe_step_detail, stepDetailFragment)
                .commit();
    }
}
