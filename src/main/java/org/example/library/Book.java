package org.example.library;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Book extends BaseModel {
    private String title;
    private String author;
    private LocalDate pubDate;
    private Status status;

    public Book(String title, String author, LocalDate pubDate, Status status) {
        this.title = title;
        this.author = author;
        this.pubDate = pubDate;
        this.status = status;
    }

    @Override
    public void display() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pubDate=" + pubDate +
                ", status=" + status +
                '}';
    }

    public static enum Status {
        BANNED,
        BORROWED,
        EXIST
    }

}
