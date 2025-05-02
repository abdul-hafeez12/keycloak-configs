package com.ahafeez.aaa.readonly.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleContainerModel;
import org.keycloak.models.RoleModel;

public class UserRoleDto implements RoleModel {
   private String name;
   private final RealmModel realm;
   @SuppressWarnings({ "rawtypes", "unchecked" })
   private Map<String, List<String>> userRoles = new HashMap();

   public UserRoleDto(String name, RealmModel realm) {
      this.name = name;
      this.realm = realm;
   }

   public String getName() {
      return this.name + " Person";
   }

   public String getDescription() {
      return null;
   }

   public void setDescription(String description) {
   }

   public String getId() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isComposite() {
      return false;
   }

   public void addCompositeRole(RoleModel role) {
   }

   public void removeCompositeRole(RoleModel role) {
   }

   public Stream<RoleModel> getCompositesStream() {
      return null;
   }

   public boolean isClientRole() {
      return false;
   }

   public String getContainerId() {
      return this.realm.getId();
   }

   public RoleContainerModel getContainer() {
      return this.realm;
   }

   public boolean hasRole(RoleModel role) {
      return this.equals(role) || this.name.equals(role.getName());
   }

   public void setSingleAttribute(String name, String value) {
   }

   public void setAttribute(String name, List<String> values) {
      this.userRoles.put(name, values);
   }

   public void removeAttribute(String name) {
   }

   public Stream<String> getAttributeStream(String name) {
      return null;
   }

   public Map<String, List<String>> getAttributes() {
      return this.userRoles;
   }

   public Stream<RoleModel> getCompositesStream(String search, Integer first, Integer max) {
      return null;
   }
}
