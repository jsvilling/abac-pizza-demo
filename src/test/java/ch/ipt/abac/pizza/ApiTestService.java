package ch.ipt.abac.pizza;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.restassured.http.Method;
import lombok.SneakyThrows;
import lombok.With;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@Profile("test-security")
public class ApiTestService {

    public record AbacTestUser(
            String userId,
            List<String> roles
    ) {
    }

    public record AbacTestConfig(
            Method httpMethod,
            String url,
            Object body,
            AbacTestUser user,
            Consumer<ValidatableResponse> validator
    ) {
        public AbacTestConfig(Method httpMethod,
                              String url,
                              AbacTestUser user,
                              Consumer<ValidatableResponse> validator) {
            this(httpMethod, url, "", user, validator);
        }
    }

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @With
    public record TestRequestConfig(
            Method httpMethod,
            String url,
            Object body,
            String userId,
            List<String> roles
    ) {
        public TestRequestConfig(Method httpMethod, String url, String role) {
            this(httpMethod, url, "", role);
        }

        public TestRequestConfig(Method httpMethod, String url, Object body, String role) {
            this(httpMethod, url, body, null, List.of(role));
        }

        public String toString() {
            return String.format("%s %s with %s", httpMethod, url, roles);
        }
    }

    private final int serverPort;

    @Autowired
    ApiTestService(@Value("${server.port}") int serverPort) {
        this.serverPort = serverPort;
    }

    // Some string magic to deal with query parameters in the url string.
    private RequestSpecification baseSpec(TestRequestConfig cfg) {
        String[] urlParts = cfg.url().split("\\?");
        var builder = new RequestSpecBuilder().setBaseUri("http://localhost").setBasePath(urlParts[0]).setPort(serverPort);
        if (urlParts.length > 1) {
            String[] kvs = urlParts[1].split("=");
            for (int i = 0; i < kvs.length; i = i + 2) {
                builder = builder.addQueryParam(kvs[i], kvs[i + 1]);
            }
        }
        return builder.build();
    }

    public void testUnauthorizedRequest(TestRequestConfig cfg) {
        final var spec = baseSpec(cfg);
        given()
                .spec(spec)
                .when()
                .request(cfg.httpMethod)
                .then().statusCode(UNAUTHORIZED.value());
    }

    public void testForbiddenRequestInvalidRole(TestRequestConfig cfg) {
        final String authToken = createAuthTokenWithOverride(cfg, List.of("this role be invalid"));
        final var spec = baseSpec(cfg).auth().oauth2(authToken);
        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(cfg.body())
                .when()
                .request(cfg.httpMethod())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    public void testForbiddenRequest(TestRequestConfig cfg) {
        final String authToken = createAuthToken(cfg);
        final var spec = baseSpec(cfg).auth().oauth2(authToken);
        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(cfg.body())
                .when()
                .request(cfg.httpMethod())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    public ValidatableResponse testValidRequest(TestRequestConfig cfg) {
        Consumer<ValidatableResponse> hasValidAuth = (r -> r.statusCode(allOf(not(is(401)), not(is(403)))));
        return testRequest(cfg, hasValidAuth);
    }

    public ValidatableResponse testRequest(AbacTestConfig testCfg) {
        var method = testCfg.httpMethod();
        var url = testCfg.url();
        var body = testCfg.body();
        var userId = testCfg.user.userId;
        var roles = testCfg.user().roles();

        var cfg = new TestRequestConfig(method, url, body, userId, roles);
        return testRequest(cfg, testCfg.validator());
    }

    public ValidatableResponse testRequest(TestRequestConfig cfg, Consumer<ValidatableResponse> validator) {
        final String validToken = createAuthToken(cfg);
        final var spec = baseSpec(cfg).auth().oauth2(validToken);

        final var response = given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(cfg.body())
                .auth()
                .oauth2(validToken)
                .when()
                .request(cfg.httpMethod())
                .then();

        validator.accept(response);

        return response;
    }

    private String createAuthTokenWithOverride(TestRequestConfig cfg, List<String> overrideRoles) {
        var newCfg = cfg.withRoles(overrideRoles);
        return createAuthToken(newCfg);
    }

    @SneakyThrows
    private String createAuthToken(TestRequestConfig cfg) {
        final byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        final var secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");

        Instant now = Instant.now();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("demo-user")
                .claim("name", "Demo User: " + cfg.userId)
                .claim("userId", cfg.userId)
                .claim("roles", cfg.roles)
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
        return signedJWT.serialize();
    }
}