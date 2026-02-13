package ch.ipt.abac.pizza.abac;

import ch.ipt.abac.pizza.adapter.secondary.persistence.QPizzaEntity;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AbacSalamiPolicy implements AbacPolicy{

    @Override
    public List<PizzaRole> relevantRoles() {
        return List.of(PizzaRole.ROLE_CHEF);
    }

    public <T> JPAQuery<T> apply(JPAQuery<T> query) {
        final QPizzaEntity qPizza = QPizzaEntity.pizzaEntity;
        final var query2 = query.where(qPizza.id.eq(UUID.randomUUID())).where(qPizza.name.eq("salami"));
        return query2;
    }

}
