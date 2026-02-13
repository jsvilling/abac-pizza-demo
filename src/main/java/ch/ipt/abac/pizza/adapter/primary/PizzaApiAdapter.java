package ch.ipt.abac.pizza.adapter.primary;

import ch.ipt.abac.pizza.adapter.primary.mapper.PizzaMapper;
import ch.ipt.abac.pizza.port.primary.PizzaCorePort;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import ch.ipt.abac.pizza.abac.api.PizzasApi;
import ch.ipt.abac.pizza.abac.api.model.Pizza;
import java.util.List;

@AllArgsConstructor
@RestController
public class PizzaApiAdapter implements PizzasApi {

    private final PizzaCorePort pizzaCorePort;
    private final PizzaMapper pizzaMapper;

    @Override
    @PreAuthorize("hasAuthority('demo_pizza_create')")
    public ResponseEntity<Pizza> createPizza(Pizza pizzaDto) {
        var requestedPizza = pizzaMapper.map(pizzaDto);
        var createdPizza = pizzaCorePort.createPizza(requestedPizza);
        var pizzaResponseDto = pizzaMapper.map(createdPizza);
        return ResponseEntity.ok(pizzaResponseDto);
    }

    @Override
    @PreAuthorize("hasAuthority('demo_pizza_read')")
    public ResponseEntity<Pizza> findPizzaByName(String name) {
        final var pizza = pizzaCorePort.findPizzaByName(name);
        final var pizzaDto = pizzaMapper.map(pizza);
        return ResponseEntity.ok(pizzaDto);
    }

    @Override
    @PreAuthorize("hasAuthority('demo_pizza_read')")
    public ResponseEntity<List<Pizza>> getAllPizzas() {
        final var pizzas = pizzaCorePort.findAllPizzas();
        final var pizzaDtos = pizzaMapper.map(pizzas);
        return ResponseEntity.ok(pizzaDtos);
    }

}
