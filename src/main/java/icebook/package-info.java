/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

/**
 * icebook is a simple simulator for SETS iceberg order book in Java. The application will read new orders
 * from {@code stdin}, apply them to the order book and output to {@code stdout} any generated trades in the format
 * defined by {@link Appenders#append(Appendable, Collection)}, followed by the current state of the book in the
 * format defined by {@link Appenders#append(Appendable, SortedSet, SortedSet)}). Any errors will be printed to
 * {@code stderr}.
 *
 * <p>Should the input not conform to the grammar defined in {@link Parsers}, the system will output an error
 * message and exit with an error code.</p>
 *
 * <p>
 * To simplify the design, the simulator does not explicitly deal with particular instruments (i.e. it does not have
 * a notion of an <em>FB.O</em> stock), but rather all orders are implicitly assumed to concern a single, but
 * unspecified stock instrument.
 * </p>
 *
 * <p>
 * Similarly, prices are assumed to be quoted in pennies and sub-penny prices are prohibited; hence,
 * prices are simply assumed to be {@code long} values.
 * </p>
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
package icebook;

import icebook.input.Parsers;
import icebook.output.Appenders;

import java.util.Collection;
import java.util.SortedSet;
