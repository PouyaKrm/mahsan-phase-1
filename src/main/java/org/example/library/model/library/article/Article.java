package org.example.library.model.library.article;

import org.example.constansts.ResourceType;
import org.example.library.model.BaseLibraryModel;

import java.time.LocalDate;


public class Article extends BaseLibraryModel {


    public Article(LocalDate pubDate, String title, String author, String content) {
        super(title, author, content, pubDate);
    }

    public Article(Long id, LocalDate pubDate, String title, String author, String content) {
        super(id, title, author, content, pubDate);
    }

    public Article() {
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
