package icebook;

import org.junit.Test;

import static icebook.OrderParser.tryParseOrder;
import static icebook.Types.Side;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public final class OrderParserTest {

    @Test
    public void testParseIcebergOrder() {
        assertEquals(tryParseOrder("B,100345,5103,100000,10000"),
                     newIcebergOrder(Side.BUY, 100345, (short) 5103, 100000,
                                     10000));
        assertEquals(tryParseOrder("S,100000,5102,10000,1345"),
                     newIcebergOrder(Side.SELL, 100000, (short) 5102, 10000,
                                     1345));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIcebergOrderPeakGreaterThanQuantity() {
        tryParseOrder("B,100345,5103,1000,10000");
    }

    @Test
    public void testLimitOrderParser() {
        assertEquals(tryParseOrder("B,100322,5103,7500"), newLimitOrder(
                Side.BUY, 100322, (short) 5103, 7500));
        assertEquals(tryParseOrder("S,5103,7500,100322"), newLimitOrder(
                Side.SELL, 5103, (short) 7500, 100322));
    }

    @Test
    public void testCommentParser() {
        assertNull(tryParseOrder("  \n  # "));
        assertNull(tryParseOrder("   # "));
        assertNull(tryParseOrder("   #asdasd"));
        assertNull(tryParseOrder("#asdasd"));
        assertNull(tryParseOrder("#"));
    }

    @Test
    public void testIgnore() {
        assertNull(tryParseOrder("  \n"));
        assertNull(tryParseOrder("   "));
        assertNull(tryParseOrder(" "));
        assertNull(tryParseOrder("\n"));
        assertNull(tryParseOrder("\t\n\r "));
        assertNull(tryParseOrder(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSide() {
        tryParseOrder("F,100000,5102,10000,1345");
    }

    private static Order newLimitOrder(final Side side, final int orderId,
                                       final short price, final int quantity) {
        return Order.newLimitOrder(side, Types.orderId(orderId), Types
                .price(price), Types.quantity(quantity));
    }

    private static Order newIcebergOrder(final Side side, final int orderId,
                                         final short price, final int quantity,
                                         final int peakSize) {
        return Order.newIcebergOrder(side, Types.orderId(orderId), Types
                .price(price), Types.quantity(quantity), Types
                                             .quantity(peakSize));
    }
}
