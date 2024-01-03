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
package us.coffeecode.advent_of_code.y2021;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 2, title = "Dive!")
@Component
public final class Year2021Day02 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    int x = 0;
    int y = 0;
    for (final Command c : il.linesAsObjects(pc, Command::make)) {
      if (c.direction == Direction.forward) {
        x += c.amount;
      }
      else if (c.direction == Direction.up) {
        y -= c.amount;
      }
      else {
        y += c.amount;
      }
    }
    return x * y;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    int x = 0;
    int y = 0;
    int aim = 0;
    for (final Command c : il.linesAsObjects(pc, Command::make)) {
      if (c.direction == Direction.forward) {
        x += c.amount;
        y += (aim * c.amount);
      }
      else if (c.direction == Direction.up) {
        aim -= c.amount;
      }
      else {
        aim += c.amount;
      }
    }
    return x * y;
  }

  private enum Direction {
    forward,
    down,
    up;
  }

  private static record Command(Direction direction, int amount) {

    private static final Pattern SPLIT = Pattern.compile(" ");

    static Command make(final String line) {
      final String[] tokens = SPLIT.split(line);
      return new Command(Direction.valueOf(tokens[0]), Integer.parseInt(tokens[1]));
    }

  }

}
