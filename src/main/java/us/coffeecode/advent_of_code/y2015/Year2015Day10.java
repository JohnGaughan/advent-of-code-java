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

@AdventOfCodeSolution(year = 2015, day = 10, title = "Elves Look, Elves Say")
@Component
public final class Year2015Day10 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return lookAndSay(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return lookAndSay(pc);
  }

  /** Apply the look-and-say algorithm to the input. */
  private long lookAndSay(final PuzzleContext pc) {
    String value = il.fileAsString(pc);
    final int iterations = pc.getInt("iterations");
    for (int i = 0; i < iterations; ++i) {
      value = lookAndSay(value);
    }
    return value.length();
  }

  /** Apply the look-and-say algorithm to the input. */
  private String lookAndSay(final String input) {
    final StringBuilder str = new StringBuilder();
    int groupStart = 0;
    int groupChar = input.codePointAt(0);
    for (int i = 1; i < input.length(); ++i) {
      final int current = input.codePointAt(i);
      // Every time the character changes, save the previous group.
      if (current != groupChar) {
        str.append(i - groupStart);
        str.appendCodePoint(groupChar);
        groupStart = i;
      }
      groupChar = current;
    }
    // Handle the final group.
    str.append(input.length() - groupStart);
    str.appendCodePoint(groupChar);
    return str.toString();
  }

}
