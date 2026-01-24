package org.example.library;

import java.time.LocalDate;

public class Book {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPubDate() {
        return pubDate;
    }

    public void setPubDate(LocalDate pubDate) {
        this.pubDate = pubDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
