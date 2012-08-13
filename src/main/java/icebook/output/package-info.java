/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

/**
 * Defines the methods for appending to {@link Appendable} representations of {@link SortedSet}s of {@link Entry}ies,
 * as well as {@link Collection}s of {@link Trade}s.
 *
 * <p>{@link Appenders#append(Appendable, Collection)} merges the {@link Trade}s trades between the same id's; the
 * decision to do it this way, instead of returning a merged collection of {@link Trade}s directly from {@link
 * ExecutionEngine} is for educational purposes only: I simply wanted to use the {@link BiMap} and {@link Equivalence}
 * from {@code guava}.</p>
 *
 * <p> If I wanted to return a merged collection, I would probably want to try to use another cool
 * feature of {@code guava}: {@link ForwardingSortedMap} of some sort, because it makes decorating collections really
 * easy.</p>
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
package icebook.output;

import com.google.common.base.Equivalence;
import com.google.common.collect.BiMap;
import com.google.common.collect.ForwardingSortedMap;
import icebook.exec.ExecutionEngine;
import icebook.exec.Trade;

import java.util.Collection;
import java.util.SortedSet;

import static icebook.book.OrderBook.Entry;
