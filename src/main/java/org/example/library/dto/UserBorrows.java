package org.example.library.dto;

import org.example.library.Displayable;
import org.example.library.model.user.User;

public record UserBorrows(Long count, User user) implements Displayable {
    @Override
    public void display() {
        var msg = new StringBuilder().append("count: ").append(count).append(" - ").append("user: ").append(user);
        System.out.println(msg);
    }
}
