package ch.ipt.abac.pizza.adapter.secondary.persistence;

import ch.ipt.abac.pizza.domain.model.Pizza;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PizzaEntityMapper {
    PizzaEntity map(Pizza pizza);
    Pizza map(PizzaEntity pizzaEntity);
}
