package ch.ipt.abac.pizza.adapter.secondary;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class SecurityContextAdapter {

    public static Collection<? extends GrantedAuthority> currentRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

}
