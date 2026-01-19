package ch.ipt.abac.pizza.adapter.primary;

import ch.ipt.abac.pizza.adapter.primary.mapper.PizzaMapper;
import ch.ipt.abac.pizza.domain.service.PizzaService;
import ch.ipt.abac.pizza.port.primary.PizzaCorePort;
import com.example.api.PizzasApi;
import com.example.model.Pizza;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class PizzaApiAdapter implements PizzasApi {

    private final PizzaCorePort pizzaCorePort;
    private final PizzaMapper pizzaMapper;

    @Override
    public ResponseEntity<Pizza> createPizza() {
        final var pizza = pizzaCorePort.createPizza(null);
        final var pizzaDto = pizzaMapper.map(pizza);
        return ResponseEntity.ok(pizzaDto);
    }

    @Override
    public ResponseEntity<Pizza> findPizzaByName(String name) {
        final var pizza = pizzaCorePort.findPizzaByName(name);
        final var pizzaDto = pizzaMapper.map(pizza);
        return ResponseEntity.ok(pizzaDto);
    }

    @Override
    public ResponseEntity<List<Pizza>> getAllPizzas() {
        final var pizzas = pizzaCorePort.findAllPizzas();
        final var pizzaDtos = pizzaMapper.map(pizzas);
        return ResponseEntity.ok(pizzaDtos);
    }

}
