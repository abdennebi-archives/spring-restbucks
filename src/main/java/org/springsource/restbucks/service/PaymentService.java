package org.springsource.restbucks.service;

import org.springsource.restbucks.domain.*;
import org.springsource.restbucks.domain.Payment.Receipt;

import java.util.Optional;

/**
 * Interface to collect payment services.
 */
public interface PaymentService {

    /**
     * Pay the given {@link Order} with the {@link CreditCard} identified by the given {@link CreditCardNumber}.
     */
    CreditCardPayment pay(Order order, CreditCardNumber creditCardNumber);

    /**
     * Returns the {@link Payment} for the given {@link Order}.
     *
     * @return the {@link Payment} for the given {@link Order} or {@link Optional#empty()} if the Order hasn't been payed
     * yet.
     */
    Optional<Payment> getPaymentFor(Order order);

    /**
     * Takes the receipt
     */
    Optional<Receipt> takeReceiptFor(Order order);
}
