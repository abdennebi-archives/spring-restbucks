package org.springsource.restbucks.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springsource.restbucks.domain.Order;
import org.springsource.restbucks.domain.Order.Status;
import org.springsource.restbucks.web.OrderProjection;

import java.util.List;

/**
 * Repository to manage {@link Order} instances.
 */
@RepositoryRestResource(excerptProjection = OrderProjection.class)
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    /**
     * Returns all {@link Order}s with the given {@link Status}.
     *
     * @param status must not be {@literal null}.
     */
    List<Order> findByStatus(@Param("status") Status status);
}
