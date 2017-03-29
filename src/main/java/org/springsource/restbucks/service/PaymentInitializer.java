package org.springsource.restbucks.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springsource.restbucks.domain.CreditCard;
import org.springsource.restbucks.domain.CreditCardNumber;
import org.springsource.restbucks.repository.CreditCardRepository;

import java.time.Month;
import java.time.Year;

/**
 * Initializing component to create a default {@link CreditCard} in the system.
 */
@Service
@Slf4j
@RequiredArgsConstructor
class PaymentInitializer {

    private final CreditCardRepository repository;

    @EventListener
    public void init(ApplicationReadyEvent event) {

        if (repository.count() > 0) {
            return;
        }

        CreditCardNumber number = new CreditCardNumber("1234123412341234");
        CreditCard creditCard = new CreditCard(number, "Oliver Gierke", Month.DECEMBER, Year.of(2099));

        creditCard = repository.save(creditCard);

        LOG.info("Credit card {} created!", creditCard);
    }
}
