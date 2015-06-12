package com.intrafab.cartomoneya.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.intrafab.cartomoneya.http.RestApiConfig;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Vasily Laushkin on 24/05/15.
 */
public class BusinessCard implements Parcelable {
    private int id;
    private String name;
    private int belongsToUser;
    private Date createdAt;
    private Date updatedAt;
    private String notes;

    private int frontImageFile;
    private int backImageFile;

    private int personage;

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

    public int getBelongsToUser() {
        return belongsToUser;
    }

    public void setBelongsToUser(int belongsToUser) {
        this.belongsToUser = belongsToUser;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public int getPersonage() {
        return personage;
    }

    public void setPersonage(int personage) {
        this.personage = personage;
    }

    public String getFrontImagePath() {
        return RestApiConfig.BASE_HOST_URL + "/fileEntity/getFile?id=" + frontImageFile;
    }

    public String getBackImagePath() {
        return RestApiConfig.BASE_HOST_URL + "/fileEntity/getFile?id=" + backImageFile;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.belongsToUser);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
        dest.writeString(this.notes);
        dest.writeInt(this.frontImageFile);
        dest.writeInt(this.backImageFile);
        dest.writeInt(this.personage == 0 ? -1: this.personage);
    }

    public BusinessCard() {
    }

    private BusinessCard(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.belongsToUser = in.readInt();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        this.notes = in.readString();
        this.frontImageFile = in.readInt();
        this.backImageFile = in.readInt();
        this.personage = in.readInt();
    }

    public BusinessCard(JSONObject object) {
        if (object == null)
            return;
    }

    public static final Parcelable.Creator<BusinessCard> CREATOR = new Parcelable.Creator<BusinessCard>() {
        public BusinessCard createFromParcel(Parcel source) {
            return new BusinessCard(source);
        }

        public BusinessCard[] newArray(int size) {
            return new BusinessCard[size];
        }
    };
}
