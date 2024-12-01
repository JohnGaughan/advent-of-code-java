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
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 1, title = "Historian Hysteria")
@Component
public class Year2024Day01 {

  private static final Pattern SPLIT = Pattern.compile("\\s+");

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    Input input = getInput(pc);
    long result = 0;
    for (int i = 0; i < input.left.length; ++i) {
      result += Math.abs(input.left[i] - input.right[i]);
    }
    return result;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    Input input = getInput(pc);
    long result = 0;
    for (long value : input.left) {
      long matches = Arrays.stream(input.right).filter(v -> v == value).count();
      result += (value * matches);
    }
    return result;
  }

  /** Split the file line into two longs. */
  private long[] split(final String line) {
    final String[] tokens = SPLIT.split(line);
    return new long[] { Long.parseLong(tokens[0]), Long.parseLong(tokens[1]) };
  }

  private long[] halve(final long[][] array, final int idx) {
    long[] result = new long[array.length];
    for (int i = 0; i < array.length; ++i) {
      result[i] = array[i][idx];
    }
    return result;
  }

  private Input getInput(final PuzzleContext pc) {
    long[][] data = il.linesAs2dLongArray(pc, this::split);
    long[] left = halve(data, 0);
    long[] right = halve(data, 1);
    Arrays.sort(left);
    Arrays.sort(right);
    return new Input(left, right);
  }

  private record Input(long[] left, long[] right) {}
}
