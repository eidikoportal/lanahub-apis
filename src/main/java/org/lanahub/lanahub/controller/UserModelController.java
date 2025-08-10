package org.lanahub.lanahub.controller;

import org.lanahub.lanahub.model.UserModel;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypes;
import org.lanahub.lanahub.model.DataResponse;
import org.lanahub.lanahub.model.GenericResponse;
import org.lanahub.lanahub.model.UserDetailsDto;
import org.lanahub.lanahub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lana-hub-api/${app.api.version}/user/docs")
public class UserModelController {

    @Autowired
    private UserService service;

    @Value("${app.api.version}")
    private String apiVersion;

    @GetMapping("/all")
    public ResponseEntity<List<UserModel>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createUserModel(
            @RequestParam("userId") String userId,
            @RequestParam("role") String role,
            @RequestParam(value = "profilePic", required = false) MultipartFile profilePic,
            @RequestParam(value = "qualificationDoc", required = false) MultipartFile qualificationDoc,
            @RequestParam(value = "policeClearance", required = false) MultipartFile policeClearance,
            @RequestParam(value = "otherDocs1", required = false) MultipartFile otherDocs1,
            @RequestParam(value = "otherDocs2", required = false) MultipartFile otherDocs2,
            @RequestParam(value = "otherDocs3", required = false) MultipartFile otherDocs3) {

        try {
            UserModel userModel = new UserModel();
            userModel.setUserId(userId);
            userModel.setRole(role);

            userModel.setProfilePic(getFileBytes(profilePic));
            userModel.setQualificationDoc(getFileBytes(qualificationDoc));
            userModel.setPoliceClearance(getFileBytes(policeClearance));
            userModel.setOtherDocs1(getFileBytes(otherDocs1));
            userModel.setOtherDocs2(getFileBytes(otherDocs2));
            userModel.setOtherDocs3(getFileBytes(otherDocs3));

            service.save(userModel);

            return ResponseEntity.ok(new GenericResponse(true, "User documents uploaded successfully."));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new GenericResponse(false, "Error processing file upload."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new GenericResponse(false, "Unexpected error: " + e.getMessage()));
        }
    }

    private byte[] getFileBytes(MultipartFile file) throws IOException {
        return (file != null && !file.isEmpty()) ? file.getBytes() : null;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DataResponse<UserDetailsDto>> getUserModel(@PathVariable String userId) {
        Optional<UserModel> optional = service.getById(userId);

        if (optional.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new DataResponse<>(false, "User not found", null));
        }

        UserModel user = optional.get();
        UserDetailsDto dto = new UserDetailsDto();
        dto.setUserId(user.getUserId());
        dto.setRole(user.getRole());

        // Generate links with file extension
        if (user.getProfilePic() != null) {
            dto.setProfilePicUrl(buildDownloadUrl(user.getUserId(), "profile-pic", user.getProfilePic()));
        }
        if (user.getQualificationDoc() != null) {
            dto.setQualificationDocUrl(
                    buildDownloadUrl(user.getUserId(), "qualification-doc", user.getQualificationDoc()));
        }
        if (user.getPoliceClearance() != null) {
            dto.setPoliceClearanceUrl(
                    buildDownloadUrl(user.getUserId(), "police-clearance", user.getPoliceClearance()));
        }
        if (user.getOtherDocs1() != null) {
            dto.setOtherDocs1Url(buildDownloadUrl(user.getUserId(), "other-docs-1", user.getOtherDocs1()));
        }
        if (user.getOtherDocs2() != null) {
            dto.setOtherDocs2Url(buildDownloadUrl(user.getUserId(), "other-docs-2", user.getOtherDocs2()));
        }
        if (user.getOtherDocs3() != null) {
            dto.setOtherDocs3Url(buildDownloadUrl(user.getUserId(), "other-docs-3", user.getOtherDocs3()));
        }

        return ResponseEntity.ok(new DataResponse<>(true, "User found", dto));
    }

    private String buildDownloadUrl(String userId, String docType, byte[] fileData) {
        try {
            Tika tika = new Tika();
            String contentType = tika.detect(fileData);

            String extension = MimeTypes.getDefaultMimeTypes()
                    .forName(contentType)
                    .getExtension();
            if (extension == null) {
                extension = "";
            }

            // Example: /users/{userId}/download/{docType}/{docType}.pdf
            return String.format("/%s/download/%s/%s%s", userId, docType,
                    docType, extension);

        } catch (Exception e) {
            // Fallback without extension
            return String.format("/%s/download/%s", userId, docType);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<GenericResponse> deleteUserModel(@PathVariable String userId) {
        try {
            service.delete(userId);
            return ResponseEntity.ok(new GenericResponse(true, "User documents deleted successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new GenericResponse(false, "Failed to delete user: " + e.getMessage()));
        }
    }

    @GetMapping("/{userId}/download/{docType}/{fileName}")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable String userId,
            @PathVariable String docType,
            @PathVariable String fileName) {

        Optional<UserModel> optional = service.getById(userId);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserModel user = optional.get();
        byte[] fileData = null;
        switch (docType) {
            case "profile-pic":
                fileData = user.getProfilePic();
                break;
            case "qualification-doc":
                fileData = user.getQualificationDoc();
                break;
            case "police-clearance":
                fileData = user.getPoliceClearance();
                break;
            case "other-docs-1":
                fileData = user.getOtherDocs1();
                break;
            case "other-docs-2":
                fileData = user.getOtherDocs2();
                break;
            case "other-docs-3":
                fileData = user.getOtherDocs3();
                break;
            default:
                fileData = null;
        }

        if (fileData == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Tika tika = new Tika();
            String contentType = tika.detect(fileData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileData);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
