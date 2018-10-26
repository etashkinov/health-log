package com.ewind.hl.service;

import android.net.Uri;

public class Person {
    private final String id;
    private final String name;
    private final Uri photo;

    public Person(String id, String name, Uri photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Uri getPhoto() {
        return photo;
    }
}
