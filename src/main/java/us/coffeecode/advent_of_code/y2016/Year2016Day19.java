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

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 19)
@Component
public final class Year2016Day19 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return a(il.fileAsLong(pc));
  }

  private long a(final long n) {
    if (n == 1) {
      return 1;
    }
    else if (n % 2 == 0) {
      return (a(n / 2) << 1) - 1;
    }
    return (a(n / 2) << 1) + 1;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return b(il.fileAsLong(pc));
  }

  private long b(final long n) {
    // Note: dealing with doubles and longs here of the scale of our input will not cause any FP wonkyness.
    // Take the log3 of the input, then power it back out. This gives us the integral log3 as well as the lower bound
    // for the current partition. We have to be between 3^pow3 (inclusive) and 3^(pow3+1) (exclusive).
    long log3 = (long) (Math.log10(n) / Math.log10(3));
    long pow3 = (long) Math.pow(3, log3);
    if (n == pow3) {
      return n;
    }
    else if (n - pow3 <= pow3) {
      return n - pow3;
    }
    // Multiply n by 2 to get the two-spacing needed, then subtract the next higher power of three to scale it back
    // down.
    return (n << 1) - (3 * pow3);
  }

}
