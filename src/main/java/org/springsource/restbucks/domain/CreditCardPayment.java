package org.springsource.restbucks.domain;

import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * A {@link Payment} done through a {@link CreditCard}.
 */
@Entity
@Getter
public class CreditCardPayment extends Payment {

    @ManyToOne
    private final CreditCard creditCard;

    protected CreditCardPayment() {
        this.creditCard = null;
    }

    /**
     * Creates a new {@link CreditCardPayment} for the given {@link CreditCard} and {@link Order}.
     *
     * @param creditCard must not be {@literal null}.
     */
    public CreditCardPayment(CreditCard creditCard, Order order) {

        super(order);
        Assert.notNull(creditCard);
        this.creditCard = creditCard;
    }
}
