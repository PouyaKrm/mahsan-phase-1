package org.example.library.model;

import org.example.constansts.ResourceType;

import java.time.LocalDate;


public class Article extends BaseModel {

    public Article(LocalDate pubDate, String title, String author, String content) {
        super(title, author, content, pubDate);
    }

    @Override
    public void display() {
        System.out.println(toString());
    }

    @Override
    public ResourceType resourceType() {
        return ResourceType.ARTICLE;
    }

    @Override
    public String toString() {
        return "Article{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", pubDate=" + pubDate +
                '}';
    }
}
