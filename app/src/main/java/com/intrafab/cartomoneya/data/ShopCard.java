package com.intrafab.cartomoneya.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Date;

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

    private int id;
    private int owner;
    private int shopBrand;
    private String barcode;
    private String name;
    private String frontImageFile;
    private String backImageFile;
    private Date createdAt;
    private Date updatedAt;

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getShopBrand() {
        return shopBrand;
    }

    public void setShopBrand(int shopBrand) {
        this.shopBrand = shopBrand;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrontImageFile() {
        return frontImageFile;
    }

    public void setFrontImageFile(String frontImageFile) {
        this.frontImageFile = frontImageFile;
    }

    public String getBackImageFile() {
        return backImageFile;
    }

    public void setBackImageFile(String backImageFile) {
        this.backImageFile = backImageFile;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ShopCard() {
    }

    public ShopCard(Parcel source) {
        id = source.readInt();
        owner = source.readInt();
        shopBrand = source.readInt();
        barcode = source.readString();
        name = source.readString();
        frontImageFile = source.readString();
        backImageFile = source.readString();
        createdAt = new Date(source.readLong());
        updatedAt = new Date(source.readLong());
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
        dest.writeInt(id);
        dest.writeInt(owner);
        dest.writeInt(shopBrand);
        dest.writeString(barcode);
        dest.writeString(name);
        dest.writeString(frontImageFile);
        dest.writeString(backImageFile);
        dest.writeLong(createdAt.getTime());
        dest.writeLong(updatedAt.getTime());
    }
}
