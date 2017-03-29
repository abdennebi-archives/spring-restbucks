package org.springsource.restbucks.domain;

import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class PaymentException extends RuntimeException {

    private static final long serialVersionUID = -4929826142920520541L;
    private final Order order;

    public PaymentException(Order order, String message) {

        super(message);

        Assert.notNull(order);
        this.order = order;
    }
}
