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
        return org.codehaus.jparsec.Parsers.or(icebergOrder(), limitOrder(), ignore());
    }

    /**
     * Gets a {@link Parser} for an iceberg order.
     *
     * @return a {@link Parser} for an iceberg order.
     */
    static final Parser<Optional<Order>> icebergOrder() {
        return new Mapper<Optional<Order>>() {
            Optional<Order> map(Side side, String s, String s1, String s2, String s3) {
                return Orders.newIcebergOrder(side, Integer.parseInt(s), Integer.parseInt(s1), Integer.parseInt(s2),
                                              Integer.parseInt(s3));
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
     * @return a {@link Parser} for a limit order.
     */
    static final Parser<Optional<Order>> limitOrder() {
        return new Mapper<Optional<Order>>() {
            Optional<Order> map(Side side, String s, String s1, String s2) {
                return Orders.newLimitOrder(side, Integer.parseInt(s), Integer.parseInt(s1), Integer.parseInt(s2));
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
     * @return a {@link Parser} for {@link Side#BUY}
     */
    static final Parser<Side> buy() {
        return Scanners.isChar('B').map(mapTo(Side.BUY));
    }

    /**
     * Gets a {@link Parser} for {@link Side#SELL}.
     *
     * @return a {@link Parser} for {@link Side#SELL}
     */
    static final Parser<Side> sell() {
        return Scanners.isChar('S').map(mapTo(Side.SELL));
    }

    /**
     * Gets a {@link Parser} that ignores {@link Scanners#WHITESPACES} and {@link Parsers#comment()}.
     *
     * @return {@link Parser} that ignores {@link Scanners#WHITESPACES} and {@link Parsers#comment()}.
     */
    static final Parser<Optional<Order>> ignore() {
        return org.codehaus.jparsec.Parsers.or(comment(), Scanners.WHITESPACES, org.codehaus.jparsec.Parsers.EOF).map(
                new Map<Object, Optional<Order>>() {
                    @Override
                    public Optional<Order> map(Object o) {
                        return Optional.absent();
                    }
                });
    }

    /**
     * Gets a {@link Parser} for comments.
     *
     * @return a {@link Parser} for comments.
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
