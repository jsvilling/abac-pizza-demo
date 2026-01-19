package ch.ipt.abac.pizza.port.primary;

import ch.ipt.abac.pizza.domain.model.Pizza;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PizzaCorePort {

    Pizza createPizza(Pizza pizza);

    Pizza findPizzaByName(String name);

    List<Pizza> findAllPizzas();

}
