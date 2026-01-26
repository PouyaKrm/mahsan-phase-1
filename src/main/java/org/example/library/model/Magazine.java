package org.example.library.model;

import org.example.constansts.ResourceType;

import java.time.LocalDate;


public class Magazine extends BaseModel {

    public Magazine(LocalDate pubDate, String title, String author, String content) {
        super(title, author, content, pubDate);
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

    @Override
    public String toString() {
        return "Magazine{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", pubDate=" + pubDate +
                '}';
    }
}
