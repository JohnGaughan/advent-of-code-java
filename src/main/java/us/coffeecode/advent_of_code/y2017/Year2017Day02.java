/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2017;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 2)
@Component
public final class Year2017Day02 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    long checksum = 0;
    for (final int[] row : il.linesAs2dIntArrayFromSplit(pc, SEPARATOR)) {
      final long min = Arrays.stream(row)
                             .min()
                             .getAsInt();
      final long max = Arrays.stream(row)
                             .max()
                             .getAsInt();
      checksum += max - min;
    }
    return checksum;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    long checksum = 0;
    for (final int[] row : il.linesAs2dIntArrayFromSplit(pc, SEPARATOR)) {
      Arrays.sort(row);
      outer: for (int i = 0; i < row.length - 1; ++i) {
        for (int j = i + 1; j < row.length; ++j) {
          if (row[j] % row[i] == 0) {
            checksum += row[j] / row[i];
            break outer;
          }
        }
      }
    }
    return checksum;
  }

  private static final Pattern SEPARATOR = Pattern.compile("\\s");
}
