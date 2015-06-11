package com.intrafab.cartomoneya.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by mono on 06/06/15.
 */

/*
{
    "belongsToUser": 1,
    "name": "Купить картошки",
    "done": false,
    "group": "Продуктовый за углом",
    "id": 1,
    "createdAt": "2015-05-19T15:09:20.158Z",
    "updatedAt": "2015-05-19T15:09:20.158Z"
  }
 */

public class ShoppingListItem implements Parcelable {
    private int belongsToUser;
    private String name;
    private boolean done;
    private String group;
    private int id;
    private Date createdAt;
    private Date updatedAt;


    public int getBelongsToUser() {
        return belongsToUser;
    }

    public void setBelongsToUser(int belongsToUser) {
        this.belongsToUser = belongsToUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.belongsToUser);
        dest.writeString(this.name);
        dest.writeByte(done ? (byte) 1 : (byte) 0);
        dest.writeString(this.group);
        dest.writeInt(this.id);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
    }

    public ShoppingListItem() {
    }

    protected ShoppingListItem(Parcel in) {
        this.belongsToUser = in.readInt();
        this.name = in.readString();
        this.done = in.readByte() != 0;
        this.group = in.readString();
        this.id = in.readInt();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
    }

    public static final Parcelable.Creator<ShoppingListItem> CREATOR = new Parcelable.Creator<ShoppingListItem>() {
        public ShoppingListItem createFromParcel(Parcel source) {
            return new ShoppingListItem(source);
        }

        public ShoppingListItem[] newArray(int size) {
            return new ShoppingListItem[size];
        }
    };
}
