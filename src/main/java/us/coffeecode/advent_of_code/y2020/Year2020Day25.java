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

@AdventOfCodeSolution(year = 2020, day = 25, title = "Combo Breaker")
@Component
public class Year2020Day25 {

  private static final long MODULO = 20_201_227;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final long[] input = il.linesAsLongs(pc);

    final long cardKey = input[0];
    final long doorKey = input[1];

    // Get the loop size.
    long doorLoops = 0;
    {
      long value = 1;
      while (value != doorKey) {
        value *= 7;
        value %= MODULO;
        ++doorLoops;
      }
    }

    // Get the encryption key.
    long key = 1;
    for (int i = 0; i < doorLoops; ++i) {
      key *= cardKey;
      key %= MODULO;
    }

    return key;
  }

}
