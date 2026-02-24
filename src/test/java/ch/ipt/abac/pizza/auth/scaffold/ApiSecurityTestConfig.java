package ch.ipt.abac.pizza.auth.scaffold;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "server.port=8889",
                // Configure jws issuer here
                "logging.level.org= INFO",
        }
)
@ActiveProfiles("test-security")
public @interface ApiSecurityTestConfig {
}