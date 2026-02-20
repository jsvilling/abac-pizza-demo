package ch.ipt.abac.pizza.abac;

import ch.ipt.abac.pizza.adapter.secondary.SecurityContextAdapter;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AbacPolicyEngine {

    private final List<AbacPolicy> policies;

    public <T> JPAQuery<T> filter(JPAQuery<T> query) {
        // TODO: We need to get this in a more efficient way
        final List<PizzaRole> roles = SecurityContextAdapter.currentRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .map(str -> {
                    try {
                        return PizzaRole.valueOf(str);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

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
