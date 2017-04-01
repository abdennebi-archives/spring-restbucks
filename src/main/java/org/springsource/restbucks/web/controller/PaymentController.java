package org.springsource.restbucks.web.controller;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springsource.restbucks.domain.*;
import org.springsource.restbucks.domain.Payment.Receipt;
import org.springsource.restbucks.service.PaymentService;

import javax.money.MonetaryAmount;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Spring MVC controller to handle payments for an {@link Order}.
 */
@Controller
@RequestMapping("/orders/{id}")
@ExposesResourceFor(Payment.class)
@RequiredArgsConstructor
public class PaymentController {

    private final @NonNull
    PaymentService paymentService;
    private final @NonNull
    EntityLinks entityLinks;

    /**
     * Accepts a payment for an {@link Order}
     *
     * @param order  the {@link Order} to process the payment for. Retrieved from the path variable and converted into an
     *               {@link Order} instance by Spring Data's {@link DomainClassConverter}. Will be {@literal null} in case no
     *               {@link Order} with the given id could be found.
     * @param number the {@link CreditCardNumber} unmarshaled from the request payload.
     * @return
     */
    @RequestMapping(path = "/payment", method = PUT)
    ResponseEntity<?> submitPayment(@PathVariable("id") Order order, @RequestBody CreditCardNumber number) {

        if (order == null || order.isPaid()) {
            return ResponseEntity.notFound().build();
        }

        CreditCardPayment payment = paymentService.pay(order, number);

        PaymentResource resource = new PaymentResource(order.getPrice(), payment.getCreditCard());
        resource.add(entityLinks.linkToSingleResource(order));

        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    /**
     * Shows the {@link Receipt} for the given order.
     */
    @RequestMapping(path = "/receipt", method = GET)
    HttpEntity<?> showReceipt(@PathVariable("id") Order order) {

        if (order == null || !order.isPaid() || order.isTaken()) {
            return ResponseEntity.notFound().build();
        }

        return paymentService.getPaymentFor(order).//
                map(payment -> createReceiptResponse(payment.getReceipt())).//
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Takes the {@link Receipt} for the given {@link Order} and thus completes the process.
     */
    @RequestMapping(path = "/receipt", method = DELETE)
    HttpEntity<?> takeReceipt(@PathVariable("id") Order order) {

        if (order == null || !order.isPaid()) {
            return ResponseEntity.notFound().build();
        }

        return paymentService.takeReceiptFor(order).//
                map(receipt -> createReceiptResponse(receipt)).//
                orElseGet(() -> new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED));
    }

    /**
     * Renders the given {@link Receipt} including links to the associated {@link Order} as well as a self link in case
     * the {@link Receipt} is still available.
     */
    private HttpEntity<Resource<Receipt>> createReceiptResponse(Receipt receipt) {

        Order order = receipt.getOrder();

        Resource<Receipt> resource = new Resource<>(receipt);
        resource.add(entityLinks.linkToSingleResource(order));

        if (!order.isTaken()) {
            resource.add(entityLinks.linkForSingleResource(order).slash("receipt").withSelfRel());
        }

        return ResponseEntity.ok(resource);
    }

    /**
     * Resource implementation for payment results.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    static class PaymentResource extends ResourceSupport {

        private final MonetaryAmount amount;
        private final CreditCard creditCard;
    }
}
