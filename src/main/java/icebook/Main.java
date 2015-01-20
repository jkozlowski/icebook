package icebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import static icebook.Utils.notNull;

/**
 * Main entry point to the icebook simulator.
 */
public final class Main {

    private final BufferedReader in;
    private final OutputFormatter f;

    public Main(final BufferedReader in, final Appendable f) {
        this.in = notNull(in, "in");
        this.f = new OutputFormatter(notNull(f, "f"));
    }

    /**
     * Does not close the underlying streams.
     */
    public void run() throws IOException {

        final OrderBook book = OrderBook.emptyBook();

        String line;
        while ((line = in.readLine()) != null) {

            // Parse the newOrder: skip comments and whitespace
            final Order newOrder = OrderParser.tryParseOrder(line);
            if (null == newOrder) {
                continue;
            }

            // Lookup the opposite book
            final OrderBook.BookSide opposite
                    = book.getBookSide(newOrder.getSide().opposite());

            // Do the matching and get the trades
            final Iterable<Trade> trades = Matcher.match(opposite, newOrder);

            // Put the remainder of the newOrder into the book
            if (newOrder.isOpen()) {
                final OrderBook.BookSide sameSide
                        = book.getBookSide(newOrder.getSide());

                // Icebergs should enter the book with full peak
                newOrder.resetToPeak();
                sameSide.insert(newOrder);
            }

            // Print trades
            for (final Trade trade : trades) {
                f.append(trade);
            }

            // Print the book
            f.append(book);

            // Flush
            f.flush();
        }
    }

    /**
     * Main entry point to the icebook simulator.
     *
     * @param args
     *         no arguments are expected.
     */
    public static void main(final String... args) throws Exception {

        if (0 != args.length) {
            System.err.println("No arguments please. Exiting...");
            System.exit(-1);
        }

        try (final BufferedReader in
                     = new BufferedReader(new InputStreamReader(System.in));
             final PrintWriter out = new PrintWriter(System.out, true)) {
            // Don't care about exceptions from here on, as per spec.
            new Main(in, out).run();
        }
    }
}
