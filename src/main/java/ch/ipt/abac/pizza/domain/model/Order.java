package ch.ipt.abac.pizza.domain.model;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {

    private final UUID id;

    @Setter
    private String userId;

    private final String pizzaName;

    @Setter
    private UUID pizzaId;

}
