package com.wojbeg.boredapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(
        tableName = "ideas"
)
public class Idea implements Parcelable {

    @SerializedName("activity")
    @Expose
    public String activity;
    @SerializedName("accessibility")
    @Expose
    public double accessibility;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("participants")
    @Expose
    public int participants;
    @SerializedName("price")
    @Expose
    public double price;
    @SerializedName("link")
    @Expose
    public String link;

    @SerializedName("key")
    @Expose
    @PrimaryKey
    @NonNull
    public String key;

    public Idea(String activity, double accessibility, String type, int participants, double price, String link, String key) {
        this.activity = activity;
        this.accessibility = accessibility;
        this.type = type;
        this.participants = participants;
        this.price = price;
        this.link = link;
        this.key = key;
    }

    public Idea(Parcel in){
        this.activity = in.readString();
        this.accessibility = in.readDouble();
        this.type = in.readString();
        this.participants = in.readInt();
        this.price = in.readDouble();
        this.link = in.readString();
        this.key = in.readString();
    }

    public static final Creator<Idea> CREATOR = new Creator<Idea>() {
        @Override
        public Idea createFromParcel(Parcel in) {
            return new Idea(in);
        }

        @Override
        public Idea[] newArray(int size) {
            return new Idea[size];
        }
    };

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(double accessibility) {
        this.accessibility = accessibility;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(activity);
        dest.writeDouble(accessibility);
        dest.writeString(type);
        dest.writeInt(participants);
        dest.writeDouble(price);
        dest.writeString(link);
        dest.writeString(key);

    }

    public boolean equals(Idea otherIdea) {
        if(key.equalsIgnoreCase(otherIdea.key) && activity.equalsIgnoreCase(otherIdea.activity)){
            return true;
        }
        return false;
    }
}
