package org.example.library.model.library.magazine;

import org.example.constansts.ResourceType;
import org.example.library.model.BaseLibraryModel;

import java.time.LocalDate;


public class Magazine extends BaseLibraryModel {

    public Magazine(LocalDate pubDate, String title, String author, String content) {
        super(title, author, content, pubDate);
    }

    public Magazine(Long id, LocalDate pubDate, String title, String author, String content) {
        super(id, title, author, content, pubDate);
    }

    public Magazine() {
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
