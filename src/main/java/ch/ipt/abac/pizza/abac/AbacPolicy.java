package ch.ipt.abac.pizza.abac;

import ch.ipt.abac.pizza.abac.api.model.Order;
import ch.ipt.abac.pizza.abac.api.model.Pizza;
import ch.ipt.abac.pizza.adapter.secondary.persistence.OrderEntity;
import ch.ipt.abac.pizza.adapter.secondary.persistence.PizzaEntity;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface AbacPolicy {

    List<PizzaRole> relevantRoles();

    default <T> JPAQuery<T> apply(JPAQuery<T> query) {
        var clazz = query.getType();

        if (OrderEntity.class.equals(clazz)) {
            return (JPAQuery<T>) applyOrder((JPAQuery<Order>) query);
        } else if (PizzaEntity.class.equals(clazz)) {
            return (JPAQuery<T>) applyPizza((JPAQuery<Pizza>) query);
        }

        return query;
    }

    JPAQuery<Pizza> applyPizza(JPAQuery<Pizza> query);

    JPAQuery<Order> applyOrder(JPAQuery<Order> query);

}
