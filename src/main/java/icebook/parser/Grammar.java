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
import org.codehaus.jparsec.functors.Map2;
import org.codehaus.jparsec.functors.Tuple4;
import org.codehaus.jparsec.functors.Tuple5;
import org.codehaus.jparsec.functors.Tuples;
import org.codehaus.jparsec.misc.Mapper;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

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
     * Gets a {@link Parser} for an iceberg order.
     *
     * @return a {@link Parser} for an iceberg order.
     */
    public static final Parser<Tuple5<Side, Integer, Integer, Integer, Integer>> icebergOrder() {
        return new Mapper<Tuple5<Side, Integer, Integer, Integer, Integer>>() {
            Tuple5<Side, Integer, Integer, Integer, Integer> map(Side side, String s, String s1, String s2,
                                                                 String s3) {
                return Tuples.tuple(side, Integer.parseInt(s), Integer.parseInt(s1), Integer.parseInt(s2),
                                    Integer.parseInt(s3));
            }
        }.sequence(
                followedByComma(Parsers.or(buy(), sell())),
                followedByComma(Scanners.INTEGER),
                followedByComma(Scanners.INTEGER),
                followedByComma(Scanners.INTEGER),
                Scanners.INTEGER
        );
    }

    /**
     * Gets a {@link Parser} for a limit order.
     *
     * @return a {@link Parser} for a limit order.
     */
    public static final Parser<Tuple4<Side, Integer, Integer, Integer>> limitOrder() {
        return new Mapper<Tuple4<Side, Integer, Integer, Integer>>() {
            Tuple4<Side, Integer, Integer, Integer> map(Side side, String s, String s1, String s2) {
                return Tuples.tuple(side, Integer.parseInt(s), Integer.parseInt(s1), Integer.parseInt(s2));
            }
        }.sequence(
                followedByComma(Parsers.or(buy(), sell())),
                followedByComma(Scanners.INTEGER),
                followedByComma(Scanners.INTEGER),
                Scanners.INTEGER
        );
    }

    /**
     * Gets a {@link Parser} that expects {@code toFollow} followed by a comma.
     *
     * @param toFollow parser to be followed by a comma.
     * @param <T>      type of the parser.
     *
     * @return {@link Parser} that expects {@code toFollow} followed by a comma.
     *
     * @throws NullPointerException if {@code toFollow} is null.
     */
    public static final <T> Parser<T> followedByComma(final @Nonnull Parser<T> toFollow) {
        checkNotNull(toFollow, "toFollow cannot be null.");
        return Parsers.sequence(toFollow, Scanners.isChar(','), new Map2<T, _, T>() {
            @Override
            public T map(T t, _ p1) {
                return t;
            }
        });
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
