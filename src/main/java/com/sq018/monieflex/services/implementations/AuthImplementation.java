package com.sq018.monieflex.services.implementations;

import com.sq018.monieflex.dtos.LoginDto;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.repositories.ConfirmationTokenRepository;
import com.sq018.monieflex.repositories.UserRepository;
import com.sq018.monieflex.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthImplementation implements AuthService {
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtImplementation jwtImplementation;

    @Override
    public ResponseEntity<ApiResponse<String>> login(LoginDto loginDto){
        var user = userRepository.findByEmailAddress(loginDto.emailAddress());
        if(user.isPresent()) {
            var confirmUser = confirmationTokenRepository.findByUser_EmailAddress(user.get().getEmailAddress());
            if(confirmUser.isPresent() && confirmUser.get().getConfirmedAt() == null) {
                /// TODO:: Generate and send OTP verify token
                throw new MonieFlexException("Account has not been activated");
            } else {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDto.emailAddress(), loginDto.password())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                Map<String, Object> claims = new HashMap<>();
                claims.put("first_name", user.get().getFirstName());
                claims.put("last_name", user.get().getLastName());
                String token = jwtImplementation.generateJwtToken(claims, loginDto.emailAddress());

                ApiResponse<String> response = new ApiResponse<>(token, "Login successful");
                return new ResponseEntity<>(response, response.getStatus());
            }
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
