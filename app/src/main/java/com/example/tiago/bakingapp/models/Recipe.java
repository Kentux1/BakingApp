package com.example.tiago.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    @SerializedName("id")
    private Integer mRecipeId;

    @SerializedName("name")
    private String mRecipeName;

    @SerializedName("ingredients")
    private List<Ingredient> mRecipeIngredients = null;

    @SerializedName("servings")
    private Integer mRecipeServings;

    @SerializedName("image")
    private String mRecipeImage;

    @SerializedName("steps")
    private List<Step> mRecipeStep = null;

    public Recipe() {
    }

    public Integer getmRecipeId() {
        return mRecipeId;
    }

    public void setmRecipeId(Integer mRecipeId) {
        this.mRecipeId = mRecipeId;
    }

    public String getmRecipeName() {
        return mRecipeName;
    }

    public void setmRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    public List<Ingredient> getmRecipeIngredients() {
        return mRecipeIngredients;
    }

    public void setmRecipeIngredients(List<Ingredient> mRecipeIngredients) {
        this.mRecipeIngredients = mRecipeIngredients;
    }

    public Integer getmRecipeServings() {
        return mRecipeServings;
    }

    public void setmRecipeServings(Integer mRecipeServings) {
        this.mRecipeServings = mRecipeServings;
    }

    public String getmRecipeImage() {
        return mRecipeImage;
    }

    public void setmRecipeImage(String mRecipeImage) {
        this.mRecipeImage = mRecipeImage;
    }

    public List<Step> getmRecipeStep() {
        return mRecipeStep;
    }

    public void setmRecipeStep(List<Step> mRecipeStep) {
        this.mRecipeStep = mRecipeStep;
    }

    protected Recipe(Parcel in) {
        mRecipeId = in.readByte() == 0x00 ? null : in.readInt();
        mRecipeName = in.readString();
        if (in.readByte() == 0x01) {
            mRecipeIngredients = new ArrayList<Ingredient>();
            in.readList(mRecipeIngredients, Ingredient.class.getClassLoader());
        } else {
            mRecipeIngredients = null;
        }
        mRecipeServings = in.readByte() == 0x00 ? null : in.readInt();
        mRecipeImage = in.readString();
        if (in.readByte() == 0x01) {
            mRecipeStep = new ArrayList<Step>();
            in.readList(mRecipeStep, Step.class.getClassLoader());
        } else {
            mRecipeStep = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mRecipeId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mRecipeId);
        }
        dest.writeString(mRecipeName);
        if (mRecipeIngredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mRecipeIngredients);
        }
        if (mRecipeServings == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mRecipeServings);
        }
        dest.writeString(mRecipeImage);
        if (mRecipeStep == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mRecipeStep);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
