package com.example.projecttraining;



import android.app.Activity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ComplaintListTest {

    @Test
    public void testComplaintList(){
        Activity context = new Activity();
        List<Complaint> complaints = new ArrayList<>();
        ComplaintList complaintList = new ComplaintList(context, complaints);

    }

}