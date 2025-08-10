package org.lanahub.lanahub.services;

import org.lanahub.lanahub.model.UserModel;
import org.lanahub.lanahub.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements Services {

    @Autowired
    private UserRepository repo;

    public UserModel save(UserModel user) {
        return repo.save(user);
    }

    public List<UserModel> getAll() {
        return repo.findAll();
    }

    public Optional<UserModel> getById(String userId) {
        if (userId == null || userId.isEmpty()) {
            return Optional.empty();
        }
        return repo.findById(userId);
    }

    public void delete(String userId) {
        repo.deleteById(userId);
    }

    @Override
    public void createUserDocuments(String userId, String role, byte[] profilePic,
            byte[] qualificationDoc, byte[] policeClearance,
            byte[] otherDocs1, byte[] otherDocs2, byte[] otherDocs3) {
        UserModel userModel = new UserModel();
        userModel.setUserId(userId);
        userModel.setRole(role);
        userModel.setProfilePic(profilePic);
        userModel.setQualificationDoc(qualificationDoc);
        userModel.setPoliceClearance(policeClearance);
        userModel.setOtherDocs1(otherDocs1);
        userModel.setOtherDocs2(otherDocs2);
        userModel.setOtherDocs3(otherDocs3);

        save(userModel);
    }
}
