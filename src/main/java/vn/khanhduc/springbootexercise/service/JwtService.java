package vn.khanhduc.springbootexercise.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import vn.khanhduc.springbootexercise.entity.Account;
import vn.khanhduc.springbootexercise.entity.InvalidToken;
import vn.khanhduc.springbootexercise.repository.InvalidTokenRepository;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final InvalidTokenRepository invalidTokenRepository;

    public String generateAccessToken(Account account) {
        Collection<? extends GrantedAuthority> authorities = account.getAuthorities(); // role : {USER} ==> {USER, ADMIN}
        List<String> authorityName = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // 1. Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS384);
        // 2. Payload
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(account.getId()))
                .issueTime(new Date())
                .claim("authorities", authorityName)
                .expirationTime(new Date(Instant.now().plus(30, ChronoUnit.MINUTES).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        // 3. Chữ kí
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateRefreshToken(Account account) {
        // 1. Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        // 2. Payload
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        var payload = new Payload(claimsSet.toJSONObject());
        // 3. Chữ kí
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyToken(String token) throws ParseException, JOSEException {
        if(StringUtils.isBlank(token)) {
            return false;
        }
        SignedJWT signedJWT = SignedJWT.parse(token);

        if(signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
            return false;
        }

        Optional<InvalidToken> invalidToken = invalidTokenRepository.findById(signedJWT.getJWTClaimsSet().getJWTID());

        if(invalidToken.isPresent()) {
            return false;
        }
        return signedJWT.verify(new MACVerifier(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

}
