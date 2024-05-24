package com.example.rosaceae.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.rosaceae.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    GUEST(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,

                    SHOP_READ,
                    SHOP_CREATE
            )),
    CUSTOMER(
            Set.of(
                    CUSTOMER_CREATE,
                    CUSTOMER_READ,
                    CUSTOMER_UPDATE,
                    CUSTOMER_DELETE,

                    SHOP_READ
            )),
    SHOP(
            Set.of(
                    SHOP_CREATE,
                    SHOP_READ,
                    SHOP_UPDATE,
                    SHOP_DELETE,

                    CUSTOMER_CREATE,
                    CUSTOMER_READ,
                    CUSTOMER_UPDATE,
                    CUSTOMER_DELETE
            )),
    SUPPER_ADMIN(
            Set.of(
                    SUPPER_ADMIN_CREATE,
                    SUPPER_ADMIN_READ,
                    SUPPER_ADMIN_UPDATE,
                    SUPPER_ADMIN_DELETE,

                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            ))


    ;
    @Getter
    private final Set<Permission> permissions;
    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities =
                getPermissions().stream()
                        .map(permission1 -> new SimpleGrantedAuthority(permission1.getPermission()))
                        .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
