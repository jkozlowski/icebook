/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook;

import com.google.common.base.Optional;
import com.google.common.io.CharStreams;
import com.google.common.io.LineProcessor;
import icebook.book.OrderBook;
import icebook.book.OrderBooks;
import icebook.exec.ExecutionEngine;
import icebook.exec.Trade;
import icebook.input.Parsers;
import icebook.order.Order;
import icebook.order.Side;
import icebook.output.Appenders;
import org.codehaus.jparsec.Parser;

import java.io.IOException;
import java.util.Collection;
import java.util.SortedSet;

/**
 * Main entry point to the icebook simulator. The simulator will read the input from {@link System#in} and parse it
 * according to the grammar as defined by {@link Parsers#newOrderParser()}. Every successfully parsed order will be
 * applied to the order book and any trade messages will be printed to {@link System#out},
 * followed by the current state of the book as defined by {@link Appenders#append(Appendable, SortedSet,
 * SortedSet)}. Any error messages will be forwarded to {@link System#err}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public final class Main {

    /**
     * Main entry point to the icebook simulator.
     *
     * @param args no arguments are expected.
     */
    public static void main(final String... args) {

        if (0 != args.length) {
            System.err.println("No arguments please. Exiting...");
            System.exit(-1);
        }

        try {
            final Parser<Optional<Order>> parser = Parsers.newOrderParser();
            final OrderBook book = OrderBooks.newDefaultOrderBook();
            final ExecutionEngine engine = new ExecutionEngine(book);

            CharStreams.readLines(Suppliers.newStdinInputSupplier(), new LineProcessor<Void>() {
                @Override
                public boolean processLine(final String line) throws IOException {
                    final Optional<Order> order = parser.parse(line);
                    if (order.isPresent()) {
                        final Collection<Trade> trades = engine.insert(order.get());
                        Appenders.append(System.out, trades);
                        Appenders.append(System.out, book.toSortedSet(Side.SELL), book.toSortedSet(Side.BUY));
                    }
                    else {
                        System.err.format("Could not parse line: '%s'%n", line).flush();
                    }

                    return true;
                }

                @Override
                public Void getResult() {
                    return null;
                }
            });
        }
        catch (IOException e) {
            System.err.print("We hit an IOException: ");
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}
