package ch.ipt.abac.pizza.abac;

import ch.ipt.abac.pizza.adapter.secondary.SecurityContextAdapter;
import ch.ipt.abac.pizza.adapter.secondary.persistence.OrderEntity;
import ch.ipt.abac.pizza.adapter.secondary.persistence.PizzaEntity;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class AbacPolicyEngine {

    private final List<AbacPolicy> policies;

    public <T> JPAQuery<T> filter(JPAQuery<T> query) {
        final List<PizzaRole> roles = SecurityContextAdapter.getCurrentRoles();

        // TODO: This works only via side effects. I think we would need recursion to do it cleanly.
        return policies
            .stream()
            .filter(p -> containsAny(roles, p.relevantRoles()))
            .reduce(query, (q, policy) -> policy.
                    apply(q), (a, b) -> a);
    }

    private boolean containsAny(List<PizzaRole> left, List<PizzaRole> right) {
        return right.stream().anyMatch(left::contains);
    }

}
