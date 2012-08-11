/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.exec;

/**
 * Implementations of this interface will provide some notion of monotonically increasing time.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public interface TimeSource {

    /**
     * Gets the current time. The implementations must guarantee that two subsequent calls will retrieve two
     * different values, and the second value will be strictly greater than the first value.
     *
     * @return the current time.
     */
    long getTime();
}
