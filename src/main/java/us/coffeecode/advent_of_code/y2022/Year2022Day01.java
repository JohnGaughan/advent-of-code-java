/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 1)
@Component
public class Year2022Day01 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final long[] c = getCaloriesPerElf(pc);
    return c[c.length - 1];
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final long[] c = getCaloriesPerElf(pc);
    return c[c.length - 1] + c[c.length - 2] + c[c.length - 3];
  }

  /** Get the sum of the calories each elf carries. */
  private long[] getCaloriesPerElf(final PuzzleContext pc) {
    final long[] elves = il.groupsAsLongs(pc, l -> l.stream()
                                                    .mapToLong(Long::parseLong)
                                                    .sum());
    Arrays.sort(elves);
    return elves;
  }

}
