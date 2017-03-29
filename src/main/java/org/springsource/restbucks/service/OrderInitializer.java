package org.springsource.restbucks.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springsource.restbucks.domain.LineItem;
import org.springsource.restbucks.domain.Order;
import org.springsource.restbucks.repository.OrderRepository;

import java.util.Arrays;

import static org.springsource.restbucks.core.Currencies.EURO;

/**
 * Initializer to set up two {@link Order}s.
 */
@Service
@RequiredArgsConstructor
class OrderInitializer {

    private final @NonNull
    OrderRepository orders;

    /**
     * Creates two orders and persists them using the given {@link OrderRepository}.
     *
     * @param event must not be {@literal null}.
     */
    @EventListener
    public void init(ApplicationReadyEvent event) {

        if (orders.count() != 0) {
            return;
        }

        LineItem javaChip = new LineItem("Java Chip", Money.of(4.20, EURO));
        LineItem cappuchino = new LineItem("Cappuchino", Money.of(3.20, EURO));

        Order javaChipOrder = new Order(javaChip);
        Order cappuchinoOrder = new Order(cappuchino);

        orders.save(Arrays.asList(javaChipOrder, cappuchinoOrder));
    }
}
