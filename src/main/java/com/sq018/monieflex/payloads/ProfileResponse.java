package com.sq018.monieflex.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sq018.monieflex.enums.AccountStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
    private Long id;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
    @JsonProperty(value = "email_address")
    private String emailAddress;
    @JsonProperty(value = "profile_picture")
    private String profilePicture;
    @JsonProperty(value = "phone_number")
    private String phoneNumber;
    @JsonProperty(value = "account_status")
    private AccountStatus status;
}
