/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.exec;

import com.google.common.base.Optional;
import icebook.book.OrderBook;
import icebook.book.OrderBook.Entry;
import icebook.book.OrderBooks;
import icebook.order.Order;
import icebook.order.Orders;
import icebook.order.Side;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link ExecutionEngine}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class ExecutionEngineTest {

    private static final long PRICE = 123;

    private static final long VOLUME = 12;

    private static final Entry sellEntry = OrderBooks.newEntry(1, 123, Side.SELL, PRICE, VOLUME);

    private static final Entry buyEntry = OrderBooks.newEntry(1, 123, Side.BUY, PRICE, VOLUME);

    private static final Order buyIceberg
            = Orders.newIcebergOrder(Side.BUY, 1, PRICE, VOLUME, 1).get();

    private static final Order sellIceberg
            = Orders.newIcebergOrder(Side.SELL, 1, PRICE, VOLUME, 1).get();

    private static final Order buyLimit = Orders.newLimitOrder(Side.BUY, 1, PRICE, VOLUME).get();

    private static final Order sellLimit
            = Orders.newLimitOrder(Side.SELL, 1, PRICE, VOLUME).get();

    private static final ExecutionEngine emptyEngine = new ExecutionEngine(OrderBooks.newDefaultOrderBook());


    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullBook() {
        new ExecutionEngine(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorNonEmptySell() {
        final OrderBook book = OrderBooks.newDefaultOrderBook();
        book.insert(sellEntry);
        new ExecutionEngine(book);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorNonEmptyBuy() {
        final OrderBook book = OrderBooks.newDefaultOrderBook();
        book.insert(buyEntry);
        new ExecutionEngine(book);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testInsertNullOrder() {
        emptyEngine.insert(null);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMatchOrderNull() {
        emptyEngine.match(null, sellEntry);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMatchIcebergOrderEntryNull() {
        emptyEngine.match(buyIceberg, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMatchIcebergOrderEntrySameSide() {
        emptyEngine.match(buyIceberg, buyEntry);
    }

    @Test
    public void testMatchIcebergOrderBuyMatchingEntry() {
        assertThat(emptyEngine.match(buyIceberg, OrderBooks.newEntry(2, 123, Side.SELL, PRICE - 1, VOLUME - 1)),
                   is(Optional.of(new Trade(buyIceberg.getId(), 2, PRICE - 1, VOLUME - 1))));
    }

    @Test
    public void testMatchIcebergOrderBuyNotMatchingEntry() {
        assertThat(emptyEngine.match(buyIceberg, OrderBooks.newEntry(2, 123, Side.SELL, PRICE + 1, VOLUME - 1)),
                   is(Optional.<Trade>absent()));
    }

    @Test
    public void testMatchIcebergOrderSellMatchingEntry() {
        assertThat(emptyEngine.match(sellIceberg, OrderBooks.newEntry(2, 123, Side.BUY, PRICE + 1, VOLUME - 1)),
                   is(Optional.of(new Trade(2, sellIceberg.getId(), PRICE + 1, VOLUME - 1))));
    }

    @Test
    public void testMatchIcebergOrderSellNotMatchingEntry() {
        assertThat(emptyEngine.match(sellIceberg, OrderBooks.newEntry(2, 123, Side.BUY, PRICE - 1, VOLUME - 1)),
                   is(Optional.<Trade>absent()));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMatchLimitOrderEntryNull() {
        emptyEngine.match(buyLimit, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMatchLimitOrderEntrySameSide() {
        emptyEngine.match(buyLimit, buyEntry);
    }

    @Test
    public void testMatchLimitOrderBuyMatchingEntry() {
        assertThat(emptyEngine.match(buyLimit, OrderBooks.newEntry(2, 123, Side.SELL, PRICE - 1, VOLUME - 1)),
                   is(Optional.of(new Trade(buyLimit.getId(), 2, PRICE - 1, VOLUME - 1))));
    }

    @Test
    public void testMatchLimitOrderBuyNotMatchingEntry() {
        assertThat(emptyEngine.match(buyLimit, OrderBooks.newEntry(2, 123, Side.SELL, PRICE + 1, VOLUME - 1)),
                   is(Optional.<Trade>absent()));
    }

    @Test
    public void testMatchLimitOrderSellMatchingEntry() {
        assertThat(emptyEngine.match(sellLimit, OrderBooks.newEntry(2, 123, Side.BUY, PRICE + 1, VOLUME - 1)),
                   is(Optional.of(new Trade(2, sellLimit.getId(), PRICE + 1, VOLUME - 1))));
    }

    @Test
    public void testMatchLimitOrderSellNotMatchingEntry() {
        assertThat(emptyEngine.match(sellLimit, OrderBooks.newEntry(2, 123, Side.BUY, PRICE - 1, VOLUME - 1)),
                   is(Optional.<Trade>absent()));
    }
}
