package com.spring.messenger.controller;

import com.spring.messenger.model.UserProfile;
import com.spring.messenger.model.dto.UserProfileDTO;
import com.spring.messenger.repository.UserProfileRepository;
import com.spring.messenger.security.model.User;
import com.spring.messenger.security.repository.UserRepository;
import com.spring.messenger.service.FileStorage;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/profile")
public class UserProfileController {
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private FileStorage fileStorage;

    @PostMapping
    public UserProfile saveUserProfile(@RequestBody UserProfile userProfile, Principal principal) throws Exception {
        userProfile.setUser(getUser(principal));
        return userProfileRepository.save(userProfile);
    }

    @PostMapping(value = "/file")
    public UserProfile saveUserProfileWithImage(@ModelAttribute UserProfileDTO userProfileDTO, @AuthenticationPrincipal Principal principal) throws Exception {
        String path = "http://localhost:8080/profile/files/";
        fileStorage.saveFile(userProfileDTO.getFile());
        UserProfile userProfile = new UserProfile();
        userProfile.setAddress1(userProfileDTO.getAddress1());
        userProfile.setAddress2(userProfileDTO.getAddress2());
        userProfile.setCity(userProfileDTO.getCity());
        userProfile.setCountry(userProfileDTO.getCountry());
        userProfile.setState(userProfileDTO.getState());
        userProfile.setStreet(userProfileDTO.getStreet());
        userProfile.setPhoneNumber(userProfileDTO.getPhoneNumber());
        userProfile.setZipCode(userProfileDTO.getZipCode());
//        userProfile.setDateOfBirth(userProfileDTO.getDateOfBirth());
        userProfile.setGender(userProfileDTO.getGender());
        userProfile.setUser(getUser(principal));
        userProfile.setImageUrl(path + userProfileDTO.getFile().getOriginalFilename());
        return userProfileRepository.save(userProfile);
    }

    @PostMapping("/files")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String message;
        fileStorage.saveFile(file);
        message = "Success. " + file.getOriginalFilename() + " uploaded successfully";
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/files/{filename:.+}")
    public Resource getFile(@PathVariable String filename) {
        return fileStorage.getFile(filename);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    @GetMapping("/my")
    public UserProfile getUserProfiles(Principal principal) throws Exception {
        User user = getUser(principal);
        return userProfileRepository.findByUser(user);
    }

    private User getUser(Principal principal) throws Exception {
        return userRepository.findByUsername(principal.getName()).orElseThrow(() -> new Exception("No such User!"));
    }
}
