/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.formatter;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link Formatters}
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class FormattersTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testAppendTimesNullSb() {
        Formatters.appendTimes(null, '-', 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAppendTimesWrongTimes() {
        Formatters.appendTimes(new StringBuilder(), '-', 0);
    }

    @Test
    public void testAppendTimes() {
        assertThat(Formatters.appendTimes(new StringBuilder(), '.', 5).toString(), is("....."));
    }
}
