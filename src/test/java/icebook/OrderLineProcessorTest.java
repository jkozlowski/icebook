/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook;

import org.testng.annotations.Test;

/**
 * Tests {@link OrderLineProcessor}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class OrderLineProcessorTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullOut() {
        new OrderLineProcessor(null, new StringBuilder());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullErr() {
        new OrderLineProcessor(new StringBuilder(), null);
    }


}
