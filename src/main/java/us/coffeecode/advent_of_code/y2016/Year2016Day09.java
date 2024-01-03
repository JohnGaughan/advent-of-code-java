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

@AdventOfCodeSolution(year = 2016, day = 9, title = "Explosives in Cyberspace")
@Component
public final class Year2016Day09 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculateLength(il.fileAsString(pc), false);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculateLength(il.fileAsString(pc), true);
  }

  private long calculateLength(final String input, final boolean recurse) {
    long output = 0;
    int markerStart = -1;
    int x = -1;
    for (int i = 0; i < input.length(); ++i) {
      int ch = input.codePointAt(i);
      // Currently parsing a marker
      if (markerStart > -1) {
        if (ch == 'x') {
          x = i;
        }
        if (ch == ')') {
          final int charsToRepeat = Integer.parseInt(input.substring(markerStart + 1, x));
          final int timesToRepeat = Integer.parseInt(input.substring(x + 1, i));
          if (recurse) {
            String sub = input.substring(i + 1, i + 1 + charsToRepeat);
            output += timesToRepeat * calculateLength(sub, true);
          }
          else {
            output += timesToRepeat * charsToRepeat;
          }
          markerStart = x = -1;
          i += charsToRepeat;
        }
      }
      // Not in a marker, starting one.
      else if (ch == '(') {
        markerStart = i;
      }
      // Not in a marker, not starting one.
      else {
        ++output;
      }
    }
    return output;
  }

}
