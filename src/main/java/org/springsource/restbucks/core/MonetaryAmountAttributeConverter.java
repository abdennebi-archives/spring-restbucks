package org.springsource.restbucks.core;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Locale;

/**
 * JPA {@link AttributeConverter} to serialize {@link MonetaryAmount} instances into a {@link String}. Auto-applied to
 * all entity properties of type {@link MonetaryAmount}.
 */
@Converter(autoApply = true)
public class MonetaryAmountAttributeConverter implements AttributeConverter<MonetaryAmount, String> {

    private static final MonetaryAmountFormat FORMAT = MonetaryFormats.getAmountFormat(Locale.ROOT);

    @Override
    public String convertToDatabaseColumn(MonetaryAmount amount) {
        return amount == null ? null : amount.toString();
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(String source) {
        return source == null ? null : Money.parse(source, FORMAT);
    }
}
