package icebook;

import java.util.LinkedHashMap;

import static icebook.Utils.*;

/**
 * Implements the matching algorithm.
 */
public final class Matcher {

    private Matcher() {
        privateConstructor(getClass());
    }

    /**
     * Matches the {@code newOrder} with orders on the book. Does not insert
     * the remainder of {@code newOrder}.
     *
     * @param book
     *         opposite side of the book
     * @param newOrder
     *         new order to match against
     *
     * @return any trades in the price, time priority, appropriately merged
     *
     * @throws NullPointerException
     *         if any argument is null
     * @throws IllegalArgumentException
     *         if the order's and the book's sides are not opposite
     */
    public static Iterable<Trade> match(final OrderBook.BookSide book,
                                        final Order newOrder) {
        notNull(book, "book");
        notNull(newOrder, "newOrder");
        checkArgument(book.getSide().opposite().equals(newOrder.getSide()),
                      "order must be opposite to the book side");

        // 1) For each order insert message, the solution should apply the
        // order to the current book, and generate any matched trades first
        // in the order of matching precedence (i.e. price nad time
        // precedence of the orders), and then output the current book.
        //
        // Using a LinkedHashMap, performing matching in price priority and
        // merging quantities or trades as we go along will maintain these
        // requirements.
        //
        // Ideally I would maintain a map from TradeKey to just quantity
        // and just map over the map with the Trade constructor,
        // but this is a little bit painful without lambdas.
        final LinkedHashMap<Types.TradeKey, Trade> trades = new
                LinkedHashMap<>();

        Order topOfBook;

        while (null != (topOfBook = book.topOfBook()) && newOrder.isOpen()) {

            // Try to match
            final Trade maybeTrade = tryMatch(topOfBook, newOrder);
            if (null == maybeTrade) {
                break;
            }

            // Add the trade
            final Trade existingTrade = trades.get(maybeTrade.getTradeKey());
            if (null != existingTrade) {
                trades.put(existingTrade.getTradeKey(), mergeQuantities
                        (existingTrade, maybeTrade));
            } else {
                trades.put(maybeTrade.getTradeKey(), maybeTrade);
            }

            // Remove the topOfBook if filled to current peak
            if (!topOfBook.hasVisible()) {
                book.removeTopOfBook();

                // Reintroduce the top of book if still has quantity
                if (topOfBook.isOpen()) {
                    topOfBook.resetToPeak();
                    book.insert(topOfBook);
                }
            }

            // Reset the newOrder
            if (!newOrder.hasVisible() && newOrder.isOpen()) {
                newOrder.resetToPeak();
            }
        }

        return trades.values();
    }

    /**
     * Tries to match the two orders. If a match occurs the quantities of the
     * two orders will be adjusted by the trade quantity.
     *
     * @param resting
     *         resting order
     * @param newOrder
     *         new order
     *
     * @return trade if they match, null otherwise
     *
     * @throws NullPointerException
     *         if any argument is null
     * @throws IllegalArgumentException
     *         if {@code resting} order is not the {@code opposite} side to
     *         newOrder or either order is filled or any order is already
     *         filled
     */
    private static Trade tryMatch(final Order resting,
                                  final Order newOrder) {

        notNull(resting, "resting");
        notNull(newOrder, "newOrder");
        checkArgument(resting.isOpposite(newOrder),
                      "resting.side.opposite != newOrder.side");
        checkArgument(resting.isOpen(), "resting order filled");
        checkArgument(newOrder.isOpen(), "newOrder order filled");

        // Check for matching prices
        final boolean isTrade;
        if (newOrder.getSide().isBuy()) {
            isTrade = newOrder.getPrice().compareTo(resting.getPrice()) >= 0;
        } else {
            isTrade = newOrder.getPrice().compareTo(resting.getPrice()) <= 0;
        }

        if (isTrade) {

            // Quantity: min of the visible quantities
            final Types.Quantity tradeQuantity = resting.getVisibleQuantity()
                    .min
                            (newOrder.getVisibleQuantity());

            // Price: always that of the resting order
            final Types.Price tradePrice = resting.getPrice();

            // Execute both orders: by definition this is correct
            // as the quantity is bounded by the visible quantity
            resting.execute(tradeQuantity);
            newOrder.execute(tradeQuantity);

            // Create the trade
            return Trade.newTrade(resting, newOrder, tradePrice, tradeQuantity);
        }

        return null;
    }

    private static Trade mergeQuantities(final Trade trade1, final Trade
            trade2) {
        notNull(trade1, "trade1");
        notNull(trade1, "trade1");
        checkArgument(trade1.getTradeKey().equals(trade2.getTradeKey()),
                      "tradeKeys not equal");

        final Types.TradeKey tradeKey = trade1.getTradeKey();
        final Types.Quantity mergedQty = trade1.getQuantity().plus(trade2.getQuantity());

        return Trade.newTrade(tradeKey, mergedQty);
    }

}
