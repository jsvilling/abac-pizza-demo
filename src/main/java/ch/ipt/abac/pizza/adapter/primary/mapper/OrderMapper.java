package ch.ipt.abac.pizza.adapter.primary.mapper;

import ch.ipt.abac.pizza.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order map(ch.ipt.abac.pizza.abac.api.model.Order order);

    ch.ipt.abac.pizza.abac.api.model.Order map(Order pizza);

    List<ch.ipt.abac.pizza.abac.api.model.Order> map(List<Order> pizza);

}
