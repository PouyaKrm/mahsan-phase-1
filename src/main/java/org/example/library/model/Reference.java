package org.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.constansts.ResourceType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString
public class Reference extends BaseModel {
    private String author;
    private String content;

    public Reference(LocalDate pubDate, String title, String author, String content) {
        super(title, pubDate);
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
}
