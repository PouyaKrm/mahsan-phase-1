package org.example.library.model;

import org.example.constansts.ResourceType;

import java.time.LocalDate;


public class Reference extends BaseModel {

    public Reference(LocalDate pubDate, String title, String author, String content) {
        super(title, author, content, pubDate);
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
        return "Reference{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", pubDate=" + pubDate +
                '}';
    }
}
