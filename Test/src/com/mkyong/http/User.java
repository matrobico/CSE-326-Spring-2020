package com.mkyong.http;

public class User {
    public String name = "";
    public String publicKey = "";

    public String getName() {
        return name;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public User(String name, String publicKey){
        this.name = name;
        this.publicKey = publicKey;
    }
    public User(String name){
        this.name = name;
    }

}
