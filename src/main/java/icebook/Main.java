/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook;

import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;
import icebook.book.OrderBooks;
import icebook.exec.ExecutionEngine;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Main entry point to the icebook simulator.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public final class Main {

    /**
     * Gets an {@link InputSupplier} for {@link System#in}.
     *
     * @return {@link InputSupplier} for {@link System#in}.
     */
    static InputSupplier<InputStreamReader> newStdinInputSupplier() {
        return CharStreams.newReaderSupplier(new InputSupplier<InputStream>() {
            @Override
            public InputStream getInput() throws IOException {
                return System.in;
            }
        }, Charset.defaultCharset());
    }

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
            final ExecutionEngine engine = new ExecutionEngine(OrderBooks.newDefaultOrderBook());
            final OrderLineProcessor processor = new OrderLineProcessor(System.out, System.err, engine);
            CharStreams.readLines(newStdinInputSupplier(), processor);
        }
        catch (IOException e) {
            System.err.print("We hit an IOException: ");
            e.printStackTrace(System.err);
            System.exit(-1);
        }
        catch (Throwable e) {
            System.err.print("There was an unrecoverable error: ");
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}
