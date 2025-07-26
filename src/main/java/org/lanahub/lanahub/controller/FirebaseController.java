package org.lanahub.lanahub.controller;

import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import javax.annotation.processing.Generated;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.lanahub.lanahub.model.User;
import org.lanahub.lanahub.services.FirebaseService;
import org.lanahub.lanahub.model.IdTokenRequest;
import com.google.gson.Gson;
import org.lanahub.lanahub.model.UserInfoResponse;
import org.lanahub.lanahub.model.ResponseMsg;

@RestController
@RequestMapping("/lana-hub-api/${app.api.version}")
public class FirebaseController {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseController.class);
    @Autowired
    private FirebaseService firebaseService;

    @GetMapping("/")
    public String home() {
        return "Welcome to LanaHub!";
    }

    @GetMapping("/user/{id}")
    public UserRecord getUserById(@PathVariable String id) throws FirebaseAuthException {
        return firebaseService.getUserById(id);
    }

    @GetMapping("/all")
    public List<UserRecord> getAllUsers() throws FirebaseAuthException {
        logger.info("Fetching all users from Firebase");
        if (firebaseService == null) {
            logger.error("FirebaseService is not initialized");
            throw new IllegalStateException("FirebaseService is not initialized");
        }
        List<UserRecord> users = firebaseService.getAllUsers();
        if (users.isEmpty()) {
            logger.warn("No users found in Firebase");
        } else {
            logger.info("Users fetched successfully");
        }
        return users;
    }

    @PostMapping("/register-user")
    public ResponseEntity<String> createUser(@RequestBody User request) throws FirebaseAuthException, IOException {
        logger.info("request.getEmail(): {}", request.getEmail());

        ResponseMsg responseMsg = new ResponseMsg();

        var user = firebaseService.createUser(request.getEmail(), request.getPassword());
        if (user == null || user.getUid() == null || user.getUid().isEmpty()) {
            logger.error("Failed to create user in Firebase");
            responseMsg.setMessage("User creation failed");
            responseMsg.setCode("001");
            return ResponseEntity.badRequest().body(new Gson().toJson(responseMsg));
        }

        firebaseService.setUserRole(user.getUid(), request.getRole());
        String id = firebaseService.addUserDetails(user, request);
        logger.info("User created successfully with UID: {}", id);

        if (id == null || id.isEmpty()) {
            logger.error("Failed to add user details");
            responseMsg.setMessage("User details addition failed");
            responseMsg.setCode("002");
            return ResponseEntity.badRequest().body(new Gson().toJson(responseMsg));
        }

        responseMsg.setMessage("User created successfully");
        responseMsg.setCode("201");
        return ResponseEntity.ok(new Gson().toJson(responseMsg));
    }

    @PutMapping("/update")
    public UserRecord updateUser(@RequestParam String uid, @RequestParam String email) throws FirebaseAuthException {
        return firebaseService.updateUser(uid, email);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam String uid) throws FirebaseAuthException {
        firebaseService.deleteUser(uid);
    }

    @PostMapping("/auth-check")
    public boolean checkAuth(@RequestParam String idToken) throws FirebaseAuthException {
        return firebaseService.checkAuth(idToken);
    }

    @PostMapping("/verify-token")
    public ResponseEntity<UserInfoResponse> verifyToken(
            @RequestHeader("Authorization") String authorizationHeader) throws FirebaseAuthException {

        // Extract token from "Bearer <token>"
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build(); // or unauthorized
        }

        String idToken = authorizationHeader.substring(7); // remove "Bearer "

        logger.info("Verifying ID token from header");

        FirebaseToken decoded = firebaseService.verifyIdToken(idToken);
        String role = firebaseService.getUserRole(decoded);

        return ResponseEntity.ok(new UserInfoResponse(decoded.getUid(), decoded.getEmail(), role));
    }

}