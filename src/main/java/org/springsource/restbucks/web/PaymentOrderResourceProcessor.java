package org.springsource.restbucks.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springsource.restbucks.domain.Order;

/**
 * {@link ResourceProcessor} to enrich {@link Order} {@link Resource}s with links to the {@link PaymentController}.
 */
@Component
@RequiredArgsConstructor
public class PaymentOrderResourceProcessor implements ResourceProcessor<Resource<Order>> {

    private final @NonNull
    PaymentLinks paymentLinks;

    @Override
    public Resource<Order> process(Resource<Order> resource) {

        Order order = resource.getContent();

        if (!order.isPaid()) {
            resource.add(paymentLinks.getPaymentLink(order));
        }

        if (order.isReady()) {
            resource.add(paymentLinks.getReceiptLink(order));
        }

        return resource;
    }
}
