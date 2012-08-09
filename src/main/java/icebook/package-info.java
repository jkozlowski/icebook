/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

/**
 * icebook is a simple simulator for SETS iceberg order book in Java. The application will read new orders
 * from {@code stdin}, apply them to the order book and output to {@code stdout} any generated trades,
 * followed by a complete dump of the current order book.
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
 * <p>
 * Timestamps are only treated as UNIX timestamps stored in {@code long} values.
 * </p>
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
package icebook;

