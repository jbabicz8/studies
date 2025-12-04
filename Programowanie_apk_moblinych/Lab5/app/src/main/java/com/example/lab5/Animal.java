package com.example.lab5;

public class Animal {
    String name;
    Integer image;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Integer getImage() { return image; }

    public void setImage(Integer image) { this.image = image; }

    public Animal(String name, Integer image) {
        this.name = name;
        this.image = image;
    }
}
