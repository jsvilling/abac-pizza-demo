package ch.ipt.abac.pizza.adapter.secondary;

import ch.ipt.abac.pizza.abac.AbacPolicyEngine;
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
import java.util.UUID;

@Component
@AllArgsConstructor
public class PizzaPersistenceAdapter implements PizzaPersistencePort {

    private final PizzaPostgresAdapter pizzaRepository;
    private final PizzaEntityMapper pizzaMapper;
    private final AbacPolicyEngine policyEngine;

    private final JPAQueryFactory queryFactory;

    @Override
    public Pizza createPizza(Pizza pizza) {
        final var entity = pizzaMapper.map(pizza);
        final var persistedEntity = pizzaRepository.save(entity);
        return pizzaMapper.map(persistedEntity);
    }

    @Override
    public Optional<Pizza> findById(UUID id) {
        final var qPizza = QPizzaEntity.pizzaEntity;

        final var query = queryFactory
                .selectFrom(qPizza)
                .where(qPizza.id.eq(id));

        final var entity = query.fetchOne();
        return Optional.ofNullable(entity).map(pizzaMapper::map);
    }

    @Override
    public List<Pizza> findByName(String name) {
        final var qPizza = QPizzaEntity.pizzaEntity;

        final var query = queryFactory
                .selectFrom(qPizza)
                .where(qPizza.name.equalsIgnoreCase(name));

        return query
                .fetch()
                .stream()
                .map(pizzaMapper::map)
                .toList();
    }

    @Override
    public List<Pizza> findAllPizzas() {
        final var qPizza = QPizzaEntity.pizzaEntity;
        final var query = queryFactory.selectFrom(qPizza);
        return query
                .fetch()
                .stream()
                .map(pizzaMapper::map)
                .toList();
    }
}