package ch.ipt.abac.pizza.adapter.secondary.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "Pizzas")
public class PizzaEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String name;

}
