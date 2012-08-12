/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import com.google.common.base.Objects;
import icebook.book.OrderBook.Entry;
import icebook.exec.Trade;
import icebook.order.Side;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasToString;
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

    private static final long VOLUME = 130;

    private static final long SELL_ID = 1;

    private static final long BUY_ID = 123;

    private static final Entry SELL = new EntryImpl(SELL_ID, TIMESTAMP, Side.SELL, PRICE, VOLUME);

    private static final Entry BUY = new EntryImpl(BUY_ID, TIMESTAMP, Side.BUY, PRICE, VOLUME);

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
        new EntryImpl(1, 123, Side.SELL, -1, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongVolume() {
        new EntryImpl(1, 123, Side.SELL, 1, -1);
    }

    @Test
    public void testConstructor() {
        assertThat(SELL.getId(), is(SELL_ID));
        assertThat(SELL.getTimestamp(), is(TIMESTAMP));
        assertThat(SELL.getSide(), is(Side.SELL));
        assertThat(SELL.getPrice(), is(PRICE));
        assertThat(SELL.getVolume(), is(VOLUME));
    }

    @Test
    public void testIsFilledFilled() {
        assertThat(SELL.execute(new Trade(123, 1, PRICE, VOLUME)).isFilled(), is(true));
    }

    @Test
    public void testIsFilledNotFilled() {
        assertThat(SELL.isFilled(), is(false));
        assertThat(BUY.isFilled(), is(false));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testExecuteNullTrade() {
        SELL.execute(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExecuteSellSellIdWrong() {
        SELL.execute(new Trade(BUY_ID, SELL_ID + 1, PRICE, VOLUME));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExecuteBuyBuyIdWrong() {
        BUY.execute(new Trade(BUY_ID + 1, SELL_ID, PRICE, VOLUME));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExecuteOverExecute() {
        BUY.execute(new Trade(BUY_ID, SELL_ID, PRICE, VOLUME + 1));
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

    @Test
    public void testEquals() {
        assertThat(SELL.equals(null), is(false));
        assertThat(SELL.equals(new Object()), is(false));
        assertThat(SELL.equals(new EntryImpl(SELL_ID + 1, TIMESTAMP, Side.SELL, PRICE, VOLUME)), is(false));
        assertThat(SELL.equals(new EntryImpl(SELL_ID, TIMESTAMP + 1, Side.SELL, PRICE, VOLUME)), is(false));
        assertThat(SELL.equals(new EntryImpl(SELL_ID, TIMESTAMP, Side.BUY, PRICE, VOLUME)), is(false));
        assertThat(SELL.equals(new EntryImpl(SELL_ID, TIMESTAMP, Side.SELL, PRICE + 1, VOLUME)), is(false));
        assertThat(SELL.equals(new EntryImpl(SELL_ID, TIMESTAMP, Side.SELL, PRICE, VOLUME + 1)), is(false));
        assertThat(SELL.equals(SELL), is(true));
        assertThat(SELL.equals(new EntryImpl(SELL_ID, TIMESTAMP, Side.SELL, PRICE, VOLUME)), is(true));
    }

    @Test
    public void testToString() {
        assertThat(SELL, hasToString(equalTo("EntryImpl{id=" + SELL_ID + ", timestamp=" + TIMESTAMP + ", side=SELL" +
                                                     ", price=" + PRICE + ", volume=" + VOLUME + "}")));
    }

    @Test
    public void testHashCode() {
        assertThat(SELL.hashCode(), is(Objects.hashCode(SELL_ID, TIMESTAMP, Side.SELL, PRICE, VOLUME)));
    }
}
