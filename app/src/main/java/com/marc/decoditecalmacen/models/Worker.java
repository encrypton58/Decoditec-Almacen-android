package com.marc.decoditecalmacen.models;

import androidx.annotation.NonNull;

public class Worker {

    String name, area, id, image;

    public Worker(String id, String name, String area) {
        this.name = name;
        this.area = area;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @NonNull
    @Override
    public String toString() {
        String area =  id.substring(0,3);
        return id.substring(3) + area;
    }
}
