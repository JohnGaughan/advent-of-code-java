/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
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
 * Represents a predicate of two {@code int}-valued argument. Core Java lacks in extensive functional interfaces for
 * primitives beyond the basics. This interface helps avoid unnecessary boxing which can affect performance when done in
 * a loop with many iterations which happens in AOC solutions.
 */
@FunctionalInterface
public interface BiIntPredicate {

  boolean test(int value1, int value2);

  default BiIntPredicate and(final BiIntPredicate other) {
    Objects.requireNonNull(other);
    return (value1, value2) -> test(value1, value2) && other.test(value1, value2);
  }

  default BiIntPredicate negate() {
    return (value1, value2) -> !test(value1, value2);
  }

  default BiIntPredicate or(final BiIntPredicate other) {
    Objects.requireNonNull(other);
    return (value1, value2) -> test(value1, value2) || other.test(value1, value2);
  }

}
