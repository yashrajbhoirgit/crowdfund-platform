package com.crowdfund.auth.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String name;
    private String bio;
    private String location;
    private String phone;
    private String website;
}
