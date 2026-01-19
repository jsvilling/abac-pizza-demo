package ch.ipt.abac.pizza.adapter.secondary;

import ch.ipt.abac.pizza.adapter.secondary.persistence.PizzaEntityMapper;
import ch.ipt.abac.pizza.adapter.secondary.persistence.PizzaPostgresAdapter;
import ch.ipt.abac.pizza.domain.model.Pizza;
import ch.ipt.abac.pizza.port.secondary.PizzaPersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PizzaPersistenceAdapter implements PizzaPersistencePort {

    private final PizzaPostgresAdapter pizzaRepository;
    private final PizzaEntityMapper pizzaMapper;

    @Override
    public Pizza createPizza(Pizza pizza) {
        final var entity = pizzaMapper.map(pizza);
        final var persistedEntity = pizzaRepository.save(entity);
        return pizzaMapper.map(persistedEntity);
    }

    @Override
    public Optional<Pizza> findByName(String name) {
        return pizzaRepository.findByName(name).map(pizzaMapper::map);
    }

    @Override
    public List<Pizza> findAllPizzas() {
        return pizzaRepository.findAll().stream().map(pizzaMapper::map).toList();
    }
}
