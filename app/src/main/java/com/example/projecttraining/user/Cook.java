package com.example.projecttraining.user;

public class Cook extends Person {
    private String cookDescription;

    public Cook(String firstName, String lastName, String email, String password, String address, String cookDescription) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setEmail(email);
        super.setPassword(password);
        super.setAddress(address);
        setCookDescription(cookDescription);
    }

    public String getCookDescription() {
        return cookDescription;
    }

    public void setCookDescription(String cookDescription) {
        this.cookDescription = cookDescription;
    }
}
