/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import icebook.book.OrderBook.Entry;
import icebook.exec.Trade;
import icebook.order.Side;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

/**
 * Tests {@link EntryImpl}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class EntryImplTest {

    private static final long TIMESTAMP = 123;

    private static final long PRICE = 12;

    private static final Entry SELL = new EntryImpl(1, TIMESTAMP, Side.SELL, PRICE, 130);

    private static final Entry BUY = new EntryImpl(1, TIMESTAMP, Side.BUY, PRICE, 130);

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongId() {
        new EntryImpl(0, 123, Side.SELL, 1, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongTimestamp() {
        new EntryImpl(1, 0, Side.SELL, 1, 1);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullSide() {
        new EntryImpl(1, 123, null, 1, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongPrice() {
        new EntryImpl(1, 123, Side.SELL, 0, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongVolume() {
        new EntryImpl(1, 123, Side.SELL, 1, 0);
    }

    @Test
    public void testConstructor() {
        final Entry entry = new EntryImpl(1, 123, Side.SELL, PRICE, 130);
        assertThat(entry.getId(), is(1L));
        assertThat(entry.getSide(), is(Side.SELL));
        assertThat(entry.getPrice(), is(12L));
        assertThat(entry.getVolume(), is(130L));
    }

    @Test
    public void testIsFilledNotFilled() {
        final Entry entry = new EntryImpl(1, TIMESTAMP, Side.SELL, PRICE, 130);
        entry.execute(new Trade(123, 1, PRICE, 130));
        assertThat(entry.isFilled(), is(true));
    }

    @Test
    public void testIsFilledFilled() {
        assertThat(SELL.isFilled(), is(false));
        assertThat(BUY.isFilled(), is(false));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testCompareToNullOther() {
        SELL.compareTo(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCompareToWrongSide() {
        SELL.compareTo(OrderBooks.newEntry(1, 123, Side.BUY, 2, 3));
    }

    @Test
    public void testCompareToItself() {
        assertThat(SELL.compareTo(SELL), is(0));
        assertThat(BUY.compareTo(BUY), is(0));
    }

    @Test
    public void testCompareToPriceLess() {
        assertThat(SELL.compareTo(OrderBooks.newEntry(1, TIMESTAMP, Side.SELL, PRICE - 1, 3)), greaterThan(0));
        assertThat(BUY.compareTo(OrderBooks.newEntry(1, TIMESTAMP, Side.BUY, PRICE - 1, 3)), lessThan(0));
    }

    @Test
    public void testCompareToPriceGreater() {
        assertThat(SELL.compareTo(OrderBooks.newEntry(1, TIMESTAMP, Side.SELL, PRICE + 1, 3)), lessThan(0));
        assertThat(BUY.compareTo(OrderBooks.newEntry(1, TIMESTAMP, Side.BUY, PRICE + 1, 3)), greaterThan(0));
    }

    @Test
    public void testCompareToSamePriceEarlierTimestamp() {
        assertThat(SELL.compareTo(OrderBooks.newEntry(1, TIMESTAMP - 1, Side.SELL, PRICE, 3)), greaterThan(0));
        assertThat(BUY.compareTo(OrderBooks.newEntry(1, TIMESTAMP - 1, Side.BUY, PRICE, 3)), greaterThan(0));
    }

    @Test
    public void testCompareToSamePriceLaterTimestamp() {
        assertThat(SELL.compareTo(OrderBooks.newEntry(1, TIMESTAMP + 1, Side.SELL, PRICE, 3)), lessThan(0));
        assertThat(BUY.compareTo(OrderBooks.newEntry(1, TIMESTAMP + 1, Side.BUY, PRICE, 3)), lessThan(0));
    }
}
