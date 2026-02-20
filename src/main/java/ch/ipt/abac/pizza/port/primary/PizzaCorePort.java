package ch.ipt.abac.pizza.port.primary;

import ch.ipt.abac.pizza.domain.model.Pizza;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PizzaCorePort {

    Pizza createPizza(Pizza pizza);

    Pizza findPizzaById(UUID id);

    List<Pizza> findPizzaByName(String name);

    List<Pizza> findAllPizzas();

}
