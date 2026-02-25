package org.example.library.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public abstract class BaseModel {
    protected Long id;
    protected Long version = 0L;

    public BaseModel() {
    }

    public BaseModel(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return Objects.nonNull(version) ? version : 0L;
    }

    public Long getNextVersion() {
        return version + 1;
    }
}
