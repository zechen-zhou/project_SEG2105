package com.example.projecttraining;

/**
 * Created by Miguel Garzón on 2017-05-07.
 */

public class Complaint {
    private String _id;
    private String _complaintname;
    private String _cookUser;
    private String _description;


    public Complaint() {
    }
    public Complaint(String id, String complaintname, String cookUser, String description) {
        _id = id;
        _complaintname = complaintname;
        _cookUser = cookUser;
        _description = description;
    }
    public Complaint(String complaintname, String cookUser, String description) {
        _complaintname = complaintname;
        _cookUser = cookUser;
        _description = description;
    }

    public void setId(String id) {
        _id = id;
    }
    public String getId() {
        return _id;
    }
    public void setComplaintName(String complaintname) {
        _complaintname = complaintname;
    }
    public String getComplaintName() {
        return _complaintname;
    }
    public void setCookUser(String cookUser) {
        _cookUser = cookUser;
    }
    public String getCookUser() {
        return _cookUser;
    }
    public void setDescription(String description){
        _description = description;
    }
    public String getDescription() {
        return _description;
    }


}

