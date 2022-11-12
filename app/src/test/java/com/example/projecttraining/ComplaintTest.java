package com.example.projecttraining;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ComplaintTest {

    private String defaultId = "123";
    private String defaultClientUser = "user123@mealer.com";
    private String defaultCookUser = "cook123@mealer.com";
    private String defaultDescription = "Got the order wrong";
    private boolean defaultReadStatus = true;

    private String tempId = "456";
    private String tempClientUser = "user456@mealer.com";
    private String tempCookUser = "cook456@mealer.com";
    private String tempDescription = "";
    private boolean tempReadStatus = false;

    private List<Complaint> defaultComplaints = new ArrayList<>();

    Complaint complaint1;
    Complaint complaint2;

    @Before
    public void setUp() {
        // Create a complaint with read status is true
        complaint1 = new Complaint(defaultId, defaultClientUser, defaultCookUser, defaultDescription, defaultReadStatus);

        // Create a complaint with read status is false
        complaint2 = new Complaint(defaultId, defaultClientUser, defaultCookUser, defaultDescription, tempReadStatus);
    }

    @After
    public void tearDown() {
        complaint1 = null;
        complaint2 = null;
        defaultComplaints = null;
    }

    @Test
    public void testAddComplaint() {
        complaint1.addComplaint(defaultComplaints, complaint1.getReadStatus(), complaint1);

        // If read status is true, the complaint will not be added to the defaultComplaints list
        assertTrue(defaultComplaints.isEmpty());

        complaint2.addComplaint(defaultComplaints, complaint2.getReadStatus(), complaint2);

        // If read status is false, the complaint will be added to the defaultComplaints list
        assertFalse(defaultComplaints.isEmpty());
    }

    @Test
    public void testGetId_True() {
        assertTrue(defaultId.equals(complaint1.getId()));
    }

    @Test
    public void testGetId_False() {
        assertFalse(tempId.equals(complaint1.getId()));
    }

    @Test
    public void testGetClientUser_True() {
        assertTrue(defaultClientUser.equals(complaint1.getClientUser()));
    }

    @Test
    public void testGetClientUser_False() {
        assertFalse(tempClientUser.equals(complaint1.getClientUser()));
    }

    @Test
    public void testGetCookUser_True() {
        assertTrue(defaultCookUser.equals(complaint1.getCookUser()));
    }

    @Test
    public void testGetCookUser_False() {
        assertFalse(tempCookUser.equals(complaint1.getCookUser()));
    }

    @Test
    public void testGetDescription_True() {
        assertTrue(defaultDescription.equals(complaint1.getDescription()));
    }

    @Test
    public void testGetDescription_False() {
        assertFalse(tempDescription.equals(complaint1.getDescription()));
    }

    @Test
    public void testGetReadStatus_True() {
        assertEquals(defaultReadStatus, complaint1.getReadStatus());
    }

    @Test
    public void testGetReadStatus_False() {
        assertNotEquals(tempReadStatus, complaint1.getReadStatus());
    }
}