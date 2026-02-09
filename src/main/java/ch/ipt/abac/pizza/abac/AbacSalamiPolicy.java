package ch.ipt.abac.pizza.abac;

import ch.ipt.abac.pizza.adapter.secondary.persistence.QPizzaEntity;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AbacSalamiPolicy implements AbacPolicy{

    @Override
    public List<PizzaRole> relevantRoles() {
        return List.of();
    }

    public <T> JPAQuery<T> apply(JPAQuery<T> query) {
        final QPizzaEntity qPizza = QPizzaEntity.pizzaEntity;
        return query.where(qPizza.name.eq("Salami"));
    }

}
