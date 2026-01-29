package org.example.library.model.magazine;

import org.example.constansts.ResourceType;
import org.example.library.model.BaseModel;

import java.time.LocalDate;


public class Magazine extends BaseModel {

    public Magazine(LocalDate pubDate, String title, String author, String content) {
        super(title, author, content, pubDate);
    }

    public Magazine(Long id, LocalDate pubDate, String title, String author, String content) {
        super(id, title, author, content, pubDate);
    }

    @Override
    public void display() {
        System.out.println(toString());
    }

    @Override
    public ResourceType resourceType() {
        return ResourceType.MAGAZINE;
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
