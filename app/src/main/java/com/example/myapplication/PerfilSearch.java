package com.example.myapplication;

public class PerfilSearch {
    private String username;
    private int photo;
    //getter and setter
    public PerfilSearch(String username, int photo) {
        this.username = username;
        this.photo = photo;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPhoto(int photo) {
        this.photo = photo;
    }
    public int getPhoto() {
        return photo;
    }
}
