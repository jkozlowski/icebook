/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook;

import com.google.common.io.CharStreams;
import icebook.input.Parsers;
import icebook.output.Appenders;

import java.io.IOException;
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
            CharStreams.readLines(Suppliers.newStdinInputSupplier(), new OrderLineProcessor(System.out, System.err));
        }
        catch (IOException e) {
            System.err.print("We hit an IOException: ");
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}
