package org.springsource.restbucks.web;

import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception being thrown in case an {@link Order} has already been paid and a payment is reattempted.
 */
@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
class OrderAlreadyPaidException extends RuntimeException {

    private static final long serialVersionUID = 1L;
}
