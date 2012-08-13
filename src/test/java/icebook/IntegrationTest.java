/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook;

import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;
import com.google.common.io.LineProcessor;
import icebook.book.OrderBooks;
import icebook.exec.ExecutionEngine;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests the integration of the whole system, by using a {@link OrderLineProcessor} to parse input and check expected
 * err and out.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class IntegrationTest {

    private static final String ENGINES_PROVIDER = "execution-engines-data-provider";

    @DataProvider(name = ENGINES_PROVIDER)
    public static ExecutionEngine[][] getOrderBooks() {
        final ExecutionEngine[][] engines = new ExecutionEngine[2][1];
        engines[0][0] = new ExecutionEngine(OrderBooks.newDefaultOrderBook());
        engines[1][0] = new ExecutionEngine(OrderBooks.newOptimisedOrderBook());
        return engines;
    }

    @Test(dataProvider = ENGINES_PROVIDER)
    public void testIntegration(final ExecutionEngine engine) throws IOException {
        final String input
                = "# BUYS\n"
                + "B,1,99,50000\n"
                + "B,2,98,25500\n"
                + "\n"
                + "# SELLS\n"
                + "S,3,100,10000\n"
                + "S,4,100,7500\n"
                + "S,5,101,20000\n"
                + "\n"
                + "# Iceberg Order\n"
                + "B,6,100,100000,10000\n"
                + "\n"
                + "# Aggressive SELL LIMIT\n"
                + "S,7,100,10000\n"
                + "S,8,100,11000\n"
                + "\n"
                + "# Second Iceberg Order\n"
                + "B,9,100,50000,20000\n"
                + "\n"
                + "# Now aggressive SELL LIMIT enters book\n"
                + "S,10,100,35000";

        final String expectedOut
                = "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|         1|       50,000|     99|       |             |          |\n"
                + "+-----------------------------------------------------------------+\n"
                + "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|         1|       50,000|     99|       |             |          |\n"
                + "|         2|       25,500|     98|       |             |          |\n"
                + "+-----------------------------------------------------------------+\n"
                + "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|         1|       50,000|     99|    100|       10,000|         3|\n"
                + "|         2|       25,500|     98|       |             |          |\n"
                + "+-----------------------------------------------------------------+\n"
                + "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|         1|       50,000|     99|    100|       10,000|         3|\n"
                + "|         2|       25,500|     98|    100|        7,500|         4|\n"
                + "+-----------------------------------------------------------------+\n"
                + "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|         1|       50,000|     99|    100|       10,000|         3|\n"
                + "|         2|       25,500|     98|    100|        7,500|         4|\n"
                + "|          |             |       |    101|       20,000|         5|\n"
                + "+-----------------------------------------------------------------+\n"
                + "6,3,100,10000\n"
                + "6,4,100,7500\n"
                + "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|         6|       10,000|    100|    101|       20,000|         5|\n"
                + "|         1|       50,000|     99|       |             |          |\n"
                + "|         2|       25,500|     98|       |             |          |\n"
                + "+-----------------------------------------------------------------+\n"
                + "6,7,100,10000\n"
                + "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|         6|       10,000|    100|    101|       20,000|         5|\n"
                + "|         1|       50,000|     99|       |             |          |\n"
                + "|         2|       25,500|     98|       |             |          |\n"
                + "+-----------------------------------------------------------------+\n"
                + "6,8,100,11000\n"
                + "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|         6|        9,000|    100|    101|       20,000|         5|\n"
                + "|         1|       50,000|     99|       |             |          |\n"
                + "|         2|       25,500|     98|       |             |          |\n"
                + "+-----------------------------------------------------------------+\n"
                + "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|         6|        9,000|    100|    101|       20,000|         5|\n"
                + "|         9|       20,000|    100|       |             |          |\n"
                + "|         1|       50,000|     99|       |             |          |\n"
                + "|         2|       25,500|     98|       |             |          |\n"
                + "+-----------------------------------------------------------------+\n"
                + "9,10,100,20000\n"
                + "6,10,100,15000\n"
                + "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|         6|        4,000|    100|    101|       20,000|         5|\n"
                + "|         9|       20,000|    100|       |             |          |\n"
                + "|         1|       50,000|     99|       |             |          |\n"
                + "|         2|       25,500|     98|       |             |          |\n"
                + "+-----------------------------------------------------------------+\n";

        final String expectedErr
                = "Could not parse line: '# BUYS'\n"
                + "Could not parse line: ''\n"
                + "Could not parse line: '# SELLS'\n"
                + "Could not parse line: ''\n"
                + "Could not parse line: '# Iceberg Order'\n"
                + "Could not parse line: ''\n"
                + "Could not parse line: '# Aggressive SELL LIMIT'\n"
                + "Could not parse line: ''\n"
                + "Could not parse line: '# Second Iceberg Order'\n"
                + "Could not parse line: ''\n"
                + "Could not parse line: '# Now aggressive SELL LIMIT enters book'\n";

        final StringBuilder out = new StringBuilder();
        final StringBuilder err = new StringBuilder();
        final LineProcessor<Void> processor = new OrderLineProcessor(out, err, engine);
        final InputSupplier<StringReader> inputSupplier = new InputSupplier<StringReader>() {
            @Override
            public StringReader getInput() throws IOException {
                return new StringReader(input);
            }
        };
        CharStreams.readLines(inputSupplier, processor);
        assertThat(err.toString(), is(expectedErr));
        assertThat(out.toString(), is(expectedOut));
    }
}
