package com.intrafab.cartomoneya.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.intrafab.cartomoneya.http.RestApiConfig;

import java.util.Date;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 06/06/15.
 */

/*
{
    "frontImageFile": 51,
    "mainImageFile": 52,
    "shopBrand": 1,
    "name": "Только сегодня скидка на женские вещи 25%",
    "text": "Ждем Вас у нас! Только сегодня скидка на женские вещи 25%",
    "id": 1,
    "createdAt": "2015-05-19T17:09:07.031Z",
    "updatedAt": "2015-05-19T19:58:17.668Z"
  },
 */

public class ShopOffer implements Parcelable {
    private int id;
    private Date createdAt;
    private Date updatedAt;
    private String name;
    private String text;
    private int shopBrand;

    private int frontImageFile;
    private int mainImageFile;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getShopBrand() {
        return shopBrand;
    }

    public void setShopBrand(int shopBrand) {
        this.shopBrand = shopBrand;
    }

    public int getFrontImageFile() {
        return frontImageFile;
    }

    public void setFrontImageFile(int frontImageFile) {
        this.frontImageFile = frontImageFile;
    }

    public int getMainImageFile() {
        return mainImageFile;
    }

    public void setMainImageFile(int mainImageFile) {
        this.mainImageFile = mainImageFile;
    }

    public String getFrontImagePath() {
        return RestApiConfig.BASE_HOST_URL + "/fileEntity/getFile?id=" + frontImageFile;
    }

    public String getMainImagePath() {
        return RestApiConfig.BASE_HOST_URL + "/fileEntity/getFile?id=" + mainImageFile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
        dest.writeString(this.name);
        dest.writeString(this.text);
        dest.writeInt(this.shopBrand);
        dest.writeInt(this.frontImageFile);
        dest.writeInt(this.mainImageFile);
    }

    public ShopOffer() {
    }

    private ShopOffer(Parcel in) {
        this.id = in.readInt();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        this.name = in.readString();
        this.text = in.readString();
        this.shopBrand = in.readInt();
        this.frontImageFile = in.readInt();
        this.mainImageFile = in.readInt();
    }

    public static final Parcelable.Creator<ShopOffer> CREATOR = new Parcelable.Creator<ShopOffer>() {
        public ShopOffer createFromParcel(Parcel source) {
            return new ShopOffer(source);
        }

        public ShopOffer[] newArray(int size) {
            return new ShopOffer[size];
        }
    };
}
