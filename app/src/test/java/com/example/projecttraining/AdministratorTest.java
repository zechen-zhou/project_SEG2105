package com.example.projecttraining;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.projecttraining.user.Administrator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AdministratorTest {

    private String firstName = "Admin";
    private String lastName = "name";
    private String email = "admin@mealer.com";
    private String password = "123456";
    private String address = "123 street";

    Administrator user;

    @Before
    public void setUp() {
        // Create an Administrator
        user = new Administrator(firstName, lastName, email, password, address);
    }

    @After
    public void tearDown() {
        user = null;
    }

    @Test
    public void testGetFirstName_True() {
        assertTrue(firstName.equals(user.getFirstName()));
        assertNotNull(user.getFirstName());
    }

    @Test
    public void testGetFirstName_False() {
        assertFalse("user".equals(user.getFirstName()));
    }

    @Test
    public void testGetLastName_True() {
        assertTrue(lastName.equals(user.getLastName()));
        assertNotNull(user.getLastName());
    }

    @Test
    public void testGetLastName_False() {
        assertFalse("user".equals(user.getLastName()));
    }

    @Test
    public void testGetEmail_True() {
        assertTrue(email.equals(user.getEmail()));
        assertNotNull(user.getEmail());
    }

    @Test
    public void testGetEmail_False() {
        assertFalse("email".equals(user.getEmail()));
    }

    @Test
    public void testGetAddress_True() {
        assertTrue(address.equals(user.getAddress()));
        assertNotNull(user.getAddress());
    }

    @Test
    public void testGetAddress_False() {
        assertFalse("address".equals(user.getAddress()));
    }
}
