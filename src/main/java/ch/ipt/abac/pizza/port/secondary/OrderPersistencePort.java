package ch.ipt.abac.pizza.port.secondary;

import ch.ipt.abac.pizza.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderPersistencePort {

    Order createOrder(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findAllOrders();

    Order updateOrder(UUID id, Order order);

    void deleteOrder(UUID id);
}
