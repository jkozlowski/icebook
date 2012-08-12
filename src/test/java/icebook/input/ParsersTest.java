/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.input;

import com.google.common.base.Optional;
import icebook.order.Order;
import icebook.order.Orders;
import icebook.order.Side;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Scanners;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link Parsers}
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class ParsersTest {

    @Test
    public void testNewGrammar() {
        final Parser<Optional<Order>> grammar = Parsers.newOrderParser();
        assertThat(grammar.parse("B,100345,5103,100000,10123"),
                   is(Orders.newIcebergOrder(100345, Side.BUY, 5103, 100000, 10123)));
        assertThat(grammar.parse("S,5103,100000,10000,10345"),
                   is(Orders.newIcebergOrder(5103, Side.SELL, 100000, 10000, 10345)));
        assertThat(grammar.parse("B,100322,5103,7500"),
                   is(Orders.newLimitOrder(100322, Side.BUY, 5103, 7500)));
        assertThat(grammar.parse("S,5103,7500,100322"),
                   is(Orders.newLimitOrder(5103, Side.SELL, 7500, 100322)));
        assertThat(grammar.parse(" \n\t\r").isPresent(), is(Boolean.FALSE));
        assertThat(grammar.parse("  \n  # ").isPresent(), is(Boolean.FALSE));
        assertThat(grammar.parse("   # ").isPresent(), is(Boolean.FALSE));
        assertThat(grammar.parse("   #asdasd").isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void testIcebergOrderParser() {
        final Parser<Optional<Order>> limitOrder = Parsers.icebergOrder();
        assertThat(limitOrder.parse("B,100345,5103,100000,10000"),
                   is(Orders.newIcebergOrder(100345, Side.BUY, 5103, 100000, 10000)));
        assertThat(limitOrder.parse("S,5103,100000,10000,100345"),
                   is(Orders.newIcebergOrder(5103, Side.SELL, 100000, 10000, 100345)));
    }

    @Test
    public void testLimitOrderParser() {
        final Parser<Optional<Order>> limitOrder = Parsers.limitOrder();
        assertThat(limitOrder.parse("B,100322,5103,7500"), is(Orders.newLimitOrder(100322, Side.BUY, 5103, 7500)));
        assertThat(limitOrder.parse("S,5103,7500,100322"), is(Orders.newLimitOrder(5103, Side.SELL, 7500, 100322)));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testFollowedByCommaNullToFollow() {
        Parsers.followedByComma(null);
    }

    @Test
    public void testFollowedByComma() {
        final String integer = "123123";
        final Parser<String> integerComma = Parsers.followedByComma(Scanners.INTEGER);
        assertThat(integerComma.parse(integer + ","), is(integer));
    }

    @Test
    public void testSellSideParser() {
        final Parser<Side> sellSide = Parsers.sell();
        assertThat(sellSide.parse("S"), is(Side.SELL));
    }

    @Test
    public void testBuySideParser() {
        final Parser<Side> buySide = Parsers.buy();
        assertThat(buySide.parse("B"), is(Side.BUY));
    }

    @Test
    public void testCommentParser() {
        final Parser<Optional<Order>> comment = Parsers.comment();
        assertThat(comment.parse("  \n  # ").isPresent(), is(Boolean.FALSE));
        assertThat(comment.parse("   # ").isPresent(), is(Boolean.FALSE));
        assertThat(comment.parse("   #asdasd").isPresent(), is(Boolean.FALSE));
        assertThat(comment.parse("#asdasd").isPresent(), is(Boolean.FALSE));
        assertThat(comment.parse("#").isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void testIgnore() {
        final Parser<Optional<Order>> comment = Parsers.ignored();
        assertThat(comment.parse("  \n").isPresent(), is(Boolean.FALSE));
        assertThat(comment.parse("   ").isPresent(), is(Boolean.FALSE));
        assertThat(comment.parse(" ").isPresent(), is(Boolean.FALSE));
        assertThat(comment.parse("\n").isPresent(), is(Boolean.FALSE));
        assertThat(comment.parse("\t\n\r ").isPresent(), is(Boolean.FALSE));
        assertThat(comment.parse("").isPresent(), is(Boolean.FALSE));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMapToNullT() {
        Parsers.mapTo(null);
    }

    @Test
    public void testMapFirst() {
        assertThat(Parsers.<Side>mapFirst().map(Side.SELL, null), is(Side.SELL));
    }

    @Test
    public void testMapTo() {
        assertThat(Parsers.mapTo(Side.BUY).map(null), is(Side.BUY));
    }
}
