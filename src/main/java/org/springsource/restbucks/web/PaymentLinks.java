package org.springsource.restbucks.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springsource.restbucks.domain.Order;
import org.springsource.restbucks.domain.Payment;
import org.springsource.restbucks.domain.Payment.Receipt;

import static org.springsource.restbucks.config.HateoasConfig.CURIE_NAMESPACE;

/**
 * Helper component to create links to the {@link Payment} and {@link Receipt}.
 */
@Component
@RequiredArgsConstructor
public class PaymentLinks {

    static final String PAYMENT_REL = CURIE_NAMESPACE + ":payment";
    static final String RECEIPT_REL = "receipt";
    static final String PAYMENT = "/payment";
    static final String RECEIPT = "/receipt";
    private final @NonNull
    EntityLinks entityLinks;

    /**
     * Returns the {@link Link} to point to the {@link Payment} for the given {@link Order}.
     *
     * @param order must not be {@literal null}.
     */
    Link getPaymentLink(Order order) {
        return entityLinks.linkForSingleResource(order).slash(PAYMENT).withRel(PAYMENT_REL);
    }

    /**
     * Returns the {@link Link} to the {@link Receipt} of the given {@link Order}.
     */
    Link getReceiptLink(Order order) {
        return entityLinks.linkForSingleResource(order).slash(RECEIPT).withRel(RECEIPT_REL);
    }
}
