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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 25, title = "Code Chronicle")
@Component
public class Year2024Day25 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final long[] input = getInput(pc);
    long result = 0;
    for (int i = 0; i < input.length - 1; ++i) {
      for (int j = i + 1; j < input.length; ++j) {
        if ((input[i] & input[j]) == 0) {
          ++result;
        }
      }
    }
    return result;
  }

  /** For each group of lines, get a long that represents the # characters in its bits. */
  private long[] getInput(final PuzzleContext pc) {
    return il.groups(pc)
             .stream()
             .mapToLong(this::convert)
             .toArray();
  }

  /** Given strings that form an input group, convert them to a long where # characters indicate bits that are set. */
  private long convert(final Iterable<String> group) {
    long value = 0;
    for (final String line : group) {
      for (final int codePoint : line.codePoints()
                                     .toArray()) {
        value <<= 1;
        if (codePoint == '#') {
          ++value;
        }
      }
    }
    return value;
  }
}
