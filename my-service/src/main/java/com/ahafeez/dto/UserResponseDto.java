package com.ahafeez.dto;

import org.jboss.resteasy.api.validation.ViolationReport;

public class UserResponseDto {
    private ViolationReport violationReport;
    private UserTlDto userTlDto;

    private String userProfileDisabled;

    public ViolationReport getViolationReport() {
        return violationReport;
    }

    public void setViolationReport(ViolationReport violationReport) {
        this.violationReport = violationReport;
    }

    public UserTlDto getUserTlDto() {
        return userTlDto;
    }

    public void setUserTlDto(UserTlDto userTlDto) {
        this.userTlDto = userTlDto;
    }

    public String getUserProfileDisabled() {
        return userProfileDisabled;
    }

    public void setUserProfileDisabled(String userProfileDisabled) {
        this.userProfileDisabled = userProfileDisabled;
    }
}
