package ch.ipt.abac.pizza.adapter.secondary.persistence;

import ch.ipt.abac.pizza.domain.model.Order;
import ch.ipt.abac.pizza.domain.model.Pizza;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderEntityMapper {
    OrderEntity map(Order pizza);
    Order map(OrderEntity pizzaEntity);
}
