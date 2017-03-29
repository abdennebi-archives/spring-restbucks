package org.springsource.restbucks.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springsource.restbucks.domain.Order;

/**
 * Event to be thrown when an {@link Order} has been payed.
 */
@Getter
@EqualsAndHashCode
@ToString
public class OrderPaidEvent {

    private final long orderId;

    /**
     * Creates a new {@link OrderPaidEvent}
     *
     * @param orderId the id of the order that just has been payed
     */
    public OrderPaidEvent(long orderId) {
        this.orderId = orderId;
    }
}
