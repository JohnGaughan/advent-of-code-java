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

@AdventOfCodeSolution(year = 2020, day = 1)
@Component
public final class Year2020Day01 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[] values = il.linesAsIntsSorted(pc);
    for (int i = 0; i < values.length; ++i) {
      final int i1 = values[i];
      for (int j = i + 1; j < values.length; ++j) {
        final int j1 = values[j];
        final int sum = i1 + j1;
        if (sum == 2020) {
          return (long) i1 * j1;
        }
        else if (sum > 2020) {
          continue;
        }
      }
    }
    return 0;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[] values = il.linesAsIntsSorted(pc);
    for (int i = 0; i < values.length; ++i) {
      final int i1 = values[i];
      for (int j = i + 1; j < values.length; ++j) {
        final int j1 = values[j];
        for (int k = j + 1; k < values.length; ++k) {
          final int k1 = values[k];
          final int sum = i1 + j1 + k1;
          if (sum == 2020) {
            return (long) i1 * j1 * k1;
          }
          else if (sum > 2020) {
            continue;
          }
        }
        if (i1 + j1 > 2020) {
          continue;
        }
      }
    }
    return 0;
  }

}
