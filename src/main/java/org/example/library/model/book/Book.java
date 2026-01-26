package org.example.library.model.book;

import org.example.constansts.ResourceType;
import org.example.library.model.BaseModel;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Book extends BaseModel {
    private Status status;

    public Book(LocalDate pubDate, String title, String author, String content, Status status) {
        super(title, author, content, pubDate);
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
