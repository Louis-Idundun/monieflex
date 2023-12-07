package com.sq018.monieflex.services;

import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.ProfileResponse;
import com.sq018.monieflex.repositories.UserRepository;
import com.sq018.monieflex.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ApiResponse<ProfileResponse> viewProfile() {
        String email = UserUtil.getLoginUser();
        var user = userRepository.findByEmailAddress(email).orElse(null);
        if (!Objects.isNull(user)) {
            ProfileResponse profileResponse = new ProfileResponse();
            profileResponse.setId(user.getId());
            profileResponse.setFirstName(user.getFirstName());
            profileResponse.setLastName(user.getLastName());
            profileResponse.setEmailAddress(user.getEmailAddress());
            profileResponse.setProfilePicture(user.getProfilePicture());
            profileResponse.setPhoneNumber(user.getPhoneNumber());
            profileResponse.setStatus(user.getStatus());

            return new ApiResponse<>(profileResponse, "Request Processed Successfully");
        } else{
            throw new MonieFlexException("User not found");
        }
    }

}
