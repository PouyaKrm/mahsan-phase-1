package org.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.constansts.ResourceType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseModel {
    private String title;
    protected LocalDate pubDate;
    public abstract void display();
    public abstract ResourceType resourceType();
}

