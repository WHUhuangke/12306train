package com.example.member.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_member")
public class MemberEntity {
    @Id
    private String id;
    private String username;
    private String passwordHash;
    private String permissions;
    private String frequentPassengers;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }
    public String getFrequentPassengers() { return frequentPassengers; }
    public void setFrequentPassengers(String frequentPassengers) { this.frequentPassengers = frequentPassengers; }
}
