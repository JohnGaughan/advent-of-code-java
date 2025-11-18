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
package us.coffeecode.advent_of_code.y2020;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 5)
@Component
public final class Year2020Day05 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final long[] values = getInput(pc);
    return values[values.length - 1];
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final long[] ids = getInput(pc);
    for (int i = 0; i < ids.length; ++i) {
      final long next = ids[i] + 1;
      if (ids[i + 1] != next) {
        return next;
      }
    }
    return 0;
  }

  /** Get the input data for this solution. */
  private long[] getInput(final PuzzleContext pc) {
    return il.lines(pc)
             .stream()
             .mapToLong(s -> Long.parseLong(s.replace('F', '0')
                                             .replace('B', '1')
                                             .replace('L', '0')
                                             .replace('R', '1'),
               2))
             .sorted()
             .toArray();
  }

}
