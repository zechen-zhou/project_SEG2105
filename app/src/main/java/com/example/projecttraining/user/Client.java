package com.example.projecttraining.user;

import android.os.Parcel;
import android.os.Parcelable;

public class Client extends Person {
    private String creditCardInfo;

    public Client(String firstName, String lastName, String email, String password, String address, String creditCardInfo) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setEmail(email);
        super.setPassword(password);
        super.setAddress(address);
        setCreditCardInfo(creditCardInfo);
    }

    protected Client (Parcel in) {
        super.setFirstName(in.readString());
        super.setLastName(in.readString());
        super.setEmail(in.readString());
        super.setPassword(in.readString());
        super.setAddress(in.readString());
        setCreditCardInfo(in.readString());
    }

    public String getCreditCardInfo() {
        return creditCardInfo;
    }

    public void setCreditCardInfo(String creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
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
        parcel.writeString(this.creditCardInfo);
    }

    public static final Parcelable.Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Client createFromParcel(Parcel source) {
            return new Client(source);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };
}
