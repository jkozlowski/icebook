package icebook;

import static icebook.Utils.notNull;

public final class Trade {

    private final Types.TradeKey tradeKey;
    private final Types.Quantity quantity;

    private Trade(final Types.TradeKey tradeKey,
                  final Types.Quantity quantity) {
        notNull(tradeKey, "tradeKey");
        notNull(quantity, "quantity");
        this.tradeKey = tradeKey;
        this.quantity = quantity;
    }

    public Types.TradeKey getTradeKey() {
        return tradeKey;
    }

    public Types.Quantity getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Trade trade = (Trade) o;

        if (!quantity.equals(trade.quantity)) {
            return false;
        }
        if (!tradeKey.equals(trade.tradeKey)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = tradeKey.hashCode();
        result = 31 * result + quantity.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradeKey=" + tradeKey +
                ", quantity=" + quantity +
                '}';
    }

    public static Trade newTrade(final Types.TradeKey tradeKey,
                                 final Types.Quantity quantity) {
        return new Trade(tradeKey, quantity);
    }

    public static Trade newTrade(final Order order1,
                                 final Order order2,
                                 final Types.Price price,
                                 final Types.Quantity quantity) {
        return new Trade(Types.tradeKey(price, order1, order2),
                         quantity);
    }
}
