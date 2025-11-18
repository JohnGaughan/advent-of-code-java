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

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 21)
@Component
public final class Year2018Day21 {

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

  private static final Pattern SPLIT = Pattern.compile(" ");

  private long calculate(final PuzzleContext pc) {
    final long magicNumber = Long.parseLong(SPLIT.split(il.lines(pc)
                                                          .get(8))[1]);
    final boolean executeFewest = pc.getBoolean("ExecuteFewest");
    long d = 0;
    final Set<Long> seen = new HashSet<>();
    long previous_d = -1;
    while (true) {
      long c = (d | 65536);
      d = magicNumber;
      while (true) {
        d += (c & 255);
        d &= 16_777_215;
        d *= 65_899;
        d &= 16_777_215;
        if (256 > c) {
          if (executeFewest) {
            return d;
          }
          else {
            final Long value = Long.valueOf(d);
            if (seen.contains(value)) {
              return previous_d;
            }
            else {
              seen.add(value);
              previous_d = d;
              break;
            }
          }
        }
        c >>= 8;
      }
    }
  }

}
