package com.example.listofitems;

public class DetailsStructure {

    String urgent;
    String request;
    String altName;
    String altPh;
    String pickup;
    String drop;
    String deliveryTime;
    String amount;
    String userid;

    public  String getUserid(){return userid;}
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDrop() {
        return drop;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getUrgent() {
        return urgent;
    }

    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }

    public String getAltPh() {
        return altPh;
    }

    public void setAltPh(String altPh) {
        this.altPh = altPh;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public DetailsStructure(String userid,String urgent, String deliveryTime, String request, String altName, String altPh, String pickup, String drop, String amount) {
        this.urgent = urgent;
        this.deliveryTime = deliveryTime;
        this.request = request;
        this.altName = altName;
        this.altPh = altPh;
        this.pickup = pickup;
        this.drop = drop;
        this.amount = amount;
        this.userid=userid;
    }
}
