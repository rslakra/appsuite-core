package com.rslakra.appsuite.core.entity;

import com.rslakra.appsuite.core.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Rohtash Lakra
 * @created 5/27/20 2:43 PM
 */
@Getter
@Setter
@NoArgsConstructor
public class Address {

    private Long id;
    private String addressLine1;
    private String addressLine2;
    private String country;
    private String state;
    private String city;
    private String zipCode;

    /**
     * ToString
     *
     * @return
     */
    @Override
    public String toString() {
        return ToString.of(Address.class)
            .add("id", getId())
            .add("addressLine1", getAddressLine1())
            .add("addressLine2", getAddressLine2())
            .add("country", getCountry())
            .add("state", getState())
            .add("city", getCity())
            .add("zipCode", getZipCode())
            .toString();
    }
}
