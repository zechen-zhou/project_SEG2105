package com.example.projecttraining.user;

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

    public String getCreditCardInfo() {
        return creditCardInfo;
    }

    public void setCreditCardInfo(String creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
    }
}
