package ch.ipt.abac.pizza.adapter.secondary;

import ch.ipt.abac.pizza.abac.AbacPolicyEngine;
import ch.ipt.abac.pizza.abac.AbacSalamiPolicy;
import ch.ipt.abac.pizza.adapter.secondary.persistence.PizzaEntity;
import ch.ipt.abac.pizza.adapter.secondary.persistence.PizzaEntityMapper;
import ch.ipt.abac.pizza.adapter.secondary.persistence.PizzaPostgresAdapter;
import ch.ipt.abac.pizza.adapter.secondary.persistence.QPizzaEntity;
import ch.ipt.abac.pizza.domain.model.Pizza;
import ch.ipt.abac.pizza.port.secondary.PizzaPersistencePort;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PizzaPersistenceAdapter implements PizzaPersistencePort {

    private final PizzaPostgresAdapter pizzaRepository;
    private final PizzaEntityMapper pizzaMapper;
    private final AbacPolicyEngine policyEngine;

    private final JPAQueryFactory queryFactory;
    private final AbacSalamiPolicy salamiPolicy;

    @Override
    public Pizza createPizza(Pizza pizza) {
        final var entity = pizzaMapper.map(pizza);
        final var persistedEntity = pizzaRepository.save(entity);
        return pizzaMapper.map(persistedEntity);
    }

    @Override
    public Optional<Pizza> findByName(String name) {
        final QPizzaEntity qPizza = QPizzaEntity.pizzaEntity;

        JPAQuery<PizzaEntity> query = queryFactory
                .selectFrom(qPizza)
                .where(qPizza.name.eq(name));

        var completeQuery = policyEngine.filter(query);
        final PizzaEntity entity = completeQuery.fetchOne();
        return Optional.ofNullable(entity).map(pizzaMapper::map);
    }

    @Override
    public List<Pizza> findAllPizzas() {
        final QPizzaEntity qPizza = QPizzaEntity.pizzaEntity;

        return queryFactory
                .selectFrom(qPizza)
                .fetch()
                .stream()
                .map(pizzaMapper::map)
                .toList();
    }
}