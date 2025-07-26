package org.lanahub.lanahub.model;

public class UserInfoResponse {
    private final String uid;
    private final String email;
    private final String role;
    public UserInfoResponse(String uid, String email, String role) {
        this.uid = uid;
        this.email = email;
        this.role = role;
    }
    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
    
    
}
