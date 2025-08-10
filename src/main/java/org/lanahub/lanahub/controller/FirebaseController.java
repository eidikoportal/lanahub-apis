package org.lanahub.lanahub.controller;

import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.lanahub.lanahub.model.User;
import org.lanahub.lanahub.services.FirebaseService;
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

    @GetMapping("/{collection}/record/{id}")
    public Object getDocument(
            @PathVariable String collection,
            @PathVariable String id) throws Exception {
        Object document = firebaseService.getDocumentById(collection, id);
        if (document == null) {
            logger.error("Document not found in collection: {}, id: {}", collection, id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new Gson().toJson(document));
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

    @PostMapping(path = "/register-user", consumes = "multipart/form-data")
    public ResponseEntity<String> createUser(@RequestParam("email") String email,
            @RequestParam("role") String role,
            @RequestParam("password") String password,
            @RequestParam("fullName") String fullName,
            @RequestParam("surName") String surName,
            @RequestParam("suburb") String suburb,
            @RequestParam("mobile") String mobile,
            @RequestParam("city") String city,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "profession", required = false) String profession,
            @RequestParam(value = "nationality", required = false) String nationality,
            @RequestParam(value = "longBio", required = false) String longBio,
            @RequestParam(value = "websiteLink", required = false) String websiteLink,
            @RequestParam(value = "experience", required = false) String experience,
            @RequestParam(value = "address", required = false) String address,

            @RequestParam(value = "profilePic", required = false) MultipartFile profilePic,
            @RequestParam(value = "qualificationDoc", required = false) MultipartFile qualificationDoc,
            @RequestParam(value = "policeClearance", required = false) MultipartFile policeClearance,
            @RequestParam(value = "otherDocs1", required = false) MultipartFile otherDocs1,
            @RequestParam(value = "otherDocs2", required = false) MultipartFile otherDocs2,
            @RequestParam(value = "otherDocs3", required = false) MultipartFile otherDocs3)
            throws FirebaseAuthException, IOException {
        logger.info("request.getEmail(): {}", email);

        ResponseMsg responseMsg = new ResponseMsg();
        User user = new User();
        user.setEmail(email);
        user.setRole(role);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setSurName(surName);
        user.setSuburb(suburb);
        user.setMobile(mobile);
        user.setCity(city);
        user.setGender(gender);
        user.setProfession(profession);
        user.setNationality(nationality);
        user.setLongBio(longBio);
        user.setWebsiteLink(websiteLink);
        user.setExperience(experience);
        user.setAddress(address);

        if (profilePic != null && !profilePic.isEmpty()) {
            user.setProfilePic(profilePic.getBytes());
        }
        if (qualificationDoc != null && !qualificationDoc.isEmpty()) {
            user.setQualificationDoc(qualificationDoc.getBytes());
        }
        if (policeClearance != null && !policeClearance.isEmpty()) {
            user.setPoliceClearance(policeClearance.getBytes());
        }
        if (otherDocs1 != null && !otherDocs1.isEmpty()) {
            user.setOtherDocs1(otherDocs1.getBytes());
        }
        if (otherDocs2 != null && !otherDocs2.isEmpty()) {
            user.setOtherDocs2(otherDocs2.getBytes());
        }
        if (otherDocs3 != null && !otherDocs3.isEmpty()) {
            user.setOtherDocs3(otherDocs3.getBytes());
        }
        var userRes = firebaseService.createUser(email, password);
        if (userRes == null || userRes.getUid() == null || userRes.getUid().isEmpty()) {
            logger.error("Failed to create user in Firebase");
            responseMsg.setMessage("User creation failed");
            responseMsg.setCode("001");
            return ResponseEntity.badRequest().body(new Gson().toJson(responseMsg));
        }

        firebaseService.setUserRole(userRes.getUid(), role);
        String id = firebaseService.addUserDetails(userRes, user);
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