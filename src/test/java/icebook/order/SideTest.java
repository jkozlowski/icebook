/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link Side}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class SideTest {

    @Test
    public void testGetOpposite() {
        assertThat(Side.SELL.getOpposite(), is(Side.BUY));
        assertThat(Side.BUY.getOpposite(), is(Side.SELL));
    }

    @Test
    public void testIsSell() {
        assertThat(Side.SELL.isSell(), is(Boolean.TRUE));
        assertThat(Side.BUY.isSell(), is(Boolean.FALSE));
    }

    @Test
    public void testIsBuy() {
        assertThat(Side.SELL.isBuy(), is(Boolean.FALSE));
        assertThat(Side.BUY.isBuy(), is(Boolean.TRUE));
    }
}
