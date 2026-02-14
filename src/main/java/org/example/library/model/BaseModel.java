package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.constansts.AppConfigs;
import org.example.constansts.ResourceType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;


public abstract class BaseModel {
    protected Long id;
    protected String title;
    protected String author;
    protected String content;

    @JsonFormat(pattern = AppConfigs.DATE_FORMAT)
    protected LocalDate borrowDate;

    @JsonFormat(pattern = AppConfigs.DATE_FORMAT)
    protected LocalDate pubDate;

    public BaseModel(String title, String author, String content, LocalDate pubDate) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.pubDate = pubDate;
    }

    public BaseModel() {
    }

    public BaseModel(Long id, String title, String author, String content, LocalDate pubDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.pubDate = pubDate;
    }

    public abstract void display();

    public abstract ResourceType resourceType();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPubDate() {
        return pubDate;
    }

    @JsonIgnore
    public Long getPubDateEpochDay() {
        return Objects.nonNull(pubDate) ? pubDate.toEpochDay() : null;
    }

    public void setPubDate(LocalDate pubDate) {
        if(Objects.isNull(pubDate)) {
            return;
        }
        this.pubDate = pubDate;
    }

    public void setPubDateFromEpochDay(Long epochDay) {
        if(Objects.isNull(pubDate)) {
            return;
        }
        this.pubDate = LocalDate.ofEpochDay(epochDay);;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    @JsonIgnore
    public Long getBorrowDateEpochDay() {
        return Objects.nonNull(borrowDate) ? borrowDate.toEpochDay() : null;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        if(Objects.isNull(borrowDate)) {
            return;
        }
        this.borrowDate = borrowDate;
    }

    public void setBorrowDateFromEpochDay(Long epochDay) {
        if(Objects.isNull(pubDate)) {
            return;
        }
        this.borrowDate = LocalDate.ofEpochDay(epochDay);;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

