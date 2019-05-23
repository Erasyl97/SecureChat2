package com.example.erabe_000.securechat2;

public class Doctor {
    public String username;
    public String userSurname;
    public String tel;
    public String email;
    public String userid;
    public String fullname;
    public String specialization;
    public String description;
    public String publicRSAKey;

    public Doctor(){}

    public Doctor(String username, String userSurname, String tel, String email, String userid, String specialization, String description, String publicRSAKey) {
        this.username = username;
        this.userSurname = userSurname;
        this.tel = tel;
        this.email = email;
        this.userid = userid;
        this.specialization = specialization;
        this.description = description;
        this.publicRSAKey = publicRSAKey;
    }
    String getUsername () {
        return username;
    }
    String getUserSurname () {
        return userSurname;
    }
    String getUserFullname () {
        fullname = username + " " + userSurname;
        return fullname;
    }
    String getTel () {
        return tel;
    }
    String getUserid () {
        return userid;
    }
    String getSpecialization() {return specialization;}
    String getDescription() {return description;}
    String getEmail() { return email;}
    String getPublicRSAKey() {
        return publicRSAKey;
    }

}
