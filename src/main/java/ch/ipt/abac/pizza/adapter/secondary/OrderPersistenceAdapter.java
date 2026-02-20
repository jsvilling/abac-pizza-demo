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
        final QOrderEntity qOrder = QOrderEntity.orderEntity;

        JPAQuery<OrderEntity> query = queryFactory
                .selectFrom(qOrder)
                .where(qOrder.id.eq(id));

        var completeQuery = policyEngine.filter(query);

        final OrderEntity entity = completeQuery.fetchOne();
        return Optional.ofNullable(entity).map(orderMapper::map);
    }

    @Override
    public List<Order> findAllOrders() {
        final QOrderEntity qOrder = QOrderEntity.orderEntity;

        var initialQuery = queryFactory.selectFrom(qOrder);
        var completeQuery = policyEngine.filter(initialQuery);

        return completeQuery.fetch().stream().map(orderMapper::map).toList();
    }

    @Override
    public Order updateOrder(UUID id, Order order) {
        validateAccess(id);
        final var entity = orderMapper.map(order);
        final var updatedEntity = orderRepository.save(entity);
        return orderMapper.map(updatedEntity);
    }

    @Override
    public void deleteOrder(UUID id) {
        validateAccess(id);
        orderRepository.deleteById(id);
    }

    private void validateAccess(UUID id) {
        this.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}