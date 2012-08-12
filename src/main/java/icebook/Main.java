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
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * Main entry point to the icebook simulator.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public final class Main {

    private Main() {
    }

    /**
     * Gets an {@link InputSupplier} for {@link System#in}.
     *
     * @return {@link InputSupplier} for {@link System#in}.
     */
    static InputSupplier<InputStreamReader> newSystemInInputSupplier() {
        return CharStreams.newReaderSupplier(new InputSupplier<InputStream>() {
            @Override
            public InputStream getInput() {
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

        final PrintWriter out = new PrintWriter(System.out, true);
        final PrintWriter err = new PrintWriter(System.err, true);

        try {
            final ExecutionEngine engine = new ExecutionEngine(OrderBooks.newDefaultOrderBook());
            final OrderLineProcessor processor = new OrderLineProcessor(out, err, engine);
            CharStreams.readLines(newSystemInInputSupplier(), processor);
        }
        catch (IOException e) {
            System.err.print("We hit an IOException: ");
            e.printStackTrace(err);
            System.exit(-1);
        }
        catch (Throwable e) {
            System.err.print("There was an unrecoverable error: ");
            e.printStackTrace(err);
            System.exit(-1);
        }
        finally {
            out.flush();
            out.close();
            err.flush();
            err.close();
        }
    }
}
