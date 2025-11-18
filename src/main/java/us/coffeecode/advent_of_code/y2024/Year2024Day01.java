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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 1)
@Component
public class Year2024Day01 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);
    return IntStream.range(0, input.left.length)
                    .mapToLong(i -> Math.abs(input.left[i] - input.right[i]))
                    .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final Map<Long, Long> occurrences = new HashMap<>();
    Arrays.stream(input.right)
          .forEach(v -> occurrences.merge(Long.valueOf(v), ONE, Math::addExact));
    return Arrays.stream(input.left)
                 .map(v -> v * occurrences.getOrDefault(Long.valueOf(v), ZERO)
                                          .longValue())
                 .sum();
  }

  private Input getInput(final PuzzleContext pc) {
    long[][] data = il.linesAs2dLongArrayFromSplit(pc, SPLIT);
    long[] left = Arrays.stream(data)
                        .mapToLong(a -> a[0])
                        .sorted()
                        .toArray();
    long[] right = Arrays.stream(data)
                         .mapToLong(a -> a[1])
                         .sorted()
                         .toArray();
    return new Input(left, right);
  }

  private static final Pattern SPLIT = Pattern.compile("\\s+");

  private static final Long ZERO = Long.valueOf(0);

  private static final Long ONE = Long.valueOf(1);

  private record Input(long[] left, long[] right) {}
}
