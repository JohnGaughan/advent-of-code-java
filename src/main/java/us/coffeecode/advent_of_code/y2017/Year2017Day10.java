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
package us.coffeecode.advent_of_code.y2017;

import java.util.HexFormat;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 10)
@Component
public final class Year2017Day10 {

  @Autowired
  private KnotHash knot;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[] array = knot.hashOneRound(il.fileAsIntsFromSplit(pc, SEPARATOR));
    return array[0] * array[1];
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    final int[] hash = knot.hash(il.fileAsString(pc));
    final byte[] bytes = new byte[hash.length];
    for (int i = 0; i < bytes.length; ++i) {
      bytes[i] = (byte) hash[i];
    }
    return HexFormat.of()
                    .formatHex(bytes);
  }

  private static final Pattern SEPARATOR = Pattern.compile(",");
}
