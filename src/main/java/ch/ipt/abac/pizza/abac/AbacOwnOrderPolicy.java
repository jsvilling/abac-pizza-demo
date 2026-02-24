package ch.ipt.abac.pizza.abac;

import ch.ipt.abac.pizza.abac.api.model.Order;
import ch.ipt.abac.pizza.abac.api.model.Pizza;
import ch.ipt.abac.pizza.adapter.secondary.SecurityContextAdapter;
import ch.ipt.abac.pizza.adapter.secondary.persistence.QOrderEntity;
import ch.ipt.abac.pizza.adapter.secondary.persistence.QPizzaEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class AbacOwnOrderPolicy implements AbacPolicy {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PizzaRole> relevantRoles() {
        return List.of(PizzaRole.ROLE_SALAMI_CUSTOMER, PizzaRole.ROLE_CUSTOMER);
    }

    @Override
    public JPAQuery<Pizza> applyPizza(JPAQuery<Pizza> query) {
        final var currentUserId = SecurityContextAdapter.getCurrentUserId();
        final var qPizza = QPizzaEntity.pizzaEntity;
        final var qOrder = QOrderEntity.orderEntity;

        final var pizzaIdSubquery = queryFactory
                .select(qOrder.pizzaId)
                .from(qOrder)
                .where(qOrder.userId.eq(currentUserId));

        return query.where(qPizza.id.in(pizzaIdSubquery));
    }

    @Override
    public JPAQuery<Order> applyOrder(JPAQuery<Order> query) {
        final var currentUserId = SecurityContextAdapter.getCurrentUserId();
        final var qOrder = QOrderEntity.orderEntity;
        return query.where(qOrder.userId.equalsIgnoreCase(currentUserId));
    }

}
