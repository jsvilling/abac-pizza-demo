package ch.ipt.abac.pizza.abac;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

@UtilityClass
public class SecurityContextAdapter {

    public static Collection<? extends GrantedAuthority> currentRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

}
