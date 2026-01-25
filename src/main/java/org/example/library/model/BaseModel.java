package org.example.library.model;

import org.example.constansts.ResourceType;

import java.time.LocalDate;


public abstract class BaseModel {
    private String title;
    protected LocalDate pubDate;

    public BaseModel(String title, LocalDate pubDate) {
        this.title = title;
        this.pubDate = pubDate;
    }

    public BaseModel() {
    }

    public abstract void display();
    public abstract ResourceType resourceType();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPubDate() {
        return pubDate;
    }

    public void setPubDate(LocalDate pubDate) {
        this.pubDate = pubDate;
    }
}

