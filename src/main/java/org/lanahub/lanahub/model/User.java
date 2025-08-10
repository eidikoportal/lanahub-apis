package org.lanahub.lanahub.model;

public class User {
    private String email;
    private String password;
    private String fullName;
    private String surName;
    private String suburb;
    private String mobile;
    private String city;
    private String gender;
    private String profession;
    private String nationality;
    private String longBio;
    private String websiteLink;
    private String experience;
    private String address;
    private byte[] profilePic;
    private byte[] qualificationDoc;
    private byte[] policeClearance;
    private byte[] otherDocs1;
    private byte[] otherDocs2;
    private byte[] otherDocs3;
    private String createdAt;
    private String role;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLongBio() {
        return longBio;
    }

    public void setLongBio(String longBio) {
        this.longBio = longBio;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
