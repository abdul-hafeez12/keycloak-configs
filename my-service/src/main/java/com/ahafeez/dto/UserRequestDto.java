package com.ahafeez.dto;

import io.smallrye.common.constraint.NotNull;
import jakarta.ws.rs.QueryParam;

public class UserRequestDto {
    @QueryParam("userId")
    @NotNull
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
