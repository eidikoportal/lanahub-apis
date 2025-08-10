package org.lanahub.lanahub.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.FirebaseAuthException;

import org.lanahub.lanahub.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

@Service
public class FirebaseService {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);
    @Autowired
    private UserService userService;

    public UserRecord createUser(String email, String password) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);
        try {
            return FirebaseAuth.getInstance().createUser(request);
        } catch (FirebaseAuthException e) {
            if ("EMAIL_EXISTS".equals(e.getMessage())) {
                // Handle specific "email already exists" error
                logger.warn("User with this email already exists: {}", email);
                // Optionally, throw a custom exception or return null
                return null;
            } else {
                // Handle other FirebaseAuth errors
                logger.error("FirebaseAuth error: {}", e.getMessage());
                return null; // Re-throw or wrap if needed
            }
        }
    }

    public UserRecord getUserById(String uid) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().getUser(uid);
    }

    public Object getDocumentById(String collectionName, String documentId) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        DocumentReference docRef = db.collection(collectionName).document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.getData(); // returns a Map<String, Object>
        } else {
            return null; // or throw exception
        }
    }

    public List<UserRecord> getAllUsers() throws FirebaseAuthException {
        List<UserRecord> users = new ArrayList<>();
        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        for (UserRecord user : page.iterateAll()) {
            users.add(user);
        }
        return users;
    }

    public UserRecord updateUser(String uid, String email) throws FirebaseAuthException {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                .setEmail(email);
        return FirebaseAuth.getInstance().updateUser(request);
    }

    public void deleteUser(String uid) throws FirebaseAuthException {
        FirebaseAuth.getInstance().deleteUser(uid);
    }

    public boolean checkAuth(String idToken) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        return decodedToken != null;
    }

    public FirebaseToken verifyIdToken(String idToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    public String getUserRole(FirebaseToken token) {
        Map<String, Object> claims = token.getClaims();
        return (String) claims.get("role");
    }

    public void setUserRole(String uid, String role) throws FirebaseAuthException {
        FirebaseAuth.getInstance().setCustomUserClaims(uid, Map.of("role", role));
    }

    public String addUserDetails(UserRecord user, User request) throws IOException, FirebaseAuthException {
        if (user == null || request == null) {
            throw new IllegalArgumentException("User or request cannot be null");
        }
        Firestore db = FirestoreClient.getFirestore();

        Map<String, Object> userDetails = Map.ofEntries(
                Map.entry("email", StringUtils.defaultString(request.getEmail())),
                Map.entry("fullName", StringUtils.defaultString(request.getFullName())),
                Map.entry("surName", StringUtils.defaultString(request.getSurName())),
                Map.entry("suburb", StringUtils.defaultString(request.getSuburb())),
                Map.entry("mobile", StringUtils.defaultString(request.getMobile())),
                Map.entry("city", StringUtils.defaultString(request.getCity())),
                Map.entry("role", StringUtils.defaultString(request.getRole())),
                Map.entry("experience", StringUtils.defaultString(request.getExperience())),
                Map.entry("gender", StringUtils.defaultString(request.getGender())),
                Map.entry("longBio", StringUtils.defaultString(request.getLongBio())),
                Map.entry("websiteLink", StringUtils.defaultString(request.getWebsiteLink())),
                Map.entry("createdAt", StringUtils.defaultString(request.getCreatedAt())),
                Map.entry("nationality", StringUtils.defaultString(request.getNationality())),
                Map.entry("address", StringUtils.defaultString(request.getAddress())),
                Map.entry("profession", StringUtils.defaultString(request.getProfession())));

        db.collection("users").document(user.getUid()).set(userDetails);
        // return db status after adding user details
        DocumentReference docRef = db.collection("users").document(user.getUid());
        if (docRef == null) {
            throw new IllegalStateException("Failed to add user details, document reference is null");
        } else {
            // create user documents
            userService.createUserDocuments(user.getUid(), request.getRole(),
                    request.getProfilePic(), request.getQualificationDoc(),
                    request.getPoliceClearance(), request.getOtherDocs1(),
                    request.getOtherDocs2(), request.getOtherDocs3());
            logger.info("User details added successfully for UID: {}", user.getUid());
        }
        return user.getUid();
    }
    // OAuth and SSO would typically be handled via Spring Security and Firebase
    // custom tokens.
}