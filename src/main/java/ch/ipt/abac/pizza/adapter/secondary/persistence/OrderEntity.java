package ch.ipt.abac.pizza.adapter.secondary.persistence;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "Orders")
public class OrderEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String pizzaName;

    private UUID pizzaId;

}