package com.sq018.monieflex.payloads;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String token;
}
