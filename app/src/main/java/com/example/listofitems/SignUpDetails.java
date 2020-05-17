package com.example.listofitems;

public class SignUpDetails {

    String userid,password,name;

    public SignUpDetails(){}

    public SignUpDetails(String userid,String password,String name)
    {
        this.userid=userid;
        this.password=password;
        this.name=name;
    }

    public String getUserid(){return userid;}
    public String getPassword(){return password;}
    public String getName(){return name;}

}
