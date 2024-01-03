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
 * Represents an operation on a boolean that returns a boolean result. This is a primitive boolean specialization of
 * Java's UnaryOperator, since core Java does not supply a boolean version of this interface.
 */
@FunctionalInterface
public interface BooleanUnaryOperator {

  boolean applyAsBoolean(boolean operand);

  default BooleanUnaryOperator compose(final BooleanUnaryOperator before) {
    Objects.requireNonNull(before);
    return (final boolean b) -> applyAsBoolean(before.applyAsBoolean(b));
  }

  default BooleanUnaryOperator andThen(final BooleanUnaryOperator after) {
    Objects.requireNonNull(after);
    return (final boolean b) -> after.applyAsBoolean(applyAsBoolean(b));
  }

  static BooleanUnaryOperator identity() {
    return b -> b;
  }
}
