package ch.ipt.abac.pizza.domain.service;

import ch.ipt.abac.pizza.adapter.secondary.SecurityContextAdapter;
import ch.ipt.abac.pizza.domain.model.Order;
import ch.ipt.abac.pizza.port.primary.OrderCorePort;
import ch.ipt.abac.pizza.port.secondary.OrderPersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService implements OrderCorePort {

    private final OrderPersistencePort orderPersistencePort;

    @Override
    public Order createOrder(Order order) {
        final var currentUserId = SecurityContextAdapter.getCurrentUserId();
        order.setUserId(currentUserId);
        return orderPersistencePort.createOrder(order);
    }

    @Override
    public Order findOrderById(UUID id) {
        return orderPersistencePort.findById(id).orElseThrow();
    }

    @Override
    public List<Order> findAllOrders() {
        return orderPersistencePort.findAllOrders();
    }

    @Override
    public Order updateOrder(UUID id, Order order) {
        return orderPersistencePort.updateOrder(id, order);
    }

    @Override
    public void deleteOrder(UUID id) {
        orderPersistencePort.deleteOrder(id);
    }
}