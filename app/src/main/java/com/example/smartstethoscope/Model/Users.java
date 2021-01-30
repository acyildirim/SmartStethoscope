package com.example.smartstethoscope.Model;

public class Users {
    private String ID, name, phone, password;

    public Users(){

    }

    public Users(String ID, String name, String phone, String password){
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
