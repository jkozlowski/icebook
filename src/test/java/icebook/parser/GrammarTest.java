/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.parser;

import icebook.order.Order;
import icebook.order.Orders;
import icebook.order.Side;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Scanners;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Tests {@link Grammar}
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class GrammarTest {

    @Test
    public void testIcebergOrderParser() {
        final Parser<Order> limitOrder = Grammar.icebergOrder();
        assertThat(limitOrder.parse("B,100345,5103,100000,10000"),
                   is(Orders.newIcebergOrder(Side.BUY, 100345, 5103, 100000, 10000)));
        assertThat(limitOrder.parse("S,5103,100000,10000,100345"),
                   is(Orders.newIcebergOrder(Side.SELL, 5103, 100000, 10000, 100345)));
    }

    @Test
    public void testLimitOrderParser() {
        final Parser<Order> limitOrder = Grammar.limitOrder();
        assertThat(limitOrder.parse("B,100322,5103,7500"), is(Orders.newLimitOrder(Side.BUY, 100322, 5103, 7500)));
        assertThat(limitOrder.parse("S,5103,7500,100322"), is(Orders.newLimitOrder(Side.SELL, 5103, 7500, 100322)));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testFollowedByCommaNullToFollow() {
        Grammar.followedByComma(null);
    }

    @Test
    public void testFollowedByComma() {
        final String integer = "123123";
        final Parser<String> integerComma = Grammar.followedByComma(Scanners.INTEGER);
        assertThat(integerComma.parse(integer + ","), is(integer));
    }

    @Test
    public void testSellSideParser() {
        final Parser<Side> sellSide = Grammar.sell();
        assertThat(sellSide.parse("S"), is(Side.SELL));
    }

    @Test
    public void testBuySideParser() {
        final Parser<Side> buySide = Grammar.buy();
        assertThat(buySide.parse("B"), is(Side.BUY));
    }

    @Test
    public void testCommentParser() {
        final Parser<?> comment = Grammar.comment();
        assertThat(comment.parse("  \n  # "), nullValue());
        assertThat(comment.parse("   # "), nullValue());
        assertThat(comment.parse("   #asdasd"), nullValue());
    }

    @Test
    public void testWhitespaceParser() {
        final Parser<?> comment = Grammar.whitespace();
        assertThat(comment.parse(" \n\t\r"), nullValue());
    }
}
