package ch.ipt.abac.pizza;

import ch.ipt.abac.pizza.abac.api.model.Pizza;
import io.restassured.response.ValidatableResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AbacAssertions {

    public static Consumer<ValidatableResponse> isOk() {
        return (ValidatableResponse response) -> response.statusCode(200);
    }

    public static Consumer<ValidatableResponse> isCreated() {
        return (ValidatableResponse response) -> response.statusCode(201);
    }

    public static Consumer<ValidatableResponse> isNoContent() {
        return (ValidatableResponse response) -> response.statusCode(204);
    }

    public static Consumer<ValidatableResponse> isForbidden() {
        return (ValidatableResponse response) -> response.statusCode(403);
    }

    public static Consumer<ValidatableResponse> isNotFound() {
        return (ValidatableResponse response) -> response.statusCode(404);
    }

    public static Consumer<ValidatableResponse> isEmpty() {
        return hasSize(0);
    }

    public static Consumer<ValidatableResponse> hasSize(int n) {
        return (ValidatableResponse response) -> {
            List<?> list = response.statusCode(200).extract().body().as(List.class);
            assertThat(list).hasSize(n);
        };
    }

    public static <T> Consumer<ValidatableResponse> hasContent(Class<T> responseClass) {
        return (ValidatableResponse response) -> {
            T actual = response.statusCode(200).extract().as(responseClass);
            assertThat(actual).isNotNull();
        };
    }

    public static Consumer<ValidatableResponse> hasOnlySalami() {
        return (ValidatableResponse response) -> {
            List<Pizza> actual = response.statusCode(200).extract().jsonPath().getList("", Pizza.class);
            assertThat(actual).extracting(Pizza::getName).allMatch("Salami"::equalsIgnoreCase);
        };
    }

    public static Consumer<ValidatableResponse> hasAllKindsOfPizza() {
        return (ValidatableResponse response) -> {
            List<Pizza> actual = response.statusCode(200).extract().jsonPath().getList("", Pizza.class);
            assertThat(actual).extracting(Pizza::getName).contains("Salami", "Margherita", "Hawaiian", "Vegetarian", "Pepperoni");
        };
    }

    private static <R> Consumer<ValidatableResponse> isUUIDList(Class<R> clazz, Function<R, UUID> extractor, String[] expected) {
        var ids = Arrays.stream(expected).map(UUID::fromString).toArray(UUID[]::new);
        return isListWith(clazz, extractor, ids);
    }

    private static <R, T> Consumer<ValidatableResponse> isListWith(Class<R> clazz, Function<R, T> extractor, T[] expected) {
        return (ValidatableResponse response) -> {
            var content = response.extract().jsonPath().getList("", clazz);
            assertThat(content).hasSize(expected.length).extracting(extractor).containsExactlyInAnyOrder(expected);
        };
    }

    private static <W, I> Consumer<ValidatableResponse> isWrappedUUIDListWith(Class<W> wClazz, Function<W, List<I>> unwrap, Function<I, UUID> extract, String... expected) {
        UUID[] uuids = Arrays.stream(expected).map(UUID::fromString).toArray(UUID[]::new);
        return isWrappedListWith(wClazz, unwrap, extract, uuids);
    }

    @SafeVarargs
    private static <W, I, T> Consumer<ValidatableResponse> isWrappedListWith(Class<W> wClazz, Function<W, List<I>> unwrap, Function<I, T> extract, T... expected) {
        return (ValidatableResponse response) -> {
            W wrapper = response.statusCode(200).extract().body().as(wClazz);
            List<I> data = unwrap.apply(wrapper);
            assertThat(data).hasSize(expected.length).extracting(extract).containsExactlyInAnyOrder(expected);
        };
    }
}