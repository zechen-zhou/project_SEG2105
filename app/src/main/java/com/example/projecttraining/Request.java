package com.example.projecttraining;

public class Request {
    String key;
    String clientId;
    Meal meal;
    Request_type request_type;

    public Request(String key, String clientId, Meal meal, Request_type request_type ){
        this.key = key;
        this.clientId = clientId;
        this.meal = meal;
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

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(String mealID) { this.meal = meal; }

    public Request_type getRequest_type() {
        return request_type;
    }

    public void setRequest_type(Request_type request_type) {
        this.request_type = request_type;
    }
}
