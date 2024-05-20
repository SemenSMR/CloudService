package com.example.cloudservice.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginResponse {
    @JsonProperty("auth-token")
    private String authToken;

    public LoginResponse(String authToken) {
        this.authToken = authToken;

    }

}
