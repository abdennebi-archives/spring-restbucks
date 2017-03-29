package org.springsource.restbucks.repository;

import org.springframework.data.repository.CrudRepository;
import org.springsource.restbucks.domain.CreditCard;
import org.springsource.restbucks.domain.CreditCardNumber;

import java.util.Optional;

/**
 * Repository to access {@link CreditCard} instances.
 */
public interface CreditCardRepository extends CrudRepository<CreditCard, Long> {

    /**
     * Returns the {@link CreditCard} assicaiated with the given {@link CreditCardNumber}.
     *
     * @param number must not be {@literal null}.
     */
    Optional<CreditCard> findByNumber(CreditCardNumber number);
}
