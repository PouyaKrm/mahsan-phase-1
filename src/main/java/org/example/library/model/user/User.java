package org.example.library.model.user;

import org.example.library.model.BaseModel;

public class User extends BaseModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
