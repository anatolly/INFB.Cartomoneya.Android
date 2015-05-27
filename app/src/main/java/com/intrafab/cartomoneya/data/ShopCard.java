package com.intrafab.cartomoneya.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.intrafab.cartomoneya.http.RestApiConfig;

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
    private int belongsToUser;
    private int shopBrand;
    private String barcode;
    private String barcodeFormat;
    private String name;
    private int frontImageFile;
    private int backImageFile;
    private Date createdAt;
    private Date updatedAt;
    private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

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

    public int getBelongsToUser() {
        return belongsToUser;
    }

    public void setBelongsToUser(int belongsToUser) {
        this.belongsToUser = belongsToUser;
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

    public String getBarcodeFormat() {
        return barcodeFormat;
    }

    public void setBarcodeFormat(String barcodeFormat) {
        this.barcodeFormat = barcodeFormat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrontImageFile() {
        return frontImageFile;
    }

    public void setFrontImageFile(int frontImageFile) {
        this.frontImageFile = frontImageFile;
    }

    public int getBackImageFile() {
        return backImageFile;
    }

    public void setBackImageFile(int backImageFile) {
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
        belongsToUser = source.readInt();
        shopBrand = source.readInt();
        barcode = source.readString();
        barcodeFormat = source.readString();
        name = source.readString();
        frontImageFile = source.readInt();
        backImageFile = source.readInt();
        long _createdAt = source.readLong();
        createdAt = _createdAt == -1 ? null : new Date(_createdAt);
        long _updatedAt = source.readLong();
        updatedAt = _updatedAt == -1 ? null : new Date(_updatedAt);
        notes = source.readString();
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
        dest.writeInt(belongsToUser);
        dest.writeInt(shopBrand);
        dest.writeString(barcode);
        dest.writeString(barcodeFormat);
        dest.writeString(name);
        dest.writeInt(frontImageFile);
        dest.writeInt(backImageFile);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
        dest.writeString(notes);
    }

    public String getFrontImagePath() {
        return RestApiConfig.BASE_HOST_URL + "/fileEntity/getFile?id=" + frontImageFile;
    }

    public String getBackImagePath() {
        return RestApiConfig.BASE_HOST_URL + "/fileEntity/getFile?id=" + backImageFile;
    }
}
