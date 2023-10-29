package com.devamatre.appsuite.core.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rohtash Lakra
 * @created 4/9/21 5:08 PM
 */
@Getter
@AllArgsConstructor
@Setter
@Builder
public class JwtRequest {

    private String appName;
    private String appId;
    // Consumer Key
    private String clientId;
    private String clientSecret;
    private String serverBaseUrl;
    private String pathSegment;
    private String realm;
    private int expirationTimeInMinutes;
    private String redirectUrl;
    private boolean withJwtId;

}
