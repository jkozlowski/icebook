/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook;

import com.google.common.base.Optional;
import com.google.common.io.LineProcessor;
import icebook.exec.ExecutionEngine;
import icebook.exec.Trade;
import icebook.input.Parsers;
import icebook.order.Order;
import icebook.order.Side;
import icebook.output.Appenders;
import org.codehaus.jparsec.Parser;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collection;
import java.util.Formatter;
import java.util.Locale;
import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link LineProcessor} implementation that parses {@link Order}s from input according to the grammar defined by
 * {@link Parsers#newOrderParser()}. Every successfully parsed order will be applied to the order book and any trade
 * messages will be printed to {@code out} in the format defined by {@link Appenders#append(Appendable, Collection)},
 * followed by the current state of the book in the format defined by {@link Appenders#append(Appendable, SortedSet,
 * SortedSet)}. Any error messages are printed to to {@code err}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class OrderLineProcessor implements LineProcessor<Void> {

    private final Appendable out;

    private final Formatter err;

    private final Parser<Optional<Order>> parser;

    private final ExecutionEngine engine;

    /**
     * Default constructor.
     *
     * @param out {@link Appendable} to output to.
     * @param err {@link Appendable} to send error messages to.
     */
    public OrderLineProcessor(@Nonnull final Appendable out, @Nonnull final Appendable err,
                              @Nonnull final ExecutionEngine engine) {
        checkNotNull(out, "out cannot be null.");
        checkNotNull(err, "err cannot be null.");
        checkNotNull(engine, "engine cannot be null.");
        this.out = out;
        this.err = new Formatter(err, Locale.ENGLISH);
        parser = Parsers.newOrderParser();
        this.engine = engine;
    }

    @Override
    public boolean processLine(@Nonnull final String line) throws IOException {
        final Optional<Order> order = parser.parse(line);
        if (order.isPresent()) {
            final Collection<Trade> trades = engine.insert(order.get());
            Appenders.append(out, trades);
            Appenders.append(out, engine.toImmutableSortedSet(Side.SELL), engine.toImmutableSortedSet(Side.BUY));
        }
        else {
            err.format("Could not parse line: '%s'%n", line).flush();
        }

        return true;
    }

    @Override
    public Void getResult() {
        return null;
    }
}
