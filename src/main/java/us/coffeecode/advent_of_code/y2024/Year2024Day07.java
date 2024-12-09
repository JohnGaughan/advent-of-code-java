/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 7, title = "Bridge Repair")
@Component
public class Year2024Day07 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, List.of(Operator.ADD, Operator.MULTIPLY));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, List.of(Operator.ADD, Operator.MULTIPLY, Operator.CONCAT));
  }

  private long calculate(final PuzzleContext pc, final Iterable<Operator> operators) {
    return il.linesAsObjects(pc, Equation::valueOf)
             .stream()
             .filter(eq -> hasSolution(eq, operators))
             .mapToLong(Equation::testValue)
             .sum();
  }

  /** Check if the equation has a solution: base case. */
  private boolean hasSolution(final Equation eq, final Iterable<Operator> operators) {
    // Prime the pump by setting the accumulator equal to the first value, then call the recursive function.
    return hasSolution(eq, operators, 1, eq.operands[0]);
  }

  /** Check if the equation has a solution: recursive case. */
  private boolean hasSolution(final Equation eq, final Iterable<Operator> operators, final int position, final long accumulator) {
    // Check end state
    if (position >= eq.operands.length) {
      return (accumulator == eq.testValue);
    }
    // Short-circuit: this branch is a dead end. Accumulator can only increase.
    if (accumulator > eq.testValue) {
      return false;
    }
    // Try all operators at the current position
    for (final Operator op : operators) {
      final long nextAccumulator = op.apply(accumulator, eq.operands[position]);
      // Once we find a single solution we can stop checking to save time.
      if (hasSolution(eq, operators, position + 1, nextAccumulator)) {
        return true;
      }
    }
    return false;
  }

  /** Represents ways to combine two operands into a single result value. */
  private enum Operator {

    ADD {

      @Override
      public long apply(final long op1, final long op2) {
        return op1 + op2;
      }
    },
    MULTIPLY {

      @Override
      public long apply(final long op1, final long op2) {
        return op1 * op2;
      }
    },
    CONCAT {

      @Override
      public long apply(final long op1, final long op2) {
        return Long.parseLong(Long.toString(op1) + Long.toString(op2));
      }
    };

    public abstract long apply(long op1, long op2);
  }

  /** Represents one line of input, split into the test value (first value) and the operands (all other values). */
  private record Equation(long testValue, long[] operands) {

    private static final Pattern SPLIT = Pattern.compile(":?\\s");

    /** Given a single line of text in the input file, convert it to an equation. */
    public static Equation valueOf(final String line) {
      final String[] tokens = SPLIT.split(line);
      final long[] numbers = Arrays.stream(tokens)
                                   .mapToLong(Long::parseLong)
                                   .toArray();
      return new Equation(numbers[0], Arrays.copyOfRange(numbers, 1, numbers.length));
    }
  }
}
