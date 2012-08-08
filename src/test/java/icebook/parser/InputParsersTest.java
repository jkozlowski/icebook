/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.parser;

import org.codehaus.jparsec.Parser;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

/**
 * Tests {@link InputParsers}
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class InputParsersTest {

    @Test
    public void testCommentParser() {
        final Parser<?> comment = InputParsers.comment();
        assertThat(comment.parse("  \n  # "), nullValue());
        assertThat(comment.parse("   # "), nullValue());
        assertThat(comment.parse("   #asdasd"), nullValue());
    }
}
