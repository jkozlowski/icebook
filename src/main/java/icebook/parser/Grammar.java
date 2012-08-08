/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.parser;

import icebook.order.Side;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec._;
import org.codehaus.jparsec.functors.Map;

/**
 * Defines the grammar for parsing the input into new orders.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class Grammar {

    /**
     * Factory class.
     */
    private Grammar() {
    }

    /**
     * Gets a {@link Parser} for a limit order.
     *
     * @return a {@link Parser} for a limit order.
     */
    public static final Parser<?> limitOrder() {
        return Parsers.or(buy(), sell());
    }

    /**
     * Gets a {@link Parser} for {@link Side#BUY}.
     *
     * @return a {@link Parser} for {@link Side#BUY}
     */
    public static final Parser<Side> buy() {
        return Scanners.isChar('B').map(new Map<_, Side>() {
            @Override
            public Side map(_ p0) {
                return Side.BUY;
            }
        });
    }

    /**
     * Gets a {@link Parser} for {@link Side#SELL}.
     *
     * @return a {@link Parser} for {@link Side#SELL}
     */
    public static final Parser<Side> sell() {
        return Scanners.isChar('S').map(new Map<_, Side>() {
            @Override
            public Side map(_ p0) {
                return Side.SELL;
            }
        });
    }

    /**
     * Gets a {@link Parser} for whitespace.
     *
     * @return a {@link Parser} for comments.
     */
    public static final Parser<?> whitespace() {
        return Scanners.WHITESPACES;
    }

    /**
     * Gets a {@link Parser} for comments.
     *
     * @return a {@link Parser} for comments.
     */
    public static final Parser<?> comment() {
        return Scanners.WHITESPACES.next(Scanners.isChar('#')).next(Scanners.ANY_CHAR.many_());
    }
}
