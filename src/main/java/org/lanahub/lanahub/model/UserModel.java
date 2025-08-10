package org.lanahub.lanahub.model;

import javax.persistence.*;

@Entity
@Table(name = "lanahub_users")
public class UserModel {

    @Id
    @Column(length = 100)
    private String userId;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] profilePic;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] qualificationDoc;

    @Column(length = 50)
    private String role;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] policeClearance;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] otherDocs1;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] otherDocs2;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] otherDocs3;

    public UserModel() {
    }

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public byte[] getQualificationDoc() {
        return qualificationDoc;
    }

    public void setQualificationDoc(byte[] qualificationDoc) {
        this.qualificationDoc = qualificationDoc;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public byte[] getPoliceClearance() {
        return policeClearance;
    }

    public void setPoliceClearance(byte[] policeClearance) {
        this.policeClearance = policeClearance;
    }

    public byte[] getOtherDocs1() {
        return otherDocs1;
    }

    public void setOtherDocs1(byte[] otherDocs1) {
        this.otherDocs1 = otherDocs1;
    }

    public byte[] getOtherDocs2() {
        return otherDocs2;
    }

    public void setOtherDocs2(byte[] otherDocs2) {
        this.otherDocs2 = otherDocs2;
    }

    public byte[] getOtherDocs3() {
        return otherDocs3;
    }

    public void setOtherDocs3(byte[] otherDocs3) {
        this.otherDocs3 = otherDocs3;
    }
}
