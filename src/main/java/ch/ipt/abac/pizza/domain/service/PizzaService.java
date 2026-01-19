package ch.ipt.abac.pizza.domain.service;

import ch.ipt.abac.pizza.domain.model.Pizza;
import ch.ipt.abac.pizza.port.primary.PizzaCorePort;
import ch.ipt.abac.pizza.port.secondary.PizzaPersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PizzaService implements PizzaCorePort {

    private final PizzaPersistencePort pizzaPersistencePort;

    @Override
    public Pizza createPizza(Pizza pizza) {
        return pizzaPersistencePort.createPizza(pizza);
    }

    @Override
    public Pizza findPizzaByName(String name) {
        return pizzaPersistencePort.findByName(name).orElseThrow();
    }

    @Override
    public List<Pizza> findAllPizzas() {
        return pizzaPersistencePort.findAllPizzas();
    }
}
