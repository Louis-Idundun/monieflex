package com.sq018.monieflex.services;

import com.sq018.monieflex.dtos.ChangePasswordDto;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.ProfileResponse;
import com.sq018.monieflex.repositories.UserRepository;
import com.sq018.monieflex.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    public ResponseEntity<ApiResponse<String>> changePassword(ChangePasswordDto changePasswordDto) {
        var email = UserUtil.getLoginUser();
        var user = userRepository.findByEmailAddress(email);
        if(user.isPresent()) {
            if(passwordEncoder.matches(changePasswordDto.oldPassword(), user.get().getEncryptedPassword())){
                if(changePasswordDto.newPassword().equals(changePasswordDto.confirmPassword())) {
                    user.get().setEncryptedPassword(passwordEncoder.encode(changePasswordDto.newPassword()));
                    userRepository.save(user.get());

                    ApiResponse<String> response = new ApiResponse<>(
                            "New password",
                            "Password successfully changed"
                    );
                    return new ResponseEntity<>(response, response.getStatus());
                } else {
                    throw new MonieFlexException("Password does not match");
                }
            } else {
                throw new MonieFlexException("Incorrect password");
            }
        } else {
            throw new MonieFlexException("User not found");
        }
    }
}
