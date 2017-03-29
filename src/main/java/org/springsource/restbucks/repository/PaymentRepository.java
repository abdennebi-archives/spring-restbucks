package org.springsource.restbucks.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springsource.restbucks.domain.Order;
import org.springsource.restbucks.domain.Payment;

import java.util.Optional;

/**
 * Repository interface to manage {@link Payment} instances.
 */
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {

    /**
     * Returns the payment registered for the given {@link Order}.
     */
    Optional<Payment> findByOrder(Order order);
}
