package com.sq018.monieflex.services;

import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ProfileResponse;
import com.sq018.monieflex.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<ProfileResponse> viewProfile(Long id) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            ProfileResponse response = new ProfileResponse();
            response.setMessage("You can now view your profile");
            response.setStatus(HttpStatus.OK);
            response.setStatusCode(200);

            ProfileResponse.ProfileData profileData = new ProfileResponse.ProfileData();
            profileData.setId(user.get().getId());
            profileData.setFirstName(user.get().getFirstName());
            profileData.setLastName(user.get().getLastName());
            profileData.setEmailAddress(user.get().getEmailAddress());
            profileData.setProfilePicture(user.get().getProfilePicture());
            profileData.setPhoneNumber(user.get().getPhoneNumber());
            profileData.setStatus(user.get().getStatus());
            response.setData(profileData);

            return new ResponseEntity<>(response, response.getStatus());
        } else{
            throw new MonieFlexException("User not found");
        }
    }

}
