/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.y2023;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MyArrays;

@AdventOfCodeSolution(year = 2023, day = 9)
@Component
public class Year2023Day09 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final long[][] input = il.linesAs2dLongArray(pc, s -> Arrays.stream(SPLIT.split(s))
                                                                .mapToLong(Long::parseLong)
                                                                .toArray());
    return Arrays.stream(input)
                 .mapToLong(this::nextValue)
                 .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final long[][] input = il.linesAs2dLongArray(pc, s -> Arrays.stream(SPLIT.split(s))
                                                                .mapToLong(Long::parseLong)
                                                                .toArray());
    final long[][] reversed = Arrays.stream(input)
                                    .map(MyArrays::reverse)
                                    .toArray(long[][]::new);
    return Arrays.stream(reversed)
                 .mapToLong(this::nextValue)
                 .sum();
  }

  private long nextValue(final long[] values) {
    // Calculate the next level down.
    final long[] diffs = new long[values.length - 1];
    for (int i = 0; i < diffs.length; ++i) {
      diffs[i] = values[i + 1] - values[i];
    }

    // Next level is all zeros; the next value at the current level is simply any (equal) value at the current level.
    if (!Arrays.stream(diffs)
               .filter(n -> n != 0)
               .findAny()
               .isPresent()) {
      return values[0];
    }

    // Otherwise, take the last value at the current level and add it with the next value at the next level.
    return values[values.length - 1] + nextValue(diffs);
  }

  /** Pattern that splits numbers on a line in the input file. */
  private static final Pattern SPLIT = Pattern.compile(" ");
}
