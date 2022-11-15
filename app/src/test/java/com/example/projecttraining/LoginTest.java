package com.example.projecttraining;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginTest {

    Login login;
    private String validEmail1 = "admin@mealer.com";
    private String validEmail2 = "admin@mail.com";
    private String invalidEmail1 = "Admin";
    private String invalidEmail2 = "123456";

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testIsValidEmail() {

        // The email is valid
        assertTrue(login.isValidEmail(validEmail1));
        assertTrue(login.isValidEmail(validEmail2));

        // The email is invalid
        assertFalse(login.isValidEmail(invalidEmail1));
        assertFalse(login.isValidEmail(invalidEmail2));
    }
}