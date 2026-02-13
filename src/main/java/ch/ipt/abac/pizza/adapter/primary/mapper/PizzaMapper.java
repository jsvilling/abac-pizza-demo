package ch.ipt.abac.pizza.adapter.primary.mapper;

import ch.ipt.abac.pizza.domain.model.Pizza;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PizzaMapper {

    Pizza map(ch.ipt.abac.pizza.abac.api.model.Pizza pizza);

    ch.ipt.abac.pizza.abac.api.model.Pizza map(Pizza pizza);

    List<ch.ipt.abac.pizza.abac.api.model.Pizza> map(List<Pizza> pizza);

}
