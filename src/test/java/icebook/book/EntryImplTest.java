/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import icebook.book.OrderBook.Entry;
import icebook.order.Side;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link EntryImpl}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class EntryImplTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongId() {
        new EntryImpl(0, Side.SELL, 1, 1);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullSide() {
        new EntryImpl(1, null, 1, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongPrice() {
        new EntryImpl(1, Side.SELL, 0, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWrongVolume() {
        new EntryImpl(1, Side.SELL, 1, 0);
    }

    @Test
    public void testConstructor() {
        final Entry entry = new EntryImpl(1, Side.SELL, 12, 130);
        assertThat(entry.getId(), is(1L));
        assertThat(entry.getSide(), is(Side.SELL));
        assertThat(entry.getPrice(), is(12L));
        assertThat(entry.getVolume(), is(130L));
    }
}
