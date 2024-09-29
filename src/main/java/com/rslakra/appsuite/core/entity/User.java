package com.rslakra.appsuite.core.entity;

import com.rslakra.appsuite.core.ToString;
import com.rslakra.appsuite.core.enums.EntityStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Rohtash Lakra
 * @created 5/27/20 2:42 PM
 */
@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long id;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private EntityStatus entityStatus;

    /**
     * ToString
     *
     * @return
     */
    @Override
    public String toString() {
        return ToString.of(User.class)
            .add("id", getId())
            .add("email", getEmail())
            .add("firstName", getFirstName())
            .add("middleName", getMiddleName())
            .add("lastName", getLastName())
            .add("entityStatus", getEntityStatus())
            .toString();
    }

    /**
     * @param id
     * @param email
     * @param firstName
     * @param middleName
     * @param lastName
     * @param entityStatus
     * @return
     */
    public static User of(final Long id, final String email, final String firstName, final String middleName,
                          final String lastName, final EntityStatus entityStatus) {
        final User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setEntityStatus(entityStatus);

        return user;
    }

    /**
     * @param id
     * @param email
     * @param firstName
     * @param lastName
     * @return
     */
    public static User of(final Long id, final String email, final String firstName, final String lastName) {
        return of(id, email, firstName, null, lastName, null);
    }
}
