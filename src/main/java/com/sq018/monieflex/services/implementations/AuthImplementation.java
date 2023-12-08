package com.sq018.monieflex.services.implementations;

import com.sq018.monieflex.dtos.LoginDto;
import com.sq018.monieflex.dtos.SignupDto;
import com.sq018.monieflex.entities.account.User;
import com.sq018.monieflex.enums.AccountStatus;
import com.sq018.monieflex.exceptions.MonieFlexException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.payloads.LoginResponse;
import com.sq018.monieflex.repositories.ConfirmationTokenRepository;
import com.sq018.monieflex.repositories.UserRepository;
import com.sq018.monieflex.repositories.WalletRepository;
import com.sq018.monieflex.services.AuthService;
import com.sq018.monieflex.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final WalletService walletService;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<ApiResponse<LoginResponse>> login(LoginDto loginDto){
        var user = userRepository.findByEmailAddress(loginDto.emailAddress());
        if(user.isPresent()) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.emailAddress(), loginDto.password())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Map<String, Object> claims = new HashMap<>();
            claims.put("first_name", user.get().getFirstName());
            claims.put("last_name", user.get().getLastName());
            String token = jwtImplementation.generateJwtToken(claims, loginDto.emailAddress());

            LoginResponse loginResponse = new LoginResponse(
                    user.get().getFirstName(),
                    user.get().getLastName(),
                    user.get().getEmailAddress(),
                    token
            );
            ApiResponse<LoginResponse> response = new ApiResponse<>(loginResponse, "Login successful");
            return new ResponseEntity<>(response, response.getStatus());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Override
    public ResponseEntity<ApiResponse<String>> signup(SignupDto signupDto) {
        var user = userRepository.findByEmailAddress(signupDto.emailAddress());
        if(user.isPresent()) {
            throw new MonieFlexException("Email address already exists");
        } else {
            User newUser = new User();
            newUser.setEmailAddress(signupDto.emailAddress());
            newUser.setEncryptedPassword(passwordEncoder.encode(signupDto.password()));
            newUser.setFirstName(signupDto.firstName());
            newUser.setLastName(signupDto.lastName());
            newUser.setBvn(signupDto.bvn());
            newUser.setPhoneNumber(signupDto.phoneNumber());
            newUser.setStatus(AccountStatus.SUSPENDED);

            var wallet = walletService.create(newUser);

            userRepository.save(newUser);
            walletRepository.save(wallet);

            ApiResponse<String> response = new ApiResponse<>(
                    "Check your email for OTP verification",
                    "Successfully created account"
            );
            return new ResponseEntity<>(response, response.getStatus());
        }
    }
}
