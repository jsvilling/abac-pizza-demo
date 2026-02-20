package ch.ipt.abac.pizza.port.secondary;

import ch.ipt.abac.pizza.domain.model.Pizza;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PizzaPersistencePort {

    Pizza createPizza(Pizza pizza);

    Optional<Pizza> findById(UUID id);

    List<Pizza> findByName(String name);

    List<Pizza> findAllPizzas();

}
