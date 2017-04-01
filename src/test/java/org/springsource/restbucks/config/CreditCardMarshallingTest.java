/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springsource.restbucks.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.junit.Before;
import org.junit.Test;
import org.springsource.restbucks.config.JacksonTestUtils;
import org.springsource.restbucks.domain.CreditCard;
import org.springsource.restbucks.domain.CreditCardNumber;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for marshaling of {@link CreditCard}.
 */
public class CreditCardMarshallingTest {

    private static final String REFERENCE = "{\"number\":\"1234123412341234\",\"cardHolderName\":\"Oliver Gierke\",\"expirationDate\":[2013,11,1]}";

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new ParameterNamesModule());
        mapper.registerModules(JacksonTestUtils.getModules());
    }

    @Test
    public void serializesCreditCardWithOutIdAndWithAppropriateMontshAndYears() throws Exception {

        CreditCard creditCard = new CreditCard(new CreditCardNumber("1234123412341234"), "Oliver Gierke", Month.NOVEMBER,
                Year.of(2013));

        String result = mapper.writeValueAsString(creditCard);

        assertThat(JsonPath.read(result, "$.number"), is("1234123412341234"));
        assertThat(JsonPath.read(result, "$.cardHolderName"), is("Oliver Gierke"));
        assertThat(JsonPath.read(result, "$.expirationDate"), is("2013-11-01"));

        Configuration configuration = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        assertThat(JsonPath.compile("$.valid").read(result, configuration), is(nullValue()));
    }

    @Test
    public void deserializesCreditCardWithOutIdAndWithAppropriateMontshAndYears() throws Exception {

        CreditCard creditCard = mapper.readValue(REFERENCE, CreditCard.class);

        assertThat(creditCard.getId(), is(nullValue()));
        assertThat(creditCard.getExpirationDate(), is(LocalDate.of(2013, 11, 1)));
        assertThat(creditCard.getNumber(), is(notNullValue()));
    }
}
