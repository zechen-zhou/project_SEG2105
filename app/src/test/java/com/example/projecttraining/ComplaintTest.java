package com.example.projecttraining;

import com.google.firebase.FirebaseApp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)

public class ComplaintTest {
    FirebaseApp f;
    @Test
    public void TestingStatusComplaint() {

        String id = "123456";
        String clientUser = "John";
        String cookUser = "Mary";
        String description = "Mary chicken was under-seasoned and undercooked";
        Boolean read = false;
        String suspensionDate = " 8 Nov 22";

        Complaint complaint = new Complaint();
    }

    @Test
    public void TestingStatusComplaint1() {
        String id = "789456";
        String clientUser = "Felix";
        String cookUser = "Marcus";
        String description = "I didn't like it";
        Boolean read = true;
        String suspensionDate = "" ;

        Complaint complaint = new Complaint();
    }

}