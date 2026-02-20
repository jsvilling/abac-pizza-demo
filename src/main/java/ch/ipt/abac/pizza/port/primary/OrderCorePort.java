package ch.ipt.abac.pizza.port.primary;

import ch.ipt.abac.pizza.domain.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderCorePort {

    Order createOrder(Order order);

    Order findOrderById(UUID id);

    List<Order> findAllOrders();

    Order updateOrder(UUID id, Order order);

    void deleteOrder(UUID id);
}