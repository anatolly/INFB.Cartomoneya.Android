package com.intrafab.cartomoneya.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by mikhailzubov on 07.06.15.
 */
public class Personage implements Parcelable {
    private int id;
    private String name;
    private String company;
    private String website;
    private String address;
    private String jobTitle;
    private String workPhone;
    private String cellPhone;
    private String fax;
    private String email;
    private String skype;
    private Date createdAt;
    private Date updatedAt;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyAddress() {
        return address;
    }

    public void setCompanyAddress(String address) {
        this.company = address;
    }

    public String getCompanySiteAddress() {
        return website;
    }

    public void setCompanySiteAddress(String website) {
        this.website = website;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
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
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.company);
        dest.writeString(this.jobTitle);
        dest.writeString(this.workPhone);
        dest.writeString(this.cellPhone);
        dest.writeString(this.email);
        dest.writeString(this.skype);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
    }

    public Personage() {
    }

    private Personage(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.company = in.readString();
        this.jobTitle = in.readString();
        this.workPhone = in.readString();
        this.cellPhone = in.readString();
        this.email = in.readString();
        this.skype = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
    }

    public static final Parcelable.Creator<Personage> CREATOR = new Parcelable.Creator<Personage>() {
        public Personage createFromParcel(Parcel source) {
            return new Personage(source);
        }

        public Personage[] newArray(int size) {
            return new Personage[size];
        }
    };
}