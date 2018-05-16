package com.example.tiago.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    @SerializedName("quantity")
    private Double mIngredientQuantity;

    @SerializedName("ingredient")
    private String mIngredient;

    @SerializedName("measure")
    private String mIngredientMeasure;

    public Ingredient() {
    }

    public Double getmIngredientQuantity() {
        return mIngredientQuantity;
    }

    public void setIngredientQuantity(Double mIngredientQuantity) {
        this.mIngredientQuantity = mIngredientQuantity;
    }

    public String getmIngredient() {
        return mIngredient;
    }

    public void setmIngredient(String mIngredient) {
        this.mIngredient = mIngredient;
    }

    public String getmIngredientMeasure() {
        return mIngredientMeasure;
    }

    public void setmIngredientMeasure(String mIngredientMeasure) {
        this.mIngredientMeasure = mIngredientMeasure;
    }

    protected Ingredient(Parcel in) {
        mIngredientQuantity = in.readByte() == 0x00 ? null : in.readDouble();
        mIngredient = in.readString();
        mIngredientMeasure = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mIngredientQuantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(mIngredientQuantity);
        }
        dest.writeString(mIngredient);
        dest.writeString(mIngredientMeasure);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
