/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import icebook.book.OptimisedOrderBook.PriceLevelEntry;
import icebook.book.OrderBook.Entry;
import icebook.order.Side;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

/**
 * Tests {@link OptimisedOrderBookTest}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class OptimisedOrderBookTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullFirstEntries() {
        new OptimisedOrderBook.PriceLevelEntry(null);
    }

    @Test
    public void testConstructor() {
        final Entry firstEntry = OrderBooks.newEntry(1, 2, Side.SELL, 3, 4);
        final PriceLevelEntry priceLevelEntry = new PriceLevelEntry(firstEntry);
        assertThat(priceLevelEntry.priceLevel, is(3L));
        assertThat(priceLevelEntry.side, is(Side.SELL));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testCompareToNullOther() {
        new PriceLevelEntry(OrderBooks.newEntry(1, 2, Side.SELL, 3, 4)).compareTo(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCompareToDifferentSide() {
        final PriceLevelEntry sellPriceLevelEntry = new PriceLevelEntry(OrderBooks.newEntry(1, 2, Side.SELL, 3, 4));
        final PriceLevelEntry buyPriceLevelEntry = new PriceLevelEntry(OrderBooks.newEntry(1, 2, Side.BUY, 4, 4));
        sellPriceLevelEntry.compareTo(buyPriceLevelEntry);
    }

    @Test
    public void testCompareTo() {
        final PriceLevelEntry sellPriceLevelEntry = new PriceLevelEntry(OrderBooks.newEntry(1, 2, Side.SELL, 3, 4));
        final PriceLevelEntry sellPriceLevelEntry2 = new PriceLevelEntry(OrderBooks.newEntry(1, 2, Side.SELL, 4, 4));
        assertThat(sellPriceLevelEntry.compareTo(sellPriceLevelEntry2), is(lessThan(0)));
        assertThat(sellPriceLevelEntry2.compareTo(sellPriceLevelEntry), is(greaterThan(0)));

        final PriceLevelEntry buyPriceLevelEntry = new PriceLevelEntry(OrderBooks.newEntry(1, 2, Side.BUY, 3, 4));
        final PriceLevelEntry buyPriceLevelEntry2 = new PriceLevelEntry(OrderBooks.newEntry(1, 2, Side.BUY, 4, 4));
        assertThat(buyPriceLevelEntry.compareTo(buyPriceLevelEntry2), is(greaterThan(0)));
        assertThat(buyPriceLevelEntry2.compareTo(buyPriceLevelEntry), is(lessThan(0)));
    }
}
