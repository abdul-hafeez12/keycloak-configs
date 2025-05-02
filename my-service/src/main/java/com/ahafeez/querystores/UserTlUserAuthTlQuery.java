package com.ahafeez.querystores;


import com.ahafeez.dto.UserTlDto;

public class UserTlUserAuthTlQuery {

    private UserTlUserAuthTlQuery() {}

    public static String getUserWithAuthQuery(UserTlDto user) {
        return "SELECT brn_cd, user_id, password , full_name FROM user WHERE BRN_CD ="+user.getBranchCode()+" AND USER_ID = '"+user.getUserName()+"'";

    }

}
