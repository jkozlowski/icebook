package icebook;

import org.junit.Test;

import java.util.Formatter;

import static icebook.OutputFormatter.*;
import static icebook.Utils.checkArgument;

/**
 * Test the formatting strings against the spec.
 */
public final class OutputFormatterTest {

    // as per spec
    private static final int LINE_LENGTH_FORMATTED = 67;

    // as per spec + 2 for newline formatter
    private static final int LINE_LENGTH_EMPTY = LINE_LENGTH_FORMATTED + 2;

    @Test
    public void testHeader() {
        for (final String s : HEADER.split(NEWLINE)) {
            checkLengthFormatter(s + NEWLINE);
        }
    }

    @Test
    public void testFullLine() {
        final Formatter f = formatter(new StringBuilder());
        f.format(DATA_LINE_BUYS + DATA_LINE_SELLS,
                 1234567890,
                 1234567890,
                 123456,
                 123456,
                 1234567890,
                 1234567890
                );
        checkLengthFormatted(f.toString());
    }

    @Test
    public void testOnlyBuy() {
        final Formatter f = formatter(new StringBuilder());
        f.format(DATA_LINE_BUYS + EMPTY_LINE_SELLS,
                 1234567890,
                 1234567890,
                 123456
                );
        checkLengthFormatted(f.toString());
    }

    @Test
    public void testOnlySell() {
        final Formatter f = formatter(new StringBuilder());
        f.format(EMPTY_LINE_BUYS + DATA_LINE_SELLS,
                 123456,
                 1234567890,
                 1234567890
                );
        checkLengthFormatted(f.toString());
    }

    @Test
    public void testEmptyLine() {
        checkLengthFormatter(EMPTY_LINE_BUYS + EMPTY_LINE_SELLS + NEWLINE);
    }

    @Test
    public void testTrailer() {
        checkLengthFormatter(TRAILER);
    }

    private static void checkLengthFormatter(final String s) {
        checkArgument(s.length() == LINE_LENGTH_EMPTY,
                      "\"" + s + "\" is of wrong length");
    }

    private static void checkLengthFormatted(final String s) {
        checkArgument(s.length() == LINE_LENGTH_FORMATTED,
                      "\"" + s + "\" is of wrong length");
    }

}
