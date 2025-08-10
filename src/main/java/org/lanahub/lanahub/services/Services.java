package org.lanahub.lanahub.services;

public interface Services {
    public void createUserDocuments(String userId, String role, byte[] profilePic,
            byte[] qualificationDoc, byte[] policeClearance,
            byte[] otherDocs1, byte[] otherDocs2, byte[] otherDocs3);
}
