/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.parser;

import icebook.order.Side;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.functors.Tuple4;
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
    public void testLimitOrderParser() {
        final Parser<Tuple4<Side, Integer, Integer, Integer>> limitOrder = Grammar.limitOrder();
        assertThat(limitOrder.parse("B,100322,5103,7500"),
                   is(new Tuple4<Side, Integer, Integer, Integer>(Side.BUY, 100322, 5103, 7500)));
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
