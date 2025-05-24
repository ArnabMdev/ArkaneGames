package com.ArnabMdev.ArkaneGames.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Set;
import java.util.HashSet;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String email;
    private String name;
    private String pictureUrl;
    private String provider; // "google", "github", etc.
    private String providerId;
    private String password;
    private Set<String> roles = new HashSet<>();
    private boolean enabled = true;

    public User() {
    }

    public User(String email, String name, String pictureUrl, String provider, String providerId) {
        this.email = email;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.provider = provider;
        this.providerId = providerId;
    }

    // Constructor for form-based authentication
    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.provider = "local";
        this.roles.add("ROLE_USER");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
