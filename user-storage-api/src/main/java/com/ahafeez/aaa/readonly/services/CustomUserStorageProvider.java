package com.ahafeez.aaa.readonly.services;

import com.ahafeez.aaa.readonly.dto.UserDto;
import com.ahafeez.aaa.readonly.webclient.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.ReadOnlyException;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

public class CustomUserStorageProvider
        implements UserStorageProvider, UserLookupProvider, CredentialInputValidator, CredentialInputUpdater {
    protected KeycloakSession session;
    protected ComponentModel model;
    protected Map<String, UserAdapter> loadedUsers = new HashMap<>();
    String baseUrl;
    static final String SPLIT_TOKEN = "-";
    private Logger logger = Logger.getLogger(CustomUserStorageProvider.class);

    private WebClient dataaccessClient;

    public CustomUserStorageProvider(KeycloakSession session, ComponentModel model, String baseUrl) {
        this.session = session;
        this.model = model;
        this.baseUrl = baseUrl;
        this.dataaccessClient = new WebClient((String) model.getConfig().getFirst("url"));
    }
    // @Override
    // public UserModel getUserByUsername(String username, RealmModel realm) {
    // this.logger.debug(" getUserByUsername: username - " + username);
    // UserDto user = this.dataaccessClient.getUserByUserName(username);
    // return user != null ? this.createAdapter(realm,
    // this.dataaccessClient.getUserByUserName(username)) : null;
    // }

    protected UserAdapter createAdapter(RealmModel realm, UserDto user) {
        return new UserAdapter(this.session, realm, this.model, user);
    }

    // @Override
    // public UserModel getUserById(String id, RealmModel realm) {
    // StorageId storageId = new StorageId(id);
    // String username = storageId.getExternalId();
    // return this.getUserByUsername(realm, username);
    // }

    // @Override
    // public UserModel getUserByEmail(String email, RealmModel realm) {
    // return null;
    // }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        System.err.println("isConfiguredFor: credentialType = " + credentialType + ", user = " + user.getClass().getName());
        return this.supportsCredentialType(credentialType) && user instanceof UserAdapter;
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        this.logger.debug(" supportsCredentialType: credentialType - " + credentialType);
        return credentialType.equals("password");
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        System.out.println("isValid: Entering");
        System.out.println("isValid: user class = " + user.getClass().getName());
        System.out.println("isValid: user full info = " + user);
    
        if (this.supportsCredentialType(input.getType()) && input instanceof UserCredentialModel) {
            String username = user.getUsername(); // e.g., "1001-jalal"
            String passwordInput = input.getChallengeResponse();
    
            System.out.println("isValid: Challenge Response - " + passwordInput + " User - " + username);
    
            // Fetch actual user from data source
            UserDto actualUser = dataaccessClient.getUserByUserName(username);
    
            if (actualUser == null) {
                System.out.println("isValid: User not found in data source.");
                return false;
            }
    
            // Compare passwords
            if (passwordInput != null && passwordInput.equalsIgnoreCase(actualUser.getPassword())) {
                System.out.println("isValid: Password is valid.");
                return true;
            } else {
                System.out.println("isValid: Password does not match.");
                return false;
            }
        } else {
            System.out.println("isValid: Unsupported credential type.");
            return false;
        }
    }
    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        if (input.getType().equals("secret")) {
            throw new ReadOnlyException("user is read only for this update");
        } else {
            return false;
        }
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
    }

    // @Override
    // public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel
    // user) {
    // return Collections.emptySet();
    // }

    @Override
    public void close() {
    }

    @Override
    public Stream<String> getDisableableCredentialTypesStream(RealmModel realm, UserModel user) {
        return Stream.empty();
    }

    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        return this.getUserByUsername(realm, username);
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        System.out.println("HERE1 " + username);
        this.logger.debug(" getUserByUsername: username - " + username);

        // Fetch user once and use it for creating the adapter
        UserDto user = this.dataaccessClient.getUserByUserName(username);

        if(user != null){
            System.out.println("Returnd user to provider: " + user.toString());
        }
        // If the user is found, create and return the adapter, otherwise return null
        return user != null ? this.createAdapter(realm, user) : null;
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        return null;
    }

}
