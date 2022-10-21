package com.example.projecttraining.user;

import android.os.Parcel;
import android.os.Parcelable;

public class Administrator extends Person {

    public Administrator(String firstName, String lastName, String email, String password, String address) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setEmail(email);
        super.setPassword(password);
        super.setAddress(address);
    }

    protected Administrator (Parcel in) {
        super.setFirstName(in.readString());
        super.setLastName(in.readString());
        super.setEmail(in.readString());
        super.setPassword(in.readString());
        super.setAddress(in.readString());
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
    }

    public static final Parcelable.Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Administrator createFromParcel(Parcel source) {
            return new Administrator(source);
        }

        @Override
        public Administrator[] newArray(int size) {
            return new Administrator[size];
        }
    };
}
