package org.springsource.restbucks.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springsource.restbucks.core.AbstractEntity;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

/**
 * A line item.
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
public class LineItem extends AbstractEntity {

    private String name;
    private int quantity;
    private Milk milk;
    private Size size;
    private MonetaryAmount price;

    public LineItem(String name, MonetaryAmount price) {
        this(name, 1, Milk.SEMI, Size.LARGE, price);
    }

}
