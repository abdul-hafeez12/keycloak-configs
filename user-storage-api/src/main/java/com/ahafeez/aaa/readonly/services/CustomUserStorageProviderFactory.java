package com.ahafeez.aaa.readonly.services;

import java.util.List;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

public class CustomUserStorageProviderFactory implements UserStorageProviderFactory<CustomUserStorageProvider> {
   protected static final List<ProviderConfigProperty> configMetadata = ProviderConfigurationBuilder.create().property().name("url").type("String").label("Dataaccess URL").helpText("Please provde the base URL for the Dataaccess services").defaultValue("http://192.168.11.224:9091").add().build();

   public String getId() {
      return "Custom User Storage SPI";
   }

   public CustomUserStorageProvider create(KeycloakSession session, ComponentModel model) {
      String daUrl = (String)model.getConfig().getFirst("url");
      CustomUserStorageProvider UserStorageProvider = new CustomUserStorageProvider(session, model, daUrl);
      return UserStorageProvider;
   }

   public List<ProviderConfigProperty> getConfigProperties() {
      return configMetadata;
   }

   public String getHelpText() {
      return "User Storage Provider, which uses SQL DB to store and authenticate users";
   }
}
