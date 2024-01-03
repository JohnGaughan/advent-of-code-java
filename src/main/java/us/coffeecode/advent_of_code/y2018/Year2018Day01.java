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
package us.coffeecode.advent_of_code.y2018;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 1, title = "Chronal Calibration")
@Component
public final class Year2018Day01 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return Arrays.stream(il.linesAsLongs(pc)).sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    long frequency = 0;
    final Set<Long> seen = new HashSet<>(1 << 17);
    final long[] input = il.linesAsLongs(pc);
    outer: while (true) {
      for (final long change : input) {
        final Long key = Long.valueOf(frequency);
        if (seen.contains(key)) {
          break outer;
        }
        seen.add(key);
        frequency += change;
      }
    }
    return frequency;
  }

}
