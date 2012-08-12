/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

/**
 * Defines the parser for {@link Order}s. The decision to make use of the <em>JParsec</em> library was made for
 * educational purposes only, consciously ignoring any potential performance issues. The API is consciously kept very
 * narrow: in fact only a single method is actually exposed: {@link Parsers#newOrderParser()}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
package icebook.input;

import icebook.order.Order;
