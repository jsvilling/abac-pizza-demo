package ch.ipt.abac.pizza;

import ch.ipt.abac.pizza.abac.api.model.Pizza;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static io.restassured.http.Method.GET;
import static io.restassured.http.Method.POST;

@ApiSecurityTestConfig
class RBACTest {

    private static final String BASE_URL = "/pizzas";
    private static final String SALAMI_URL = "/pizzas/salami";

    @Autowired
    ApiTestService testRequest;

    @ParameterizedTest(name = "{0}")
    @MethodSource("validTestRequests")
    void testValidRequests(ApiTestService.TestRequestConfig cfg) {
        testRequest.testUnauthorizedRequest(cfg);
        testRequest.testForbiddenRequestInvalidRole(cfg);
        testRequest.testValidRequest(cfg);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("forbiddenTestRequests")
    void testForbiddenRequests(ApiTestService.TestRequestConfig cfg) {
        testRequest.testUnauthorizedRequest(cfg);
        testRequest.testForbiddenRequestInvalidRole(cfg);
        testRequest.testForbiddenRequest(cfg);
    }

    private static Stream<ApiTestService.TestRequestConfig> validTestRequests() {

        Pizza pizza = new Pizza();

        return Stream.of(
                new ApiTestService.TestRequestConfig(GET, BASE_URL, "ROLE_CHEF"),
                new ApiTestService.TestRequestConfig(GET, BASE_URL, "ROLE_CUSTOMER"),
                new ApiTestService.TestRequestConfig(GET, BASE_URL, "ROLE_SALAMI_CHEF"),
                new ApiTestService.TestRequestConfig(GET, BASE_URL, "ROLE_SALAMI_CUSTOMER"),
                new ApiTestService.TestRequestConfig(GET, SALAMI_URL, "ROLE_CHEF"),
                new ApiTestService.TestRequestConfig(GET, SALAMI_URL, "ROLE_CUSTOMER"),
                new ApiTestService.TestRequestConfig(GET, SALAMI_URL, "ROLE_SALAMI_CHEF"),
                new ApiTestService.TestRequestConfig(GET, SALAMI_URL, "ROLE_SALAMI_CUSTOMER"),
                new ApiTestService.TestRequestConfig(POST, BASE_URL, pizza, "ROLE_CHEF"),
                new ApiTestService.TestRequestConfig(POST, BASE_URL, pizza, "ROLE_SALAMI_CHEF")
        );
    }

    private static Stream<ApiTestService.TestRequestConfig> forbiddenTestRequests() {

        Pizza pizza = new Pizza();

        return Stream.of(
                new ApiTestService.TestRequestConfig(POST, BASE_URL, pizza, "ROLE_CUSTOMER"),
                new ApiTestService.TestRequestConfig(POST, BASE_URL, pizza, "ROLE_SALAMI_CUSTOMER")
        );
    }
}