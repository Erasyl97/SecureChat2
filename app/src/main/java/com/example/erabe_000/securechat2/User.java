package com.example.erabe_000.securechat2;

import java.security.PublicKey;

public class User {
    public String username;
    public String userSurname;
    public String tel;
    public String email;
    public String userid;
    public String fullname;
    public String publicRSAKey;

    public User(){}

    public User(String username, String userSurname, String tel, String email, String userid, String publicRSAKey) {
        this.username = username;
        this.userSurname = userSurname;
        this.tel = tel;
        this.email = email;
        this.userid = userid;
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
    String getEmail() { return email;}
    String getPublicRSAKey() {
        return publicRSAKey;
    }
}



