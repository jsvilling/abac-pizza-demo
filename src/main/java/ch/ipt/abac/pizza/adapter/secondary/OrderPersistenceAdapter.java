package ch.ipt.abac.pizza.adapter.secondary;

import ch.ipt.abac.pizza.abac.AbacPolicyEngine;
import ch.ipt.abac.pizza.adapter.secondary.persistence.OrderEntity;
import ch.ipt.abac.pizza.adapter.secondary.persistence.OrderEntityMapper;
import ch.ipt.abac.pizza.adapter.secondary.persistence.OrderPostgresAdapter;
import ch.ipt.abac.pizza.adapter.secondary.persistence.QOrderEntity;
import ch.ipt.abac.pizza.domain.exception.EntityNotFoundException;
import ch.ipt.abac.pizza.domain.model.Order;
import ch.ipt.abac.pizza.port.secondary.OrderPersistencePort;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class OrderPersistenceAdapter implements OrderPersistencePort {

    private final OrderPostgresAdapter orderRepository;
    private final OrderEntityMapper orderMapper;
    private final AbacPolicyEngine policyEngine;

    private final JPAQueryFactory queryFactory;

    @Override
    public Order createOrder(Order order) {
        final var entity = orderMapper.map(order);
        final var persistedEntity = orderRepository.save(entity);
        return orderMapper.map(persistedEntity);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        final var qOrder = QOrderEntity.orderEntity;

        final var query = queryFactory
                .selectFrom(qOrder)
                .where(qOrder.id.eq(id));

        final OrderEntity entity = query.fetchOne();
        return Optional.ofNullable(entity).map(orderMapper::map);
    }

    @Override
    public List<Order> findAllOrders() {
        final QOrderEntity qOrder = QOrderEntity.orderEntity;
        var query = queryFactory.selectFrom(qOrder);
        return query.fetch().stream().map(orderMapper::map).toList();
    }

    @Override
    public Order updateOrder(UUID id, Order order) {
        final var entity = orderMapper.map(order);
        final var updatedEntity = orderRepository.save(entity);
        return orderMapper.map(updatedEntity);
    }

    @Override
    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }
}