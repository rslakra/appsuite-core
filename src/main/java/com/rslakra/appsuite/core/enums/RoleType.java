package com.rslakra.appsuite.core.enums;

import com.rslakra.appsuite.core.BeanUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Rohtash Lakra
 * @created 10/9/21 3:54 PM
 */
public enum RoleType {
    /* ADMIN */
    ADMIN,

    /* USER */
    USER,

    /* CREATOR */
    CREATOR,

    /* COLLABORATOR */
    COLLABORATOR,

    /* EDITOR */
    EDITOR,

    /* REFRESH_TOKEN */
    REFRESH_TOKEN;

    /**
     * @param roleType
     * @return
     */
    public static RoleType ofString(final String roleType) {
        return (BeanUtils.isNull(roleType) ? null : RoleType.valueOf(roleType.toUpperCase()));
    }

    /**
     * Returns the authority for the role type.
     *
     * @param roleType
     * @return
     */
    public static String toAuthority(final RoleType roleType) {
        return String.format("ROLE_%s", roleType.name());
    }

    /**
     * Returns the list of <code>GrantedAuthority</code> from the set of <code>Role</code> type.
     *
     * @param roles
     * @return
     */
//    public static Set<GrantedAuthority> toAuthorities(final Set<Role> roles) {
// return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole().name()))
//            .collect(Collectors.toSet());
//    }

    /**
     * Returns the list of <code>GrantedAuthority</code> from an array of <code>RoleType</code> type.
     *
     * @param roleTypes
     * @return
     */
//    public static List<GrantedAuthority> toAuthorities(final RoleType... roleTypes) {
// return Arrays.asList(roleTypes).stream().map(roleType -> new SimpleGrantedAuthority(roleType.name()))
//            .collect(Collectors.toList());
//    }

    /**
     * Returns the set of <code>RoleType</code> from the
     * <code>commaDelimitedRoleTypes</code> string.
     *
     * @param commaDelimitedRoleTypes
     * @return
     */
    public static Set<RoleType> toRoleTypes(final String commaDelimitedRoleTypes) {
        return (BeanUtils.isEmpty(commaDelimitedRoleTypes) ? null
                                                           : Arrays.asList(commaDelimitedRoleTypes.split(","))
                    .stream().map(roleType -> RoleType.valueOf(roleType)).collect(Collectors.toSet()));
    }

    /**
     * Returns the <code>RoleType</code> for the <code>roleTypes</code> objects.
     *
     * @param roleTypes
     * @return
     */
    public static Set<RoleType> toRoleTypes(final Collection<? extends String> roleTypes) {
        return (Objects.isNull(roleTypes) ? new HashSet<>()
                                          : roleTypes.stream().map(authority -> ofString(authority))
                    .collect(Collectors.toSet()));
    }


}
