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

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 13, title = "Packet Scanners")
@Component
public final class Year2017Day13 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    int severity = 0;
    for (final Scanner scanner : il.linesAsObjects(pc, Scanner::make)) {
      if (scanner.depth % (2 * (scanner.range - 1)) == 0) {
        severity += scanner.depth * scanner.range;
      }
    }
    return severity;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Iterable<Scanner> input = il.linesAsObjects(pc, Scanner::make);
    for (int t = 0; t < Integer.MAX_VALUE; t += 2) {
      boolean caught = false;
      for (final Scanner scanner : input) {
        if ((t + scanner.depth) % (2 * (scanner.range - 1)) == 0) {
          caught = true;
          break;
        }
      }
      if (!caught) {
        return t;
      }
    }
    return 0;
  }

  private static record Scanner(int depth, int range) {

    private static final Pattern SEPARATOR = Pattern.compile(": ");

    static Scanner make(final String input) {
      final String[] array = SEPARATOR.split(input);
      return new Scanner(Integer.parseInt(array[0]), Integer.parseInt(array[1]));
    }

  }

}
