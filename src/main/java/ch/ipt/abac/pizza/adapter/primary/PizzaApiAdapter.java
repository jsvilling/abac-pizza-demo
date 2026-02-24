package ch.ipt.abac.pizza.adapter.primary;

import ch.ipt.abac.pizza.adapter.primary.mapper.PizzaMapper;
import ch.ipt.abac.pizza.port.primary.PizzaCorePort;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ch.ipt.abac.pizza.abac.api.PizzasApi;
import ch.ipt.abac.pizza.abac.api.model.Pizza;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class PizzaApiAdapter implements PizzasApi {

    private final PizzaCorePort pizzaCorePort;
    private final PizzaMapper pizzaMapper;

    @Override
    public ResponseEntity<Pizza> createPizza(Pizza pizzaDto) {
        var requestedPizza = pizzaMapper.map(pizzaDto);
        var createdPizza = pizzaCorePort.createPizza(requestedPizza);
        var pizzaResponseDto = pizzaMapper.map(createdPizza);
        return ResponseEntity.ok(pizzaResponseDto);
    }

    @Override
    public ResponseEntity<Pizza> getPizzaById(UUID id) {
        final var pizza = pizzaCorePort.findPizzaById(id);
        final var pizzaDtos = pizzaMapper.map(pizza);
        return ResponseEntity.ok(pizzaDtos);
    }

    @Override
    public ResponseEntity<List<Pizza>> getPizzas(String name) {
        if (name == null) {
            final var pizzas = pizzaCorePort.findAllPizzas();
            final var pizzaDtos = pizzaMapper.map(pizzas);
            return ResponseEntity.ok(pizzaDtos);
        }

        final var pizza = pizzaCorePort.findPizzaByName(name);
        final var pizzaDtos = pizzaMapper.map(pizza);
        return ResponseEntity.ok(pizzaDtos);
    }

}
