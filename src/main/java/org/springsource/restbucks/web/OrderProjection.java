package org.springsource.restbucks.web;

import org.springframework.data.rest.core.config.Projection;
import org.springsource.restbucks.domain.Order;
import org.springsource.restbucks.domain.Order.Status;

import java.time.LocalDateTime;

/**
 * Projection interface to render {@link Order} summaries.
 */
@Projection(name = "summary", types = Order.class)
public interface OrderProjection {

    /**
     * @see Order#getOrderedDate()
     */
    LocalDateTime getOrderedDate();

    /**
     * @see Order#getStatus()
     */
    Status getStatus();
}
