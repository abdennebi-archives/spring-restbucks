package org.springsource.restbucks.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springsource.restbucks.domain.Order;

/**
 * {@link ResourceProcessor} implementation to add links to the {@link Order} representation that indicate that the
 * Order can be updated or cancelled as long as it has not been paid yet.
 */
@Component
@RequiredArgsConstructor
class CoreOrderResourceProcessor implements ResourceProcessor<Resource<Order>> {

    private static final String CANCEL_REL = "cancel";
    private static final String UPDATE_REL = "update";

    private final @NonNull
    EntityLinks entityLinks;

    /*
     * (non-Javadoc)
     * @see org.springframework.hateoas.ResourceProcessor#process(org.springframework.hateoas.ResourceSupport)
     */
    @Override
    public Resource<Order> process(Resource<Order> resource) {

        Order order = resource.getContent();

        if (!order.isPaid()) {
            resource.add(entityLinks.linkForSingleResource(order).withRel(CANCEL_REL));
            resource.add(entityLinks.linkForSingleResource(order).withRel(UPDATE_REL));
        }

        return resource;
    }
}
