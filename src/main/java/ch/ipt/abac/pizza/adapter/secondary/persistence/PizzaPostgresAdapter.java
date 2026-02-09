package ch.ipt.abac.pizza.adapter.secondary.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PizzaPostgresAdapter extends JpaRepository<PizzaEntity, UUID> {
}
