package icebook;

import static icebook.Utils.checkArgument;
import static icebook.Utils.notNull;

/**
 * Defines either a limit or an iceberg order.
 *
 * <p>For iceberg orders, individual executions must be bounded by the
 * peakSize.</p>
 */
public final class Order {

    private final Types.Side side;
    private final Types.OrderId orderId;
    private final Types.Price price;
    // Nullable
    private final Types.Quantity peakSize;

    // Currently visible quantity
    private Types.Quantity visibleQuantity;
    // Remaining total quantity
    private Types.Quantity remainingQty;

    /**
     * Default constructor.
     *
     * @param side
     *         side of the order
     * @param orderId
     *         id of the order
     * @param price
     *         price of the order
     * @param quantity
     *         original quantity of the order
     * @param peakSize
     *         peakSize of the order (nullable to indicate limit order).
     *         If specified must be {@code <= quantity}
     *
     * @throws NullPointerException
     *         if any argument is null
     * @throws IllegalArgumentException
     *         if peakSize is not {@code <= quantity}
     */
    public Order(final Types.Side side, final Types.OrderId orderId,
                 final Types.Price price, final Types.Quantity quantity,
                 final Types.Quantity peakSize) {
        notNull(side, "side");
        notNull(orderId, "orderId");
        notNull(price, "price");
        notNull(quantity, "quantity");
        checkArgument(peakSize == null || peakSize.leq(quantity),
                      "peakSize must be <= quantity or null");
        this.side = side;
        this.orderId = orderId;
        this.price = price;

        this.remainingQty = quantity;
        this.visibleQuantity = peakSize != null ? peakSize : quantity;
        this.peakSize = peakSize;
    }

    public Types.Side getSide() {
        return side;
    }

    public Types.OrderId getOrderId() {
        return orderId;
    }

    public Types.Price getPrice() {
        return price;
    }

    public Types.Quantity getVisibleQuantity() {
        return visibleQuantity;
    }

    public boolean isOpen() {
        return !remainingQty.isZero();
    }

    public boolean hasVisible() {
        return !visibleQuantity.isZero();
    }

    public boolean isIceberg() {
        return peakSize != null;
    }

    public boolean isOpposite(final Order other) {
        notNull(other, "other");
        return side.opposite().equals(other.getSide());
    }

    /**
     * Executes this quantity.
     *
     * @param toExecute
     *         quantity to execute
     *
     * @throws IllegalArgumentException
     *         if quantity to execute is larger than currently visible
     *         quantity on this order
     */
    public void execute(final Types.Quantity toExecute) {
        notNull(toExecute, "toExecute cannot be null");
        checkArgument(toExecute.leq(visibleQuantity),
                      "trades must happen on visible qty");

        remainingQty = remainingQty.minus(toExecute);
        visibleQuantity = isIceberg() ?
                visibleQuantity.minus(toExecute) :
                remainingQty;
    }

    /**
     * Resets the visible quantity by the peak, if this order is an iceberg.
     */
    public void resetToPeak() {
        if (isIceberg()) {
            visibleQuantity = remainingQty.min(peakSize);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Order order = (Order) o;

        if (!orderId.equals(order.orderId)) {
            return false;
        }
        if (peakSize != null ? !peakSize.equals(order.peakSize) : order
                .peakSize != null) {
            return false;
        }
        if (!price.equals(order.price)) {
            return false;
        }
        if (!remainingQty.equals(order.remainingQty)) {
            return false;
        }
        if (side != order.side) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = side.hashCode();
        result = 31 * result + orderId.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + remainingQty.hashCode();
        result = 31 * result + (peakSize != null ? peakSize.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "side=" + side +
                ", orderId=" + orderId +
                ", price=" + price +
                ", quantity=" + remainingQty +
                ", peakSize=" + peakSize +
                '}';
    }

    public static Order newLimitOrder(final Types.Side side,
                                      final Types.OrderId orderId,
                                      final Types.Price price,
                                      final Types.Quantity quantity) {
        return new Order(side, orderId, price, quantity,
                         null);
    }

    public static Order newIcebergOrder(final Types.Side side,
                                        final Types.OrderId orderId,
                                        final Types.Price price,
                                        final Types.Quantity quantity,
                                        final Types.Quantity peakSize) {
        return new Order(side, orderId, price, quantity, peakSize);
    }
}
