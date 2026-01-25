package org.example.library.model;

import org.example.constansts.ResourceType;

import java.time.LocalDate;


public class Magazine extends BaseModel {
    private String author;
    private String content;

    public Magazine(LocalDate pubDate, String title, String author, String content) {
        super(title, pubDate);
        this.author = author;
        this.content = content;
    }

    @Override
    public void display() {
        System.out.println(toString());
    }

    @Override
    public ResourceType resourceType() {
        return ResourceType.REFERENCE;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", pubDate=" + pubDate +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
