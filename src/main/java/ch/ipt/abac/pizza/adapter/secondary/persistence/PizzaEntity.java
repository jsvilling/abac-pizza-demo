package ch.ipt.abac.pizza.adapter.secondary.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "Pizzas")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PizzaEntity {

    @Id
    private UUID id;

    private String name;


}
