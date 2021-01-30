package com.example.smartstethoscope.Model;

public class UserMySql {
    private String ID, name, password;
    private int phone;
    public UserMySql(){

    }

    public UserMySql(String ID, String name, int phone, String password){
        this.ID = ID;
        this.name = name;
        this.phone = phone;
        this.password = password;

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
