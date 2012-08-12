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
import org.codehaus.jparsec._;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Map2;
import org.codehaus.jparsec.misc.Mapper;
import org.codehaus.jparsec.pattern.CharPredicates;
import org.codehaus.jparsec.pattern.Patterns;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Defines the grammar for parsing {@link Order}s.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public final class Parsers {

    /**
     * Factory class.
     */
    private Parsers() {
    }

    /**
     * Gets a {@link Parser} for parsing {@link Order}s.
     *
     * @return {@link Parser} for parsing {@link Order}s.
     */
    public static final Parser<Optional<Order>> newOrderParser() {
        return org.codehaus.jparsec.Parsers.or(icebergOrder(), limitOrder(), ignored());
    }

    /**
     * Gets a {@link Parser} for an iceberg order.
     *
     * <p>An iceberg order entry consists of the following fields, with comma
     * separated values:</p>
     *
     * <table>
     * <tr><th>Field Index</th><td>Type</td><th>Description</th></tr>
     * <tr><td>0</td><td>Character</td><td>‘B’ for a buy order, ‘S’ for a sell order</td></tr>
     * <tr><td>1</td><td>Integer</td><td>unique order identifier</td></tr>
     * <tr><td>2</td><td>Integer</td><td>price in pence (>0)</td></tr>
     * <tr><td>3</td><td>Integer</td><td>quantity of order (>0)</td></tr>
     * <tr><td>4</td><td>Integer</td><td>peak size (>0)</td></tr>
     * </table>
     *
     * Example:
     * <pre>S,100345,5103,100000,10000</pre>
     *
     * Description:
     * <pre>Iceberg order id 100345: Sell 100,000 at 5103p, with a peak size of 10,000</pre>.
     *
     * @return {@link Parser} for an iceberg order.
     */
    static final Parser<Optional<Order>> icebergOrder() {
        return new Mapper<Optional<Order>>() {
            Optional<Order> map(Side side, String s, String s1, String s2, String s3) {
                return Orders.newIcebergOrder(Long.parseLong(s), side, Long.parseLong(s1), Long.parseLong(s2),
                                              Long.parseLong(s3));
            }
        }.sequence(
                followedByComma(org.codehaus.jparsec.Parsers.or(buy(), sell())),
                followedByComma(Scanners.INTEGER),
                followedByComma(Scanners.INTEGER),
                followedByComma(Scanners.INTEGER),
                Scanners.INTEGER
        );
    }

    /**
     * Gets a {@link Parser} for a limit order.
     *
     * <p>A limit order entry consists of the following fields, with comma
     * separated values:</p>
     *
     * <table>
     * <tr><th>Field Index</th><td>Type</td><th>Description</th></tr>
     * <tr><td>0</td><td>Character</td><td>‘B’ for a buy order, ‘S’ for a sell order</td></tr>
     * <tr><td>1</td><td>Integer</td><td>unique order identifier</td></tr>
     * <tr><td>2</td><td>Integer</td><td>price in pence (>0)</td></tr>
     * <tr><td>3</td><td>Integer</td><td>quantity of order (>0)</td></tr>
     * </table>
     *
     * Example:
     * <pre>B,100322,5103,7500</pre>
     *
     * @return {@link Parser} for a limit order.
     */
    static final Parser<Optional<Order>> limitOrder() {
        return new Mapper<Optional<Order>>() {
            Optional<Order> map(Side side, String s, String s1, String s2) {
                return Orders.newLimitOrder(Long.parseLong(s), side, Long.parseLong(s1), Long.parseLong(s2));
            }
        }.sequence(
                followedByComma(org.codehaus.jparsec.Parsers.or(buy(), sell())),
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
    static final <T> Parser<T> followedByComma(final @Nonnull Parser<T> toFollow) {
        checkNotNull(toFollow, "toFollow cannot be null.");
        return org.codehaus.jparsec.Parsers.sequence(toFollow, Scanners.isChar(','), Parsers.<T>mapFirst());
    }

    /**
     * Gets a {@link Parser} for {@link Side#BUY}.
     *
     * @return {@link Parser} for {@link Side#BUY}
     */
    static final Parser<Side> buy() {
        return Scanners.isChar('B').map(mapTo(Side.BUY));
    }

    /**
     * Gets a {@link Parser} for {@link Side#SELL}.
     *
     * @return {@link Parser} for {@link Side#SELL}
     */
    static final Parser<Side> sell() {
        return Scanners.isChar('S').map(mapTo(Side.SELL));
    }

    /**
     * Gets a {@link Parser} that accepts {@link Scanners#WHITESPACES}, {@link Parsers#comment()} and {@link org
     * .codehaus.jparsec.Parsers.EOF} (to ensure that an empty string gets accepted).
     *
     * @return {@link Parser} for ignored parts of the grammar.
     */
    static final Parser<Optional<Order>> ignored() {
        return org.codehaus.jparsec.Parsers.or(comment(), Scanners.WHITESPACES, org.codehaus.jparsec.Parsers.EOF).map(
                new Map<Object, Optional<Order>>() {
                    @Override
                    public Optional<Order> map(Object o) {
                        return Optional.absent();
                    }
                });
    }

    /**
     * Gets a {@link Parser} for comments, where a comment is defined as zero or more whitespace characters,
     * followed by {@code '#'} character, followed by zero or more characters.
     *
     * @return {@link Parser} for comments.
     */
    static final Parser<Optional<Order>> comment() {
        return Scanners.pattern(Patterns.many(CharPredicates.IS_WHITESPACE), "zero or more whitespaces")
                .next(Scanners.isChar('#'))
                .next(Scanners.ANY_CHAR.many().map(
                        new Map<List<_>, Optional<Order>>() {
                            @Override
                            public Optional<Order> map(List<_> list) {
                                return Optional.absent();
                            }
                        }));
    }

    /**
     * Gets a {@link Map} that discards the value, and returns {@code t} instead.
     *
     * @param t   value to return.
     * @param <T> type of the value to return.
     *
     * @return {@link Map} that returns {@code t}.
     *
     * @throws NullPointerException if {@code t} is null.
     */
    static final <T> Map<_, T> mapTo(@Nonnull final T t) {
        checkNotNull(t, "t cannot be null.");
        return new Map<_, T>() {
            @Override
            public T map(_ p0) {
                return t;
            }
        };
    }

    /**
     * Gets a {@link Map2} that returns the first argument.
     *
     * @param <T> type of the argument.
     *
     * @return {@link Map2} that returns the first argument.
     */
    static final <T> Map2<T, _, T> mapFirst() {
        return new Map2<T, _, T>() {
            @Override
            public T map(T t, _ p1) {
                return t;
            }
        };
    }
}
