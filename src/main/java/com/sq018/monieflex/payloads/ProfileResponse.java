package com.sq018.monieflex.payloads;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sq018.monieflex.enums.AccountStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ProfileResponse {
    private String message;
    private HttpStatus status;
    @JsonProperty(value = "status_code")
    private Integer statusCode;
    @JsonProperty(value = "data")
    private ProfileData data;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProfileData{
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

}
