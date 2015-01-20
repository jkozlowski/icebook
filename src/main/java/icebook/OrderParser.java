package icebook;

import static icebook.Types.Side;
import static icebook.Utils.*;

public final class OrderParser {

    private static final char COMMENT_CHAR = '#';

    private OrderParser() {
        privateConstructor(getClass());
    }

    /**
     * Parses an {@link Order}.
     *
     * <p>The code below does not recover from error conditions and will make
     * the assumption that input is well formed, throwing unchecked
     * exceptions whenever that is not the case.</p>
     *
     * @param line
     *         line to parse
     *
     * @return parsed order or null if line is a comment or blank line
     *
     * @throws NullPointerException
     *         if {@code line} is null
     */
    public static Order tryParseOrder(final String line) {

        notNull(line, "line");

        // Trim to see if comment of empty:
        // 1) A line may consist entirely of whitespace characters (empty)
        // 2) Begin with whitespace, followed by the '#' character and
        // subsequently any other characters (comment)
        final String trimmed = line.trim();
        if (!trimmed.isEmpty() && trimmed.charAt(0) != COMMENT_CHAR) {

            // 3) In comma-separated ascii format (data)
            final String[] split = trimmed.split(",");
            checkState(split.length >= 4 && split.length <= 5, "Malformed" +
                    " line");

            // Parse the values: assume no overflow and throw unchecked
            // exceptions on malformed input
            final Side side = parseSide(split[0]);
            final Types.OrderId orderId = parseOrderId(split[1]);
            final Types.Price price = parsePrice(split[2]);
            final Types.Quantity quantity = parseQuantity(split[3]);
            final Types.Quantity peakSize
                    = split.length == 5
                    ? parseQuantity(split[4])
                    : null;

            return new Order(side, orderId, price, quantity,
                             peakSize);
        }

        return null;
    }

    private static Side parseSide(final String s) {
        notNull(s, "s");
        if ("S".equals(s)) {
            return Side.SELL;
        } else if ("B".equals(s)) {
            return Side.BUY;
        } else {
            throw new IllegalArgumentException("Invalid side: " + s);
        }
    }

    private static Types.OrderId parseOrderId(final String s) {
        return Types.orderId(Integer.parseInt(s));
    }

    private static Types.Price parsePrice(final String s) {
        return Types.price(Short.parseShort(s));
    }

    private static Types.Quantity parseQuantity(final String s) {
        return Types.nonZeroQuantity(Integer.parseInt(s));
    }
}
