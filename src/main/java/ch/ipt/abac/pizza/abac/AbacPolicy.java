package ch.ipt.abac.pizza.abac;

import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface AbacPolicy {

    List<PizzaRole> relevantRoles();

    <T> JPAQuery<T> apply(JPAQuery<T> query);

}
