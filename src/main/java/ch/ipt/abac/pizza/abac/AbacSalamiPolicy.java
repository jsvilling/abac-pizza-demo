package ch.ipt.abac.pizza.abac;

import ch.ipt.abac.pizza.abac.api.model.Order;
import ch.ipt.abac.pizza.abac.api.model.Pizza;
import ch.ipt.abac.pizza.adapter.secondary.persistence.QPizzaEntity;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AbacSalamiPolicy implements AbacPolicy{

    @Override
    public List<PizzaRole> relevantRoles() {
        return List.of(PizzaRole.ROLE_SALAMI_CUSTOMER, PizzaRole.ROLE_SALAMI_ENTHUSIAST);
    }

    @Override
    public JPAQuery<Pizza> applyPizza(JPAQuery<Pizza> query) {
        final QPizzaEntity qPizza = QPizzaEntity.pizzaEntity;
        final var query2 = query.where(qPizza.name.equalsIgnoreCase("salami"));
        return query2;
    }

    @Override
    public JPAQuery<Order> applyOrder(JPAQuery<Order> query) {
        // This policy is not applicable for Orders
        return query;
    }

}
