package ch.ipt.abac.pizza;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@SpringBootTest
class GenerateTokenTest {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

	@Test
	@SneakyThrows
    void contextLoads() {
        final byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        final var secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");

        Instant now = Instant.now();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("demo-user")
                .claim("name", "Demo User")
                .claim("roles", List.of("ROLE_CHEF"))
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(3600)))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.HS256)
                        .type(JOSEObjectType.JWT)
                        .build(),
                claims
        );

        signedJWT.sign(new MACSigner(secretKey.getEncoded()));
        final var jwt = signedJWT.serialize();
        System.out.println(jwt);
	}

}
