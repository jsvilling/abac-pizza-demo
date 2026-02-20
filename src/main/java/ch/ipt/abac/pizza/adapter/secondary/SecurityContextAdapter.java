package ch.ipt.abac.pizza.adapter.secondary;

import ch.ipt.abac.pizza.abac.PizzaRole;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class SecurityContextAdapter {

    public static List<PizzaRole> getCurrentRoles() {
        return SecurityContextAdapter.getCurrentAuthorities().stream()
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
    }

    public static Collection<? extends GrantedAuthority> getCurrentAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    public static String getCurrentUserId() {
        return ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getToken().getClaim("userId");
    }

}
