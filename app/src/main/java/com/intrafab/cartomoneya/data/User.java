package com.intrafab.cartomoneya.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private String name;
    private String login;
    private Date createdAt;
    private Date updatedAt;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User() {
    }

    public User(Parcel source) {
        name = source.readString();
        login = source.readString();
        createdAt = new Date(source.readLong());
        updatedAt = new Date(source.readLong());
    }

    public User(JSONObject object) {
        if (object == null)
            return;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(login);
        dest.writeLong(createdAt.getTime());
        dest.writeLong(updatedAt.getTime());
    }
}
