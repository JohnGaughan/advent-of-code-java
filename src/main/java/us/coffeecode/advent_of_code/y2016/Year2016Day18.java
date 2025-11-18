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
package us.coffeecode.advent_of_code.y2016;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Booleans;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 18)
@Component
public final class Year2016Day18 {

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

  public long calculate(final PuzzleContext pc) {
    final int iterations = pc.getInt("rows");
    boolean[] row = il.fileAsBooleanArray(pc, '^');
    long safe = count(row);
    // Start at one: first iteration is the preceding line of code.
    for (int i = 1; i < iterations; ++i) {
      row = translate(row);
      safe += count(row);
    }
    return safe;
  }

  private long count(final boolean[] array) {
    // Safe tiles are false, traps are true. Count safe tiles.
    return Booleans.asList(array)
                   .stream()
                   .filter(b -> !b.booleanValue())
                   .count();
  }

  private boolean[] translate(final boolean[] input) {
    final boolean[] output = new boolean[input.length];
    for (int i = 0; i < input.length; ++i) {
      if (i == 0) {
        // Leftmost tile. Don't care about center - matches tile above and to the right.
        output[i] = input[i + 1];
      }
      else if (i == input.length - 1) {
        // Rightmost tile. Don't care about center - matches tile above and to the left.
        output[i] = input[i - 1];
      }
      else {
        // Somewhere in the center: if the diagonal tiles are different, this is a trap.
        output[i] = input[i - 1] != input[i + 1];
      }
    }

    return output;
  }

}
