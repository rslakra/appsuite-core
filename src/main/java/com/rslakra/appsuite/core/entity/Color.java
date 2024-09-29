package com.rslakra.appsuite.core.entity;

import com.rslakra.appsuite.core.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Rohtash Lakra
 * @created 9/11/19 1:36 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Color {

    private Long id;
    private String name;

    /**
     * Returns the string representation of this object.
     *
     * @return
     */
    @Override
    public String toString() {
        return ToString.of(Color.class)
            .add("id", id)
            .add("name", name)
            .toString();
    }
}
