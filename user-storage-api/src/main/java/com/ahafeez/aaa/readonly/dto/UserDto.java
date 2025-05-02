package com.ahafeez.aaa.readonly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class UserDto {
   private String branchCode;
   private String userName;
   private String userId;
   private String password;

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getBranchCode() {
      return this.branchCode;
   }

   public void setBranchCode(String branchCode) {
      this.branchCode = branchCode;
   }

   public String getUserId() {
      return this.userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String toString() {
      return "UserDto [branchCode=" + this.branchCode + ", password=" + this.password + ", userId=" + this.userId + ", userName=" + this.userName + "]";
   }
}
