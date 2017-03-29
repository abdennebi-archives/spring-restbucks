package org.springsource.restbucks.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Value object to represent a {@link CreditCardNumber}.
 */
@Data
@Embeddable
public class CreditCardNumber {

    private static final String regex = "[0-9]{16}";

    @Column(unique = true)
    private final String number;

    protected CreditCardNumber() {
        this.number = null;
    }

    /**
     * Creates a new {@link CreditCardNumber}.
     *
     * @param number must not be {@literal null} and be a 16 digit numerical only String.
     */
    public CreditCardNumber(String number) {

        if (!isValid(number)) {
            throw new IllegalArgumentException(String.format("Invalid credit card NUMBER %s!", number));
        }

        this.number = number;
    }

    /**
     * Returns whether the given {@link String} is a valid {@link CreditCardNumber}.
     */
    public static boolean isValid(String number) {
        return number != null && number.matches(regex);
    }
}
