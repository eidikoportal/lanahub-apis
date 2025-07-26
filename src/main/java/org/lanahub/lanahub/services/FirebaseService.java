package org.lanahub.lanahub.services;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.FirebaseAuthException;

import org.lanahub.lanahub.controller.FirebaseController;
import org.lanahub.lanahub.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class FirebaseService {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

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
        Map<String, Object> userDetails = Map.of(
                "fullName", request.getFullName(),
                "surName", request.getSurName(),
                "suburb", request.getSuburb(),
                "mobile", request.getMobile(),
                "city", request.getCity(),
                "profilePic", request.getProfilePic() != null ? request.getProfilePic() : "");
        db.collection("users").document(user.getUid()).set(userDetails);
        // return db status after adding user details
        DocumentReference docRef = db.collection("users").document(user.getUid());
        if (docRef == null) {
            throw new IllegalStateException("Failed to add user details, document reference is null");
        }
        return user.getUid();
    }
    // OAuth and SSO would typically be handled via Spring Security and Firebase
    // custom tokens.
}