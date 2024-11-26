package com.example.sjhealthy.dto;

import lombok.Data;

@Data
public class OAuthToken {
    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
