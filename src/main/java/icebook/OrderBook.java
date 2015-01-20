package icebook;

import java.util.*;

import static icebook.Types.Price;
import static icebook.Types.Side;
import static icebook.Utils.checkArgument;
import static icebook.Utils.notNull;

/**
 * Basic order book implementation.
 *
 * <p>Time-precedence is implied, i.e. orders entered later get appended to the
 * end of the queue.</p>
 */
public final class OrderBook {

    private final BookSide buySide;
    private final BookSide sellSide;

    private OrderBook(final BookSide buySide, final BookSide sellSide) {
        this.buySide = notNull(buySide, "buySide");
        this.sellSide = notNull(sellSide, "sellSide");
    }

    public BookSide getBookSide(final Side side) {
        notNull(side, "side");
        return side.isBuy() ? buySide : sellSide;
    }

    public static final class BookSide implements Iterable<Order> {

        /**
         * Just for validation.
         */
        private final Side side;
        private final SortedMap<Price, Deque<Order>> entries;

        private BookSide(final Side side) {
            notNull(side, "side");
            this.side = side;
            entries = createUnderlyingMap(side);
        }

        public Side getSide() {
            return side;
        }

        /**
         * Gets the order at the top of book.
         *
         * @return top of book or null
         */
        public Order topOfBook() {
            if (entries.isEmpty()) {
                return null;
            } else {
                final Price topOfBook = entries.firstKey();
                return entries.get(topOfBook).getFirst();
            }
        }

        /**
         * Inserts the {@code entry} at the end of the queue at this price.
         *
         * @param order
         *         to insert
         *
         * @throws NullPointerException
         *         if {@code order} is null
         * @throws IllegalArgumentException
         *         if order is of the wrong side
         */
        public void insert(final Order order) {
            notNull(order, "order");
            checkArgument(order.getSide().equals(side),
                          "order on the wrong side");
            Deque<Order> queue = entries.get(order.getPrice());
            if (null == queue) {
                queue = new LinkedList<>();
                entries.put(order.getPrice(), queue);
            }
            queue.addLast(order);
        }

        /**
         * Removes the order at the top of the book, if there is any.
         */
        public void removeTopOfBook() {
            if (!entries.isEmpty()) {
                final Price topOfBook = entries.firstKey();
                final Deque<Order> orders = entries.get(topOfBook);
                orders.removeFirst();
                if (orders.isEmpty()) {
                    entries.remove(topOfBook);
                }
            }
        }

        @Override
        public Iterator<Order> iterator() {
            return new Iterator<Order>() {

                private final Iterator<Deque<Order>>
                        i = entries.values().iterator();
                private Iterator<Order> orders;

                @Override
                public boolean hasNext() {
                    if (null == orders) {
                        return i.hasNext();
                    } else {
                        return orders.hasNext();
                    }
                }

                @Override
                public Order next() {
                    if (null == orders) {
                        orders = i.next().iterator();
                    }
                    final Order next = orders.next();
                    if (!orders.hasNext()) {
                        orders = null;
                    }
                    return next;
                }
            };
        }
    }

    private static SortedMap<Price, Deque<Order>>
    createUnderlyingMap(final Side side) {
        notNull(side, "side");
        return side.isSell() ?
                // Lowest first: Natural ordering on Prices
                new TreeMap<Price, Deque<Order>>() :
                // Highest first: reverse of the natural ordering
                new TreeMap<Price, Deque<Order>>(Collections.reverseOrder());
    }

    public static OrderBook emptyBook() {
        return new OrderBook(new BookSide(Side.BUY), new BookSide(Side.SELL));
    }
}
