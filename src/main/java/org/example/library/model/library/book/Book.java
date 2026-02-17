package org.example.library.model.library.book;

import org.example.constansts.ResourceType;
import org.example.exception.InvalidOperationException;
import org.example.library.Displayable;
import org.example.library.model.BaseLibraryModel;

import java.time.LocalDate;


public class Book extends BaseLibraryModel implements Displayable {
    private Status status;

    public Book() {
    }

    public Book(LocalDate pubDate, String title, String author, String content, Status status) {
        super(title, author, content, pubDate);
        this.status = status;
    }

    public Book(Long id, LocalDate pubDate, String title, String author, String content, Status status) {
        super(id, title, author, content, pubDate);
        this.status = status;
    }


    @Override
    public void display() {
        System.out.println(this);
    }

    @Override
    public ResourceType resourceType() {
        return ResourceType.BOOK;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static enum Status {
        BANNED,
        BORROWED,
        EXIST
    }


    public static Book borrowItem(Book model) throws InvalidOperationException {
        if(!model.getStatus().equals(Status.EXIST)) {
            throw new InvalidOperationException("can not borrow this book");
        }
        model.setStatus(Status.BORROWED);
        model.setBorrowDate(LocalDate.now());
        return model;
    }

    @Override
    public String toString() {
        return "Book{" +
                "status=" + status +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", borrowDate=" + borrowDate +
                ", pubDate=" + pubDate +
                '}';
    }
}
