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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 9, title = "Stream Processing")
@Component
public final class Year2017Day09 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(il.fileAsString(pc)).score;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(il.fileAsString(pc)).garbage;
  }

  private Result calculate(final String input) {
    boolean inGarbage = false;
    boolean escape = false;
    long garbage = 0;
    long score = 0;
    long depth = 1;
    for (int i = 0; i < input.length(); ++i) {
      final int ch = input.codePointAt(i);
      if (inGarbage) {
        if (escape) {
          escape = false;
        }
        else if (ch == '!') {
          escape = true;
        }
        else if (ch == '>') {
          inGarbage = false;
        }
        else {
          ++garbage;
        }
      }
      else if (ch == '<') {
        inGarbage = true;
      }
      else if (ch == '{') {
        score += depth;
        ++depth;
      }
      else if (ch == '}') {
        --depth;
      }
    }
    return new Result(score, garbage);
  }

  private static record Result(long score, long garbage) {}

}
