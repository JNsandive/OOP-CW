package com.movie.ticketing.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}