package ch.ipt.abac.pizza.auth;

import ch.ipt.abac.pizza.auth.scaffold.ApiSecurityTestConfig;
import ch.ipt.abac.pizza.abac.api.model.Order;
import ch.ipt.abac.pizza.abac.api.model.Pizza;
import ch.ipt.abac.pizza.auth.scaffold.ApiTestService;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static ch.ipt.abac.pizza.abac.PizzaRole.*;
import static io.restassured.http.Method.*;
import static ch.ipt.abac.pizza.auth.scaffold.AbacAssertions.*;

@ApiSecurityTestConfig
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbacTest {

    private static final ApiTestService.AbacTestUser CHEF = new ApiTestService.AbacTestUser("1", List.of(ROLE_CHEF.name()));
    private static final ApiTestService.AbacTestUser CUSTOMER = new ApiTestService.AbacTestUser("1", List.of(ROLE_CUSTOMER.name()));
    private static final ApiTestService.AbacTestUser SALAMI_CUSTOMER = new ApiTestService.AbacTestUser("1", List.of(ROLE_SALAMI_CUSTOMER.name()));
    private static final ApiTestService.AbacTestUser SALAMI_ENTHUSIAST = new ApiTestService.AbacTestUser("1", List.of(ROLE_SALAMI_ENTHUSIAST.name()));

    private static final String BASE_PIZZA_URL = "/pizzas";
    private static final String SEARCH_SALAMI_URL = "/pizzas?name=salami";

    private static final String BASE_ORDER_URL = "/orders";
    private static final String ID_ORDER_URL = "/orders/";

    private static final String USER_1_ORDER_ID = "11111111-1111-1111-1111-111111111111";
    private static final String USER_2_ORDER_ID = "22222222-2222-2222-2222-222222222222";

    @Autowired
    ApiTestService testRequest;

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("testReadCompanyDetailsSrc")
    void testSomething(ApiTestService.AbacTestConfig testCfg) {
        testRequest.testRequest(testCfg);
    }

    private static Stream<ApiTestService.AbacTestConfig> testReadCompanyDetailsSrc() {
        Pizza pizza = new Pizza().name("Salami");
        Order order = new Order().userId("1").pizzaName("Salami").pizza(pizza);

        return Stream.of(
                // ----- Pizza Tests -----
                // Get all
                new ApiTestService.AbacTestConfig(GET, BASE_PIZZA_URL, CHEF, hasAllKindsOfPizza()),
                new ApiTestService.AbacTestConfig(GET, BASE_PIZZA_URL, CUSTOMER, hasOnlySalami()),
                new ApiTestService.AbacTestConfig(GET, BASE_PIZZA_URL, SALAMI_CUSTOMER, hasOnlySalami()),
                new ApiTestService.AbacTestConfig(GET, BASE_PIZZA_URL, SALAMI_ENTHUSIAST, hasOnlySalami()),

                // Get salami search
                new ApiTestService.AbacTestConfig(GET, SEARCH_SALAMI_URL, CHEF, hasOnlySalami()),
                new ApiTestService.AbacTestConfig(GET, SEARCH_SALAMI_URL, CUSTOMER, hasOnlySalami()),
                new ApiTestService.AbacTestConfig(GET, SEARCH_SALAMI_URL, SALAMI_CUSTOMER, hasOnlySalami()),
                new ApiTestService.AbacTestConfig(GET, SEARCH_SALAMI_URL, SALAMI_ENTHUSIAST, hasOnlySalami()),

                // Create pizza
                new ApiTestService.AbacTestConfig(POST, BASE_PIZZA_URL, pizza, CHEF, isOk()),
                new ApiTestService.AbacTestConfig(POST, BASE_PIZZA_URL, pizza, CUSTOMER, isForbidden()),
                new ApiTestService.AbacTestConfig(POST, BASE_PIZZA_URL, pizza, SALAMI_CUSTOMER, isForbidden()),
                new ApiTestService.AbacTestConfig(POST, BASE_PIZZA_URL, pizza, SALAMI_ENTHUSIAST, isForbidden()),

                // ----- Order Tests -----
                // Get all orders
                new ApiTestService.AbacTestConfig(GET, BASE_ORDER_URL, CHEF, hasUserIds("1", "2", "3", "4", "5")),
                new ApiTestService.AbacTestConfig(GET, BASE_ORDER_URL, CUSTOMER, hasOnlyUserId("1")),
                new ApiTestService.AbacTestConfig(GET, BASE_ORDER_URL, SALAMI_CUSTOMER, hasOnlyUserId("1")),
                new ApiTestService.AbacTestConfig(GET, BASE_ORDER_URL, SALAMI_ENTHUSIAST, isForbidden()),

                // Get order by Id
                new ApiTestService.AbacTestConfig(GET, ID_ORDER_URL  + USER_1_ORDER_ID, order, CHEF, isOk()),
                new ApiTestService.AbacTestConfig(GET, ID_ORDER_URL  + USER_2_ORDER_ID, order, CHEF, isOk()),
                new ApiTestService.AbacTestConfig(GET, ID_ORDER_URL  + USER_1_ORDER_ID, order, CUSTOMER, isOk()),
                new ApiTestService.AbacTestConfig(GET, ID_ORDER_URL  + USER_2_ORDER_ID, order, CUSTOMER, isNotFound()),

                // Create order
                new ApiTestService.AbacTestConfig(POST, BASE_ORDER_URL, order, CHEF, isForbidden()),
                new ApiTestService.AbacTestConfig(POST, BASE_ORDER_URL, order, CUSTOMER, isOk()),
                new ApiTestService.AbacTestConfig(POST, BASE_ORDER_URL, order, SALAMI_CUSTOMER, isOk()),

                // Update order
                new ApiTestService.AbacTestConfig(PUT, ID_ORDER_URL  + USER_1_ORDER_ID, order, CHEF, isForbidden()),
                new ApiTestService.AbacTestConfig(PUT, ID_ORDER_URL  + USER_2_ORDER_ID, order, CHEF, isForbidden()),
                new ApiTestService.AbacTestConfig(PUT, ID_ORDER_URL  + USER_1_ORDER_ID, order, CUSTOMER, isOk()),
                new ApiTestService.AbacTestConfig(PUT, ID_ORDER_URL  + USER_2_ORDER_ID, order, CUSTOMER, isNotFound()),

                // Delete order
                new ApiTestService.AbacTestConfig(DELETE, ID_ORDER_URL  + USER_1_ORDER_ID, order, CHEF, isForbidden()),
                new ApiTestService.AbacTestConfig(DELETE, ID_ORDER_URL  + USER_2_ORDER_ID, order, CHEF, isForbidden()),
                new ApiTestService.AbacTestConfig(DELETE, ID_ORDER_URL  + USER_1_ORDER_ID, order, CUSTOMER, isNoContent()),
                new ApiTestService.AbacTestConfig(DELETE, ID_ORDER_URL  + USER_2_ORDER_ID, order, CUSTOMER, isNotFound())
        );
    }
}