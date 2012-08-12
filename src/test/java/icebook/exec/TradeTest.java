/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.exec;

import com.google.common.base.Objects;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link Trade}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class TradeTest {

    private static final Trade trade = new Trade(1, 2, 3, 4);

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongBuyOrderId() {
        new Trade(0, 1, 1, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongSellOrderId() {
        new Trade(1, 0, 1, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongPrice() {
        new Trade(1, 1, 0, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongVolume() {
        new Trade(1, 1, 1, 0);
    }

    @Test
    public void testConstructor() {
        assertThat(trade.getBuyOrderId(), is(1L));
        assertThat(trade.getSellOrderId(), is(2L));
        assertThat(trade.getPrice(), is(3L));
        assertThat(trade.getQuantity(), is(4L));
    }

    @Test
    public void testEquals() {
        assertThat(trade, is(trade));
        assertThat(trade.equals(null), is(false));
        assertThat(trade.equals(new Object()), is(false));
        assertThat(trade.equals(new Trade(2, 2, 3, 4)), is(false));
        assertThat(trade.equals(new Trade(1, 3, 3, 4)), is(false));
        assertThat(trade.equals(new Trade(1, 2, 4, 4)), is(false));
        assertThat(trade.equals(new Trade(1, 2, 3, 5)), is(false));
        assertThat(trade.equals(new Trade(1, 2, 3, 4)), is(true));
    }

    @Test
    public void testHashCode() {
        assertThat(trade.hashCode(), is(Objects.hashCode(1, 2, 3, 4)));
    }

    @Test
    public void testToString() {
        assertThat(trade, hasToString(equalTo("Trade{buyOrderId=1, sellOrderId=2, price=3, quantity=4}")));
    }
}
