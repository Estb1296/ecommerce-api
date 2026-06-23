package org.yearup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.exception.DataAccessException;
import org.yearup.exception.InvalidInputException;
import org.yearup.exception.ResourceNotFoundException;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

import java.util.List;

@Service
public class ProfileService
{
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository)
    {
        this.profileRepository = profileRepository;
    }
    public Profile getByUserId(int userId){
        if (userId <= 0) {
            throw new InvalidInputException("User ID must be a positive number");
        }
        return profileRepository.findByUserId(userId);
    }

    public Profile create(Profile profile)
    {
        if (profile == null) {
            throw new InvalidInputException("Profile data cannot be null");
        }

        //Validate essential business fields (e.g., First Name, Last Name)
        if (profile.getFirstName() == null || profile.getFirstName().isBlank()) {
            throw new InvalidInputException("First name is required");
        }
        if (profile.getLastName() == null || profile.getLastName().isBlank()) {
            throw new InvalidInputException("Last name is required");
        }

        try {
            //Persist safely
            return profileRepository.save(profile);
        } catch (Exception e) {
            //Translate raw SQL exceptions into your custom exceptions
            throw new DataAccessException("Failed to create user profile", e);
        }
    }

    public Profile update(int userId, Profile profile) {


        if (profile == null) {

            throw new InvalidInputException("Profile data cannot be null");

        }
        //Validate essential business fields (e.g., First Name, Last Name)

        if (profile.getFirstName() == null || profile.getFirstName().isBlank()) {

            throw new InvalidInputException("First name is required");

        }

        if (profile.getLastName() == null || profile.getLastName().isBlank()) {

            throw new InvalidInputException("Last name is required");

        }



        try {

            // 🌟 THE FIX: Explicitly bind the authenticated userId to the profile entity.

            // This forces JPA to perform an UPDATE on this specific user's row instead of an INSERT.

            profile.setUserId(userId);



            // Persist safely

            return profileRepository.save(profile);

        } catch (Exception e) {

            // Translate raw SQL exceptions into your custom exceptions

            throw new DataAccessException("Failed to update user profile", e);

        }

    }

}

