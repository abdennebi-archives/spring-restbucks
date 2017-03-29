package org.springsource.restbucks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springsource.restbucks.core.AbstractEntity;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

/**
 * Abstraction of a credit card.
 */
@Entity
@ToString(callSuper = true)
@AllArgsConstructor
public class CreditCard extends AbstractEntity {

    @Getter
    private final CreditCardNumber number;

    @Getter
    private final String cardHolderName;

    private Month expiryMonth;

    private Year expiryYear;

    protected CreditCard() {
        this(null, null, null, null);
    }

    /**
     * Returns whether the {@link CreditCard} is currently valid.
     */
    public boolean isValid() {
        return isValid(LocalDate.now());
    }

    /**
     * Returns whether the {@link CreditCard} is valid for the given date.
     */
    public boolean isValid(LocalDate date) {
        return date != null && getExpirationDate().isAfter(date);
    }

    /**
     * Returns the {@link LocalDate} the {@link CreditCard} expires.
     *
     * @return will never be {@literal null}.
     */
    public LocalDate getExpirationDate() {
        return LocalDate.of(expiryYear.getValue(), expiryMonth, 1);
    }

    /**
     * Protected setter to allow binding the expiration date.
     */
    protected void setExpirationDate(LocalDate date) {

        this.expiryYear = Year.of(date.getYear());
        this.expiryMonth = date.getMonth();
    }
}
