package com.ahafeez.dto;

import java.math.BigDecimal;

import com.ahafeez.util.ResponseParam;
import com.ahafeez.util.Table;

public class UserTlDto {
    @Table(column = "password")
    @ResponseParam(name = "password")
    private String password;

    @Table(column = "brn_cd")
    @ResponseParam(name = "branchCode")
    private BigDecimal branchCode;

    @Table(column = "user_id")
    @ResponseParam(name = "userName")
    private String userName;

    @Table(column = "full_name")
    @ResponseParam(name = "fullName")
    private String fullName;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(BigDecimal branchCode) {
        this.branchCode = branchCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getfullName() {
        return fullName;
    }

    public void setfullName(String fullName) {
        this.fullName = fullName;
    }
}
