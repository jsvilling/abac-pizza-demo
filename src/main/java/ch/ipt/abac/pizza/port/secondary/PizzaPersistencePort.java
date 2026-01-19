package ch.ipt.abac.pizza.port.secondary;

import ch.ipt.abac.pizza.domain.model.Pizza;

import java.util.List;
import java.util.Optional;

public interface PizzaPersistencePort {

    Pizza createPizza(Pizza pizza);

    Optional<Pizza> findByName(String name);

    List<Pizza> findAllPizzas();

}
