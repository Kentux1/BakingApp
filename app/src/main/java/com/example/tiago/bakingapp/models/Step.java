package com.example.tiago.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Step implements Parcelable {

    @SerializedName("id")
    private Integer mStepId;

    @SerializedName("description")
    private String mStepDescription;

    @SerializedName("shortDescription")
    private String mStepShortDescription;

    @SerializedName("videoURL")
    private String mVideoUrl;

    @SerializedName("thumbnailURL")
    private String mVideoThumbnailUrl;

    private boolean mIsSelected;

    public Step() {
    }

    public Integer getmStepId() {
        return mStepId;
    }

    public void setmStepId(Integer mStepId) {
        this.mStepId = mStepId;
    }

    public String getmStepDescription() {
        return mStepDescription;
    }

    public void setmStepDescription(String mStepDescription) {
        this.mStepDescription = mStepDescription;
    }

    public String getmStepShortDescription() {
        return mStepShortDescription;
    }

    public void setmStepShortDescription(String mStepShortDescription) {
        this.mStepShortDescription = mStepShortDescription;
    }

    public String getmVideoUrl() {
        return mVideoUrl;
    }

    public void setmVideoUrl(String mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
    }

    public String getmVideoThumbnailUrl() {
        return mVideoThumbnailUrl;
    }

    public void setmVideoThumbnailUrl(String mVideoThumbnailUrl) {
        this.mVideoThumbnailUrl = mVideoThumbnailUrl;
    }

    public boolean getmIsSelected() {
        return mIsSelected;
    }

    public void setmIsSelected(boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }

    protected Step(Parcel in) {
        mStepId = in.readByte() == 0x00 ? null : in.readInt();
        mStepDescription = in.readString();
        mStepShortDescription = in.readString();
        mVideoUrl = in.readString();
        mVideoThumbnailUrl = in.readString();
        mIsSelected = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mStepId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mStepId);
        }
        dest.writeString(mStepDescription);
        dest.writeString(mStepShortDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mVideoThumbnailUrl);
        dest.writeByte((byte) (mIsSelected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
