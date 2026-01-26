package org.example.library.model;

import org.example.constansts.ResourceType;

import java.time.LocalDate;


public abstract class BaseModel {
    protected String title;
    protected String author;
    protected String content;
    protected LocalDate borrowDate;
    protected LocalDate pubDate;

    public BaseModel(String title, String author, String content, LocalDate pubDate) {
        this.title = title;
        this.author = author;
        this.content = content;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
}

