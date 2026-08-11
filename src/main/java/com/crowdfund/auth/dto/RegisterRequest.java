package com.crowdfund.auth.dto;

import com.crowdfund.auth.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
