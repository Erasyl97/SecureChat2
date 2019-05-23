package com.example.erabe_000.securechat2;

public class Note {
    private int id;
    private String about;
    private String description;
    private String time;
    private String picture;

    public Note(String about, String description, String time, String picture, int id) {
        this.about = about;
        this.description = description;
        this.time = time;
        this.picture = picture;
        this.id = id;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getAbout() {return about;}
    public void setAbout(String about) {this.about = about;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}

    public String getPicture() {return picture;}
    public void setPicture(String picture) {this.picture = picture;}
}
