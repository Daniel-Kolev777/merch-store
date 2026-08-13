package com.merchstore.helpers;

import com.merchstore.models.User;
import org.springframework.stereotype.Component;

import static com.merchstore.models.enums.Role.ADMIN;
import static com.merchstore.models.enums.Role.USER;

@Component
public class RoleValidator {

    public static boolean isUser(User user) {
        return USER.equals(user.getRole());
    }

    public static boolean isAdmin(User user) {
        return ADMIN.equals(user.getRole());
    }
}
