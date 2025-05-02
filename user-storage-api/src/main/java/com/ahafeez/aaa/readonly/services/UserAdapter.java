package com.ahafeez.aaa.readonly.services;

import com.ahafeez.aaa.readonly.dto.UserDto;
import java.util.HashSet;
import java.util.Set;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

public class UserAdapter extends AbstractUserAdapterFederatedStorage {
   UserDto user;
   Set<RoleModel> userRoles = new HashSet<>();
   static final String SPLIT_TOKEN = "-";

   public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, UserDto user) {
      super(session, realm, model);
      // System.out.println("creating adpter :"+user.toString());
      // this.storageId = new StorageId(this.storageProviderModel.getId(),
      // user.getUserId());
      // Combine branchCode and userId into a unique external ID
      String compositeId = user.getBranchCode() + "-" + user.getUserId();
      System.out.println("Creating adapter with composite ID: " + compositeId);

      // Store this combined ID so that getUserById can find the user later
      this.storageId = new StorageId(this.storageProviderModel.getId(), compositeId);

      this.user = user;
   }

   public String getUsername() {
      return  this.user.getBranchCode() + "-" + this.user.getUserId();
   }

   public void setUsername(String username) {
      this.user.setUserId(username);
   }

   protected Set<RoleModel> getFederatedRoleMappings() {
      return this.userRoles;
   }

   public String toString() {
      return "UserAdapter [UserDto=" + this.user + "]";
   }
}
