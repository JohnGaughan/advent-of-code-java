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

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 1, title = "Not Quite Lisp")
@Component
public final class Year2015Day01 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Collection<Direction> input = il.fileAsObjectsFromCodePoints(pc, Direction::valueOf);
    final long ups = input.stream().filter(d -> d == Direction.UP).count();
    return (ups << 1) - input.size();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    long floor = 0;
    long step = 1;
    for (final Direction d : il.fileAsObjectsFromCodePoints(pc, Direction::valueOf)) {
      if (d == Direction.UP) {
        ++floor;
      }
      else {
        --floor;
      }
      if (floor < 0) {
        break;
      }
      ++step;
    }
    return step;
  }

  /** A direction that Santa can take. */
  private static enum Direction {

    UP('('),
    DOWN(')');

    public static Direction valueOf(final int c) {
      return Arrays.stream(values()).filter(d -> d.ch == c).findFirst().get();
    }

    private final int ch;

    private Direction(final int c) {
      ch = c;
    }
  }

}
