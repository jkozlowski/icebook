package icebook;

import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;

import static icebook.Types.Side;
import static icebook.Utils.notNull;

/**
 * Formats the output as per spec.
 *
 * <p>Mostly copied from the previous answer, with appropriate adjustments.</p>
 *
 * <p>Formatting strings are exposed for the purpose of sanity testing
 * against the spec.</p>
 */
public final class OutputFormatter {

    public static final String HEADER
      = "+-----------------------------------------------------------------+%n"
      + "| BUY                            | SELL                           |%n"
      + "| Id       | Volume      | Price | Price | Volume      | Id       |%n"
      + "+----------+-------------+-------+-------+-------------+----------+%n";

    public static final String TRAILER
      = "+-----------------------------------------------------------------+%n";

    public static final String EMPTY_LINE_BUYS
      = "|          |             |       |";

    public static final String EMPTY_LINE_SELLS
      =                                   "       |             |          |";

    public static final String DATA_LINE_BUYS
      = "|%10d|%,13d|%,7d|";

    public static final String DATA_LINE_SELLS
      = "%,7d|%,13d|%10d|";

    public static final String NEWLINE = "%n";

    private final Formatter format;

    public OutputFormatter(final Appendable appendable) {
        notNull(appendable, "appendable");
        format = formatter(appendable);
    }

    public void flush() {
        format.flush();
    }

    /**
     * Appends the trade representations. Will append a newline at the end.
     *
     * @param trade
     *         trade to append
     * @throws NullPointerException if {@code trade} is null
     */
    public void append(final Trade trade) {
        notNull(trade, "trade");
        // No actual formatting, just substitution
        format.format("%s,%s,%s,%s%n",
                      trade.getTradeKey().getBuyOrderId().value(),
                      trade.getTradeKey().getSellOrderId().value(),
                      trade.getTradeKey().getPrice().value(),
                      trade.getQuantity().value());
    }

    /**
     * Appends to {@code out} a formatted representation of {@code book}, as
     * per spec.
     *
     * <p>If both {@code sells} and {@code buys} are empty, this method will
     * append an empty table to {@code out}.
     *
     * @param book
     *         book to format
     *
     * @throws NullPointerException
     *         if {@code book} is null.
     */
    public void append(final OrderBook book) {

        notNull(book, "book");

        format.format(HEADER);

        final Iterator<Order> buysIterator = book.getBookSide(Side.BUY)
                .iterator();
        final Iterator<Order> sellsIterator = book.getBookSide(Side.SELL)
                .iterator();

        while (sellsIterator.hasNext() || buysIterator.hasNext()) {

            if (buysIterator.hasNext()) {
                final Order buyOrder = buysIterator.next();
                // I used magic values, so that the append string is easier
                // to read.
                format.format(DATA_LINE_BUYS,
                              buyOrder.getOrderId().value(),
                              buyOrder.getVisibleQuantity().value(),
                              buyOrder.getPrice().value());
            } else {
                // Again, this is easier to read.
                format.format(EMPTY_LINE_BUYS);
            }

            if (sellsIterator.hasNext()) {
                final Order sellEntry = sellsIterator.next();
                // Likewise.
                format.format(DATA_LINE_SELLS,
                              sellEntry.getPrice().value(),
                              sellEntry.getVisibleQuantity().value(),
                              sellEntry.getOrderId().value());
            } else {
                // Likewise.
                format.format(EMPTY_LINE_SELLS);
            }
            format.format(NEWLINE);
        }

        format.format(TRAILER);
    }

    public static Formatter formatter(final Appendable out) {
        return new Formatter(
                notNull(out, "out"),
                Locale.ENGLISH);
    }
}
