package com.intrafab.cartomoneya.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class ShopBrand implements Parcelable {
    public static final Creator<ShopBrand> CREATOR = new Creator<ShopBrand>() {
        @Override
        public ShopBrand createFromParcel(Parcel source) {
            return new ShopBrand(source);
        }

        @Override
        public ShopBrand[] newArray(int size) {
            return new ShopBrand[size];
        }
    };

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShopBrand() {
    }

    public ShopBrand(Parcel source) {
        id = source.readInt();
        name = source.readString();
    }

    public ShopBrand(JSONObject object) {
        if (object == null)
            return;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
