package com.example.vicharan.entities;

public class User {


    private String Name;
    private String Email;
    private String Phone;


    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public User(String Name, String Email, String Phone) {
        this.Name = Name;
        this.Email = Email;
        this.Phone = Phone;
    }

    public User() {
    }


    public String toString() {
        return "Name: " + Name + "Email: " + Email + "Phone: " + Phone;
    }
}
