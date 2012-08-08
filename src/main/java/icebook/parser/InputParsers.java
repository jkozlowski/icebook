/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.parser;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.pattern.CharPredicates;

/**
 * Defines the grammar for parsing the input into new orders.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class InputParsers {

    /**
     * Gets a {@link Parser} for comments.
     *
     * @return a {@link Parser} for comments.
     */
    public static final Parser<?> comment() {
        return Scanners.WHITESPACES.next(Scanners.isChar('#')).next(Scanners.isChar(CharPredicates.ALWAYS).many_());
    }
}
