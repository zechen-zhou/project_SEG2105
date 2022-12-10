package com.example.projecttraining;

public class Request {
    String key;
    String clientId;
    String mealID;
    String cookID;
    Request_type request_type;

    public Request() {}

    public Request(String key, String clientId, String meal, String cookID, Request_type request_type ){
        this.key = key;
        this.clientId = clientId;
        this.mealID = meal;
        this.cookID = cookID;
        this.request_type = request_type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMealID() {
        return mealID;
    }

    public void setMealID (String mealID){
        this.mealID = mealID;
    }

    public String getCookID() {
        return cookID;
    }

    public void setCookID (String cookID) {
        this.cookID = cookID;
    }

    public Request_type getRequest_type() {
        return request_type;
    }

    public void setRequest_type(Request_type request_type) {
        this.request_type = request_type;
    }
}
