/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.exec;

import icebook.book.OrderBook;
import icebook.book.OrderBooks;
import icebook.order.Side;
import org.testng.annotations.Test;

/**
 * Tests {@link ExecutionEngine}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class ExecutionEngineTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullBook() {
        new ExecutionEngine(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorNonEmptySell() {
        final OrderBook book = OrderBooks.newDefaultOrderBook();
        book.insert(OrderBooks.newEntry(1, 123, Side.SELL, 123, 123));
        new ExecutionEngine(book);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorNonEmptyBuy() {
        final OrderBook book = OrderBooks.newDefaultOrderBook();
        book.insert(OrderBooks.newEntry(1, 123, Side.BUY, 123, 123));
        new ExecutionEngine(book);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testInsertNullOrder() {
        new ExecutionEngine(OrderBooks.newDefaultOrderBook()).insert(null);
    }
}
