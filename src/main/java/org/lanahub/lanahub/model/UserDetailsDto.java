package org.lanahub.lanahub.model;

public class UserDetailsDto {
    private String userId;
    private String role;

    private String profilePicUrl;
    private String qualificationDocUrl;
    private String policeClearanceUrl;
    private String otherDocs1Url;
    private String otherDocs2Url;
    private String otherDocs3Url;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getQualificationDocUrl() {
        return qualificationDocUrl;
    }

    public void setQualificationDocUrl(String qualificationDocUrl) {
        this.qualificationDocUrl = qualificationDocUrl;
    }

    public String getPoliceClearanceUrl() {
        return policeClearanceUrl;
    }

    public void setPoliceClearanceUrl(String policeClearanceUrl) {
        this.policeClearanceUrl = policeClearanceUrl;
    }

    public String getOtherDocs1Url() {
        return otherDocs1Url;
    }

    public void setOtherDocs1Url(String otherDocs1Url) {
        this.otherDocs1Url = otherDocs1Url;
    }

    public String getOtherDocs2Url() {
        return otherDocs2Url;
    }

    public void setOtherDocs2Url(String otherDocs2Url) {
        this.otherDocs2Url = otherDocs2Url;
    }

    public String getOtherDocs3Url() {
        return otherDocs3Url;
    }

    public void setOtherDocs3Url(String otherDocs3Url) {
        this.otherDocs3Url = otherDocs3Url;
    }

}
