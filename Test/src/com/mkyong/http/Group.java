package com.mkyong.http;

public class Group {
    public String name = "";
    public int id = 0;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group(String name, int id){
        this.name = name;
        this.id = id;
    }

    public Group (){}


}
