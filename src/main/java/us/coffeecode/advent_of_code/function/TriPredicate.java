/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package us.coffeecode.advent_of_code.function;

import java.util.Objects;

/**
 * Predicate that accepts three parameters and returns a boolean answer.
 */
@FunctionalInterface
public interface TriPredicate<T, U, V> {

  boolean test(T t, U u, V v);

  default TriPredicate<T, U, V> and(final TriPredicate<? super T, ? super U, ? super V> other) {
    Objects.requireNonNull(other);
    return (final T t, final U u, final V v) -> test(t, u, v) && other.test(t, u, v);
  }

  default TriPredicate<T, U, V> negate() {
    return (final T t, final U u, final V v) -> !test(t, u, v);
  }

  default TriPredicate<T, U, V> or(final TriPredicate<? super T, ? super U, ? super V> other) {
    Objects.requireNonNull(other);
    return (final T t, final U u, final V v) -> test(t, u, v) || other.test(t, u, v);
  }

}
