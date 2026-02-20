package ch.ipt.abac.pizza.adapter.primary;

import ch.ipt.abac.pizza.abac.api.OrdersApi;
import ch.ipt.abac.pizza.abac.api.model.Order;
import ch.ipt.abac.pizza.adapter.primary.mapper.OrderMapper;
import ch.ipt.abac.pizza.port.primary.OrderCorePort;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class OrdersApiAdapter implements OrdersApi {

    private final OrderCorePort orderCorePort;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('demo_order_create')")
    public ResponseEntity<Order> createOrder(Order orderDto) {
        var requestedOrder = orderMapper.map(orderDto);
        var createdOrder = orderCorePort.createOrder(requestedOrder);
        var orderResponseDto = orderMapper.map(createdOrder);
        return ResponseEntity.ok(orderResponseDto);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('demo_order_delete')")
    public ResponseEntity<Void> deleteOrder(UUID id) {
        orderCorePort.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('demo_order_read')")
    public ResponseEntity<Order> getOrderById(UUID id) {
        final var order = orderCorePort.findOrderById(id);
        final var orderDto = orderMapper.map(order);
        return ResponseEntity.ok(orderDto);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('demo_order_read')")
    public ResponseEntity<List<Order>> getOrders() {
        final var orders = orderCorePort.findAllOrders();
        final var orderDtos = orderMapper.map(orders);
        return ResponseEntity.ok(orderDtos);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('demo_order_update')")
    public ResponseEntity<Order> updateOrder(UUID id, Order orderDto) {
        var requestedOrder = orderMapper.map(orderDto);
        var updatedOrder = orderCorePort.updateOrder(id, requestedOrder);
        var orderResponseDto = orderMapper.map(updatedOrder);
        return ResponseEntity.ok(orderResponseDto);
    }

}