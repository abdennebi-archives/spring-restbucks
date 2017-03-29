package org.springsource.restbucks.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springsource.restbucks.domain.Order;
import org.springsource.restbucks.event.OrderPaidEvent;
import org.springsource.restbucks.repository.OrderRepository;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple {@link OrderPaidEvent} listener marking the according {@link Order} as in process, sleeping for 10 seconds and
 * marking the order as processed right after that.
 */
@Slf4j
@Service
@AllArgsConstructor
class Engine {

    private final @NonNull
    OrderRepository repository;
    private final Set<Order> ordersInProgress = Collections.newSetFromMap(new ConcurrentHashMap<Order, Boolean>());

    @Async
    @TransactionalEventListener
    public void handleOrderPaidEvent(OrderPaidEvent event) {

        Order order = repository.findOne(event.getOrderId());
        order.markInPreparation();
        order = repository.save(order);

        ordersInProgress.add(order);

        LOG.info("Starting to process order {}.", order);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        order.markPrepared();
        repository.save(order);

        ordersInProgress.remove(order);

        LOG.info("Finished processing order {}.", order);
    }
}
