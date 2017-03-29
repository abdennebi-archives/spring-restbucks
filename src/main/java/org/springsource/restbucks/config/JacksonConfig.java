package org.springsource.restbucks.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.javamoney.moneta.Money;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.rest.webmvc.json.JsonSchema.JsonSchemaProperty;
import org.springframework.data.rest.webmvc.json.JsonSchemaPropertyCustomizer;
import org.springframework.data.util.TypeInformation;
import org.springsource.restbucks.domain.*;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Configures custom serialization and deserialization of {@link Money} instances
 */
@Configuration
public class JacksonConfig {

    public @Bean
    Module moneyModule() {
        return new MoneyModule();
    }

    public @Bean
    Module restbucksModule() {
        return new RestbucksModule();
    }

    @SuppressWarnings("serial")
    static class RestbucksModule extends SimpleModule {

        RestbucksModule() {

            setMixInAnnotation(Order.class, RestbucksModule.OrderMixin.class);
            setMixInAnnotation(LineItem.class, LineItemMixin.class);
            setMixInAnnotation(CreditCard.class, CreditCardMixin.class);
            setMixInAnnotation(CreditCardNumber.class, CreditCardNumberMixin.class);
        }

        @JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
        static abstract class OrderMixin {

            @JsonCreator
            public OrderMixin(Collection<LineItem> lineItems, Location location) {
            }
        }

        static abstract class LineItemMixin {

            @JsonCreator
            public LineItemMixin(String name, int quantity, Milk milk, Size size, MonetaryAmount price) {
            }
        }

        @JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
        static abstract class CreditCardMixin {

            abstract @JsonUnwrapped
            CreditCardNumber getNumber();
        }

        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        static abstract class CreditCardNumberMixin {
        }
    }

    @SuppressWarnings("serial")
    static class MoneyModule extends SimpleModule {

        MoneyModule() {

            addSerializer(MonetaryAmount.class, new MonetaryAmountSerializer());
            addValueInstantiator(MonetaryAmount.class, new MoneyInstantiator());
        }

        /**
         * A dedicated serializer to render {@link MonetaryAmount} instances as formatted {@link String}. Also implements
         * {@link JsonSchemaPropertyCustomizer} to expose the different rendering to the schema exposed by Spring Data REST.
         */
        static class MonetaryAmountSerializer extends StdSerializer<MonetaryAmount>
                implements JsonSchemaPropertyCustomizer {

            private static final Pattern MONEY_PATTERN;

            static {

                StringBuilder builder = new StringBuilder();

                builder.append("(?=.)^"); // Start
                builder.append("[A-Z]{3}?"); // 3-digit currency code
                builder.append("\\s"); // single whitespace character
                builder.append("(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?"); // digits with optional grouping by "," every 3)
                builder.append("(\\.[0-9]{1,2})?$"); // End with a dot and two digits

                MONEY_PATTERN = Pattern.compile(builder.toString());
            }

            MonetaryAmountSerializer() {
                super(MonetaryAmount.class);
            }


            @Override
            public void serialize(MonetaryAmount value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

                if (value != null) {
                    jgen.writeString(MonetaryFormats.getAmountFormat(LocaleContextHolder.getLocale()).format(value));
                } else {
                    jgen.writeNull();
                }
            }

            @Override
            public JsonSchemaProperty customize(JsonSchemaProperty property, TypeInformation<?> type) {
                return property.withType(String.class).withPattern(MONEY_PATTERN);
            }
        }

        static class MoneyInstantiator extends ValueInstantiator {

            @Override
            public String getValueTypeDesc() {
                return MonetaryAmount.class.toString();
            }

            @Override
            public boolean canCreateFromString() {
                return true;
            }

            @Override
            public Object createFromString(DeserializationContext context, String value) throws IOException {
                return Money.parse(value, MonetaryFormats.getAmountFormat(LocaleContextHolder.getLocale()));
            }
        }
    }
}
