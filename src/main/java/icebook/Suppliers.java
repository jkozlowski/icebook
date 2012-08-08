/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook;

import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * {@link InputSupplier}s for {@link System#in}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class Suppliers {

    private Suppliers() {
    }

    public static InputSupplier<InputStreamReader> newStdinInputSupplier() {
        return CharStreams.newReaderSupplier(new InputSupplier<InputStream>() {
            @Override
            public InputStream getInput() throws IOException {
                return System.in;
            }
        }, Charset.defaultCharset());
    }
}
