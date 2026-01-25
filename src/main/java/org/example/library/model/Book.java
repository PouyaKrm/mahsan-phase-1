package org.example.library.model;

import org.example.constansts.ResourceType;

import java.time.LocalDate;


public class Book extends BaseModel {
    private String author;
    private Status status;

    public Book(LocalDate pubDate, String title, String author, Status status) {
        super(title, pubDate);
        this.author = author;
        this.status = status;
    }

    @Override
    public void display() {
        System.out.println(toString());
    }

    @Override
    public ResourceType resourceType() {
        return ResourceType.BOOK;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static enum Status {
        BANNED,
        BORROWED,
        EXIST
    }

}
