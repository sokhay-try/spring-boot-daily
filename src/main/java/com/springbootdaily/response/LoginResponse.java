package com.springbootdaily.response;

import com.springbootdaily.entities.User;
import lombok.Data;

@Data
public class LoginResponse {
    private User user;
    private String accessToken;
}
