package net.dfranek.library.rest.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import net.dfranek.library.rest.config.JwtConfig;
import net.dfranek.library.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class SecurityHelper {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserRepository userRepository;

    public static final String AUTH_LOGIN_URL = "/login";

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    public String getUserNameFromAuthHeader(final String authHeader) {
        Jws<Claims> parsedToken = Jwts.parser()
                .setSigningKey(getSecret().getBytes())
                .parseClaimsJws(authHeader.replace("Bearer ", ""));

        return parsedToken
                .getBody()
                .getSubject();
    }

    public String getSecret() {
        return jwtConfig.getSecret();
    }

    public boolean isUserExisting(final String username) {
        return userRepository.findByEmail(username) != null;
    }

}
