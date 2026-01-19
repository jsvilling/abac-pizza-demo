package ch.ipt.abac.pizza.adapter.primary.mapper;

import ch.ipt.abac.pizza.domain.model.Pizza;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PizzaMapper {

    Pizza map(com.example.model.Pizza pizza);

    com.example.model.Pizza map(Pizza pizza);

    List<com.example.model.Pizza> map(List<Pizza> pizza);

}
