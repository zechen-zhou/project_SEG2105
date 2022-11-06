package com.example.projecttraining;

import static org.junit.Assert.*;

import org.junit.Test;

public class Inbox_AdminTest {

    @Test
    public void testInbox(){
        Inbox_Admin inbox_admin = new Inbox_Admin();
    }

    @Test
    public void editCookStatus() {
        String cookName = "Mary";
        int status = 1;
        Inbox_Admin inbox_admin = new Inbox_Admin();
    }

    @Test
    public void editComplaintRead() {
        String cookName = "Joe";
        boolean status = false;
        Inbox_Admin inbox_admin = new Inbox_Admin();
    }

    @Test
    public void showDecisionDialog() {
        String complaintId = "123456";
        String cookName = "Mike";
        String clientName = "Moe";
        String description = "No good";

        Inbox_Admin inbox_admin = new Inbox_Admin();
    }
}