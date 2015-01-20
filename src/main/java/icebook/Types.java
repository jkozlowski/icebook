package icebook;

import static icebook.Utils.*;

/**
 * Types used in multiple places.
 *
 * <p>
 * These were introduced because multiple classes use those types and they have
 * fairly strictly defined domains; therefore, in order to not have to
 * validate them in multiple places, they were abstracted away into simple
 * wrappers.
 * </p>
 *
 * <p>This does increase the line noise a little, because defining new types
 * in Java is somewhat expensive, however I believe this to be a valid
 * trade-off even in this simple application.
 * </p>
 */
public final class Types {

    private Types() {
        privateConstructor(getClass());
    }

    /**
     * Possible sides of an {@link icebook.Order}.
     */
    public enum Side {

        BUY, SELL;

        public boolean isBuy() {
            return BUY.equals(this);
        }

        public boolean isSell() {
            return SELL.equals(this);
        }

        public Side opposite() {
            return isSell() ? BUY : SELL;
        }
    }

    /**
     * Introduced as a separate type for readability because it is created in
     * multiple places and has the same underlying type as quantity.
     */
    public static final class OrderId {

        private final int orderId;

        /**
         * Default constructor.
         *
         * @param orderId
         *         orderId to use (must be {@code > 0})
         *
         * @throws IllegalArgumentException
         *         is {@code orderId <= 0}
         */
        private OrderId(final int orderId) {
            checkArgument(orderId > 0, "orderId must be > 0");
            this.orderId = orderId;
        }

        public int value() {
            return orderId;
        }

        @Override
        public boolean equals(final Object o) {

            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final OrderId orderId1 = (OrderId) o;

            return orderId == orderId1.orderId;
        }

        @Override
        public int hashCode() {
            return orderId;
        }

        @Override
        public String toString() {
            return "OrderId{" +
                    "orderId=" + orderId +
                    '}';
        }
    }

    public static final class Price implements Comparable<Price> {

        private final short price;

        private Price(final short price) {
            checkArgument(price > 0, "price must be >0");
            this.price = price;
        }

        public short value() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Price price1 = (Price) o;

            return price == price1.price;
        }

        @Override
        public int hashCode() {
            return (int) price;
        }

        @Override
        public String toString() {
            return "Price{" +
                    "price=" + price +
                    '}';
        }

        /**
         * Follows natural ordering of shorts.
         *
         * {@inheritDoc}
         */
        @Override
        public int compareTo(final Price other) {
            notNull(other, "other");
            return Short.compare(this.price, other.price);
        }
    }

    public static final class Quantity {

        private final int quantity;

        private Quantity(final int quantity) {
            checkArgument(quantity >= 0, "quantity must be >= 0");
            this.quantity = quantity;
        }

        public int value() {
            return quantity;
        }

        public boolean leq(final Quantity other) {
            notNull(other, "other");
            return this.quantity <= other.quantity;
        }

        public Quantity min(final Quantity other) {
            return this.quantity <= other.quantity ? this : other;
        }

        public boolean isZero() {
            return this.quantity == 0;
        }

        /**
         * Decreases this quantity by the other quantity.
         *
         * @param other
         *         quantity to decrease by
         *
         * @return new quantity
         *
         * @throws NullPointerException
         *         if {@code other} is null
         * @throws IllegalArgumentException
         *         if {@code other} is larger
         *         than this quantity
         */
        public Quantity minus(final Quantity other) {
            notNull(other, "other");
            checkArgument(quantity >= other.quantity, "cannot decrease " +
                    "by more than current quantity");
            return new Quantity(quantity - other.quantity);
        }

        public Quantity plus(final Quantity other) {
            notNull(other, "other");
            return new Quantity(quantity + other.quantity);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Quantity quantity1 = (Quantity) o;

            return quantity == quantity1.quantity;
        }

        @Override
        public int hashCode() {
            return quantity;
        }

        @Override
        public String toString() {
            return "Quantity{" +
                    "quantity=" + quantity +
                    '}';
        }
    }

    public static final class TradeKey {

        private final OrderId buyOrderId;
        private final OrderId sellOrderId;
        private final Price price;

        private TradeKey(final Price price, final Order order1,
                         final Order order2) {
            notNull(price, "price");
            notNull(order1, "order1");
            notNull(order2, "order2");
            checkArgument(order1.isOpposite(order2),
                          "orders are not of opposite side");
            this.price = price;
            buyOrderId = order1.getSide().isBuy() ? order1.getOrderId() : order2
                    .getOrderId();
            sellOrderId = order1.getSide().isSell() ? order1.getOrderId() :
                    order2.getOrderId();
        }

        public OrderId getBuyOrderId() {
            return buyOrderId;
        }

        public OrderId getSellOrderId() {
            return sellOrderId;
        }

        public Price getPrice() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final TradeKey tradeKey = (TradeKey) o;

            if (!buyOrderId.equals(tradeKey.buyOrderId)) {
                return false;
            }
            if (!price.equals(tradeKey.price)) {
                return false;
            }
            if (!sellOrderId.equals(tradeKey.sellOrderId)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = buyOrderId.hashCode();
            result = 31 * result + sellOrderId.hashCode();
            result = 31 * result + price.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "TradeKey{" +
                    "buyOrderId=" + buyOrderId +
                    ", sellOrderId=" + sellOrderId +
                    ", price=" + price +
                    '}';
        }
    }

    public static OrderId orderId(final int orderId) {
        return new OrderId(orderId);
    }

    public static Price price(final short price) {
        return new Price(price);
    }

    public static Quantity quantity(final int quantity) {
        return new Quantity(quantity);
    }

    public static Quantity nonZeroQuantity(final int quantity) {
        checkArgument(quantity > 0, "quantity must be > 0");
        return new Quantity(quantity);
    }

    public static TradeKey tradeKey(final Price price, final Order order1,
                                    final Order order2) {
        return new TradeKey(price, order1, order2);
    }
}
