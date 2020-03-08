package net.dfranek.library.rest.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.dfranek.library.rest.dto.login.Login;
import net.dfranek.library.rest.dto.login.Token;
import net.dfranek.library.rest.utils.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;
    private Login loginInformation;

    private final SecurityHelper securityHelper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityHelper securityHelper) {
        this.authenticationManager = authenticationManager;
        this.securityHelper = securityHelper;

        setFilterProcessesUrl(SecurityHelper.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            this.loginInformation = objectMapper.readValue(request.getReader(), Login.class);
        } catch (IOException e) {
            LOG.info("login information not parseable", e);
        }

        return super.attemptAuthentication(request, response);
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {

        return Optional.ofNullable(this.loginInformation)
                .map(Login::getPassword)
                .orElse(null);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return Optional.ofNullable(this.loginInformation)
                .map(Login::getUsername)
                .orElse(null);
    }

    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException {
        UserPrincipal user = ((UserPrincipal) authentication.getPrincipal());

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] signingKey = securityHelper.getSecret().getBytes();

        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityHelper.TOKEN_TYPE)
                .setIssuer(SecurityHelper.TOKEN_ISSUER)
                .setAudience(SecurityHelper.TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .claim("rol", roles)
                .compact();

        response.addHeader(SecurityHelper.TOKEN_HEADER, SecurityHelper.TOKEN_PREFIX + token);
        response.setContentType("application/json; charset=utf-8");

        Token loginToken = new Token();
        loginToken.setToken(token);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), loginToken);
    }
}
