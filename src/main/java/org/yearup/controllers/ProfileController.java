package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.yearup.exception.InvalidInputException;
import org.yearup.exception.ResourceNotFoundException;
import org.yearup.models.Profile;

import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    public ProfileController (ProfileService profileService,UserService userService){

        this.profileService=profileService;
        this.userService = userService;
    }
    private int getUserIdFromPrincipal(Principal principal) {
        if (principal==null){
            throw new InvalidInputException("Authentication required");
        }
        User user = userService.getByUserName(principal.getName());
        if (user==null) {
            throw new ResourceNotFoundException("User not found");
        }
        return user.getId();
    }
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Profile>getProfile(Principal principal){
        int userId  = getUserIdFromPrincipal(principal);
        Profile profile = profileService.getByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Profile> updateProfile(Principal principal, @RequestBody Profile profile) {
        // 1. Get the authenticated user's ID
        String userName = principal.getName();
        User user = userService.getByUserName(userName);

        if (user == null) {
            throw new ResourceNotFoundException("User not found matching session token");
        }

        int userId = user.getId();
        // 2. Delegate the updating business logic down to your profile service
        Profile updatedProfile = profileService.update(userId, profile);
        // 3. Return the updated profile with a clean 200 OK
        return ResponseEntity.ok(updatedProfile);
    }
}
