package com.example.projecttraining.user;

import android.os.Parcel;
import android.os.Parcelable;

public class Cook extends Person {
    private String cookDescription;

    /**
     * 0 --> no suspensions applied
     * 1 --> temporarily suspended
     * 2 --> permanently suspended
     */
    private int status;

    public Cook(String firstName, String lastName, String email, String password, String address, String cookDescription) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setEmail(email);
        super.setPassword(password);
        super.setAddress(address);
        setCookDescription(cookDescription);
        status = 0;
    }

    protected Cook (Parcel in) {
        super.setFirstName(in.readString());
        super.setLastName(in.readString());
        super.setEmail(in.readString());
        super.setPassword(in.readString());
        super.setAddress(in.readString());
        setCookDescription(in.readString());
    }

    public String getCookDescription() {
        return cookDescription;
    }

    public void setCookDescription(String cookDescription) {
        this.cookDescription = cookDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.firstName);
        parcel.writeString(this.lastName);
        parcel.writeString(this.email);
        parcel.writeString(this.password);
        parcel.writeString(this.address);
        parcel.writeString(this.cookDescription);
    }

    public static final Parcelable.Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Cook createFromParcel(Parcel source) {
            return new Cook(source);
        }

        @Override
        public Cook[] newArray(int size) {
            return new Cook[size];
        }
    };

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
