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

import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MD5;

@AdventOfCodeSolution(year = 2015, day = 4, title = "The Ideal Stocking Stuffer")
@Component
public final class Year2015Day04 {

  @Autowired
  private InputLoader il;

  @Autowired
  private MD5 md5;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, "00000");
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, "000000");
  }

  public long calculate(final PuzzleContext pc, final String prefix) {
    final String input = il.fileAsString(pc);
    for (long i = 0; i < Long.MAX_VALUE; ++i) {
      final String plaintext = input + i;
      final String ciphertext = HexFormat.of().formatHex(md5.md5(plaintext));
      if (ciphertext.startsWith(prefix)) {
        return i;
      }
    }
    return -1;
  }

}
