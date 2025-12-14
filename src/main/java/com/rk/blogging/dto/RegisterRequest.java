package com.rk.blogging.dto;

import com.rk.blogging.model.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private User.Role role;
}