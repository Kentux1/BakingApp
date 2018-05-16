package com.example.tiago.bakingapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiago.bakingapp.R;
import com.example.tiago.bakingapp.models.Step;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepViewHolder> {

    private StepOnClickHandler mOnClickHandler;
    private Context mContext;
    private List<Step> mSteps;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    public interface StepOnClickHandler {
        void onClick(Step step);
    }

    public RecipeStepsAdapter(Context context, StepOnClickHandler onClickHandler) {
        mContext = context;
        mOnClickHandler = onClickHandler;
        mSharedPreferences = mContext.getSharedPreferences("recipe_preference", Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mSharedPreferencesEditor.apply();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.recipe_step_list_item, parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepViewHolder holder, int position) {
        if (position < getItemCount()) {
            Step step = mSteps.get(position);
            String recipeStepShortDesc = step.getmStepId() + ". " + step.getmStepShortDescription();

            if (!recipeStepShortDesc.isEmpty()) {
                holder.stepDescriptionTextView.setText(recipeStepShortDesc);
            }

            if (mSteps.get(position).getmIsSelected()) {
                holder.stepCardView.setCardBackgroundColor(holder.colorSelectedStepBackground);
            } else {
                holder.stepCardView.setCardBackgroundColor(holder.colorUnselectedStepBackground);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mSteps == null) ? 0 : mSteps.size();
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_card_view) CardView stepCardView;
        @BindView(R.id.step_description_tv) TextView stepDescriptionTextView;
        @BindColor(R.color.colorUnselectedStep) int colorUnselectedStepBackground;
        @BindColor(R.color.colorSelectedStep) int colorSelectedStepBackground;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Step step = mSteps.get(position);
            mOnClickHandler.onClick(step);
        }
    }

    public void setRecipeSteps(List<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public void setSelectedStep(int position) {
        final String SELECTED_STEP_PREFERENCE = "selected_step_preference";

        int lastPosition = mSharedPreferences.getInt(SELECTED_STEP_PREFERENCE, -1);
        if ((lastPosition > -1) && (lastPosition <getItemCount())) {
            mSteps.get(lastPosition).setmIsSelected(false);
        }

        mSharedPreferencesEditor.putInt(SELECTED_STEP_PREFERENCE, position);
        mSharedPreferencesEditor.commit();
        mSteps.get(position).setmIsSelected(true);
        notifyDataSetChanged();
    }
}
