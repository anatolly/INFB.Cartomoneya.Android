package com.intrafab.cartomoneya.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class ShopCard implements Parcelable {
    public static final Creator<ShopCard> CREATOR = new Creator<ShopCard>() {
        @Override
        public ShopCard createFromParcel(Parcel source) {
            return new ShopCard(source);
        }

        @Override
        public ShopCard[] newArray(int size) {
            return new ShopCard[size];
        }
    };

    public ShopCard() {
    }

    public ShopCard(Parcel source) {
    }

    public ShopCard(JSONObject object) {
        if (object == null)
            return;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
