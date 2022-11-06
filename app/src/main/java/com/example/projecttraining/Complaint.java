package com.example.projecttraining;

public class Complaint {
    private String _id;
    private String _clientUser;
    private String _cookUser;
    private String _description;
    private boolean _read;
    private String _suspensionDate;

    public Complaint() {
    }

    public Complaint(String id, String clientUser, String cookUser, String description) {
        _id = id;
        _clientUser = clientUser;
        _cookUser = cookUser;
        _description = description;
        _read = false;
        _suspensionDate = "N/A";
    }

    public Complaint(String id, String clientUser, String cookUser, String description, boolean read) {
        _id = id;
        _clientUser = clientUser;
        _cookUser = cookUser;
        _description = description;
        _read = read;
        _suspensionDate = "N/A";
    }

    public Complaint(String cookUser, String clientUser, String description) {
        _cookUser = cookUser;
        _clientUser = clientUser;
        _description = description;
        _read = false;
        _suspensionDate = "N/A";
    }

    public void setId(String id) {
        _id = id;
    }
    public String getId() {
        return _id;
    }
    public void setClientUser(String clientUser) {_clientUser = clientUser;}
    public String getClientUser() {
        return _clientUser;
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

    public boolean isRead() {return _read;}
    public void setReadStatus(boolean status) {_read = status;}
    public String getSuspensionDate() {return _suspensionDate;}
    public void setSuspensionDate(String date) {_suspensionDate = date;}

}

