/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2025  John Gaughan
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
package us.coffeecode.advent_of_code.y2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2025, day = 6)
@Component
public class Year2025Day06 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  public long calculate(final PuzzleContext pc) {
    return getInput(pc).stream()
                       .mapToLong(p -> Arrays.stream(p.operands)
                                             .reduce(p.reducer)
                                             .getAsLong())
                       .sum();
  }

  /**
   * Get the problem input, which is where most of the work for today's puzzle is performed. This is an ugly mess
   * because the input format is an ugly mess.
   */
  private List<Problem> getInput(final PuzzleContext pc) {
    // The goofy spacing in the file matters here, so don't use more "advanced" methods to load input.
    final List<String> lines = il.lines(pc);
    final String operators = lines.getLast();
    final int[] problemOffsets = getOperatorIndices(operators);
    final List<Problem> problems = new ArrayList<>(problemOffsets.length);
    for (int i = 0; i < problemOffsets.length; ++i) {
      final boolean last = (i == problemOffsets.length - 1);
      final int start = problemOffsets[i];
      final int end = (last ? operators.length() : problemOffsets[i + 1]);
      final long[] operands;
      if (pc.getPart() == 1) {
        operands = new long[lines.size() - 1];
        for (int j = 0; j < operands.length; ++j) {
          operands[j] = Long.parseLong(lines.get(j)
                                            .substring(start, end)
                                            .trim());
        }
      }
      else {
        // I don't like this, but the string parsing is different between parts which affects the edge case of the final
        // column.
        operands = new long[end - start - (last ? 0 : 1)];
        for (int j = 0; j < operands.length; ++j) {
          final StringBuilder str = new StringBuilder();
          for (int k = 0; k < lines.size() - 1; ++k) {
            str.appendCodePoint(lines.get(k)
                                     .codePointAt(start + j));
          }
          operands[j] = Long.parseLong(str.toString()
                                          .trim());
        }
      }
      problems.add(new Problem(operands, OPS.get(Integer.valueOf(operators.codePointAt(start)))));
    }
    return problems;
  }

  /**
   * Get the location of all operators in the string. This is useful to pre-cache because the other input lines are
   * horizontal-aligned on this column.
   */
  private int[] getOperatorIndices(final String line) {
    final IntStream.Builder indices = IntStream.builder();
    for (int i = 0; i < line.length(); ++i) {
      if (!Character.isWhitespace(line.codePointAt(i))) {
        indices.accept(i);
      }
    }
    return indices.build()
                  .toArray();
  }

  /** Represents one math problem in the input. */
  private record Problem(long[] operands, LongBinaryOperator reducer) {}

  /**
   * Map the character used in the input for each operation to a lambda that will be used to reduce a stream of numbers
   * in the way that operation works.
   */
  private static final Map<Integer, LongBinaryOperator> OPS =
    Map.of(Integer.valueOf('+'), (a, b) -> a + b, Integer.valueOf('*'), (a, b) -> a * b);
}
