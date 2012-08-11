/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.exec;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Default implementation of {@link TimeSource} that provides a strictly increasing sequence that starts at {@code 0}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class AtomicLongTimeSource implements TimeSource {

    private final AtomicLong time = new AtomicLong(0);

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTime() {
        return time.incrementAndGet();
    }
}
