/*
 * Copyright 2012-2015 the original author or authors.
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
package org.springsource.restbucks.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springsource.restbucks.domain.CreditCard;
import org.springsource.restbucks.domain.CreditCardNumber;

/**
 * Repository to access {@link CreditCard} instances.
 * 
 * @author Oliver Gierke
 */
public interface CreditCardRepository extends CrudRepository<CreditCard, Long> {

	/**
	 * Returns the {@link CreditCard} assicaiated with the given {@link CreditCardNumber}.
	 * 
	 * @param number must not be {@literal null}.
	 * @return
	 */
	Optional<CreditCard> findByNumber(CreditCardNumber number);
}
