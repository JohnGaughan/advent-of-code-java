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
package us.coffeecode.advent_of_code.y2015;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 25)
@Component
public final class Year2015Day25 {

  private static final long FIRST_VALUE = 20_151_125L;

  private static final long MULTIPLIER = 252_533L;

  private static final long MODULUS = 33_554_393L;

  private static final long PERIOD = MODULUS >> 1;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input crd = Input.make(il.fileAsIntsFromDigitGroups(pc));
    // Calculate the number of iterations required. This is simplified from a more complex equation.
    final long iterations = ((crd.y * crd.y) + ((crd.y * crd.x) << 1) + (crd.x * crd.x) - (3 * crd.y) - crd.x) >> 1;

    // Use modular exponentiation to calculate it.
    long result = 1;
    long base = MULTIPLIER;
    long exponent = iterations % PERIOD;
    while (exponent > 0) {
      if ((exponent & 1) == 1) {
        result = result * base % MODULUS;
      }
      exponent >>= 1;
      base = base * base % MODULUS;
    }
    result = FIRST_VALUE * result % MODULUS;
    return result;
  }

  private static record Input(int x, int y) {

    static Input make(final int[] i) {
      return new Input(i[1], i[0]);
    }
  }

}
