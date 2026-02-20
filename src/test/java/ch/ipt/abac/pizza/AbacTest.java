package ch.ipt.abac.pizza;

import ch.ipt.abac.pizza.abac.api.model.Pizza;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static ch.ipt.abac.pizza.AbacAssertions.*;
import static ch.ipt.abac.pizza.abac.PizzaRole.*;
import static io.restassured.http.Method.GET;
import static io.restassured.http.Method.POST;

@ApiSecurityTestConfig
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbacTest {

    private static final ApiTestService.AbacTestUser CHEF = new ApiTestService.AbacTestUser("1", List.of(ROLE_CHEF.name()));
    private static final ApiTestService.AbacTestUser CUSTOMER = new ApiTestService.AbacTestUser("1", List.of(ROLE_CUSTOMER.name()));
    private static final ApiTestService.AbacTestUser SALAMI_CUSTOMER = new ApiTestService.AbacTestUser("1", List.of(ROLE_SALAMI_CUSTOMER.name()));

    private static final String BASE_URL = "/pizzas";
    private static final String SEARCH_SALAMI_URL = "/pizzas?name=salami";

    @Autowired
    ApiTestService testRequest;

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("testReadCompanyDetailsSrc")
    void testSomething(ApiTestService.AbacTestConfig testCfg) {
        testRequest.testRequest(testCfg);
    }

    private static Stream<ApiTestService.AbacTestConfig> testReadCompanyDetailsSrc() {
        Pizza pizza = new Pizza().name("Salami");
        return Stream.of(
                // Get all
                new ApiTestService.AbacTestConfig(GET, BASE_URL, CHEF, hasAllKindsOfPizza()),
                new ApiTestService.AbacTestConfig(GET, BASE_URL, CUSTOMER, hasAllKindsOfPizza()),
                new ApiTestService.AbacTestConfig(GET, BASE_URL, SALAMI_CUSTOMER, hasOnlySalami()),

                // Get salami search
                new ApiTestService.AbacTestConfig(GET, SEARCH_SALAMI_URL, CHEF, hasOnlySalami()),
                new ApiTestService.AbacTestConfig(GET, SEARCH_SALAMI_URL, CUSTOMER, hasOnlySalami()),
                new ApiTestService.AbacTestConfig(GET, SEARCH_SALAMI_URL, SALAMI_CUSTOMER, hasOnlySalami()),

                // Create
                new ApiTestService.AbacTestConfig(POST, BASE_URL, pizza, CHEF, isOk()),
                new ApiTestService.AbacTestConfig(POST, BASE_URL, pizza, CUSTOMER, isForbidden()),
                new ApiTestService.AbacTestConfig(POST, BASE_URL, pizza, SALAMI_CUSTOMER, isForbidden())
        );
    }
}
