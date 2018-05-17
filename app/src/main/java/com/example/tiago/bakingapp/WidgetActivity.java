package com.example.tiago.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tiago.bakingapp.models.Ingredient;
import com.example.tiago.bakingapp.models.Recipe;
import com.example.tiago.bakingapp.widget.BakingWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;

public class WidgetActivity extends AppCompatActivity {

    final Context mContext = this;
    private RadioButton mRadioButton;
    private RadioButton [] mRadioButtons;
    private RadioGroup mRadioGroupRecipeOptions;
    private Button mButton;
    private AppWidgetManager mAppWidgetManager;
    private int mAppWidgetId;
    private RadioGroup.LayoutParams mRadioGroupLayoutParams;
    public ArrayList<Recipe> mRecipes;
    private Toast mToast;
    private String [] mWidgetRecipe;
    int mRecipeId;
    private List<Ingredient> mIngredients;

    @BindString(R.string.appwidget_text) String mWidgetDefaultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_widget);
        ButterKnife.bind(this);

        if (MainActivity.mRecipes == null) {
            startActivity(new Intent(this, MainActivity.class));
            showToastMessage(mContext, mToast, "You need to start the app first to activate the widget.").show();
            finish();
        }

        mRecipes = MainActivity.mRecipes;
        mWidgetRecipe = new String[3];

        handleIntentExtras();

        chooseRecipe();

        mAppWidgetManager = AppWidgetManager.getInstance(mContext);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleWidgetRecipe();
            }
        });
    }

    public void handleWidgetRecipe() {
        int selectId = mRadioGroupRecipeOptions.getCheckedRadioButtonId();
        mRadioButton = findViewById(selectId);

        if (mRadioButton != null) {
            mWidgetRecipe[0] = mRadioButton.getTag().toString();

            mWidgetRecipe[1] = mRadioButton.getText().toString();

            int id = Integer.valueOf(mWidgetRecipe[0]) - 1;
            mIngredients = mRecipes.get(id).getmRecipeIngredients();

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

            mWidgetRecipe[2] = ingredientString.toString();

            BakingWidgetService.startActionUpdateWidget(mContext, mWidgetRecipe);
            Intent chosenRecipe = new Intent();
            chosenRecipe.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, chosenRecipe);
            finish();
        } else {
            mWidgetRecipe[0] = "0";
            mWidgetRecipe[1] = "";
            mWidgetRecipe[2] = mWidgetDefaultText;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntentExtras();
    }

    private void handleIntentExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            String[] lastRecipe = bundle.getStringArray("widget_recipe");
            mRecipeId = (lastRecipe != null) ? Integer.valueOf(lastRecipe[0]) : 0;
        }
    }

    public void chooseRecipe() {
        mRadioGroupRecipeOptions = findViewById(R.id.radiogroup_recipe_options);
        mButton = findViewById(R.id.button_choose_recipe);
        mRadioGroupLayoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRadioButtons = new RadioButton[mRecipes.size()];

        int i = 0;
        for (Recipe recipe : mRecipes) {
            mRadioButtons[i] = new RadioButton(this);
            mRadioGroupRecipeOptions.addView(mRadioButtons[i]);
            mRadioButtons[i].setText(recipe.getmRecipeName());
            mRadioButtons[i].setTag(recipe.getmRecipeId());
            mRadioGroupLayoutParams.setMargins(16, 16, 16, 16);
            mRadioButtons[i].setLayoutParams(mRadioGroupLayoutParams);
            mRadioButtons[i].setPadding(36, 0, 0, 0);

            if (mRecipeId != 0) {
                if (mRecipeId == recipe.getmRecipeId()) {
                    mRadioButtons[i].setChecked(true);
                }
            }

        }
    }

    public static Toast showToastMessage(Context context, Toast toast, String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        return toast;
    }
}
