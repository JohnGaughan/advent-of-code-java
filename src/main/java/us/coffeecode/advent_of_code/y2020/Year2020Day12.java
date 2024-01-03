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

@AdventOfCodeSolution(year = 2020, day = 12, title = "Rain Risk")
@Component
public final class Year2020Day12 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    long x = 0;
    long y = 0;
    Direction d = Direction.EAST;
    for (final Input input : il.linesAsObjects(pc, Input::make)) {
      if (input.action == Action.N) {
        y += input.arg;
      }
      else if (input.action == Action.E) {
        x += input.arg;
      }
      else if (input.action == Action.S) {
        y -= input.arg;
      }
      else if (input.action == Action.W) {
        x -= input.arg;
      }
      else if ((input.action == Action.L) || (input.action == Action.R)) {
        for (int i = 0; i < getNumberOfRightTurns(input.action, input.arg); ++i) {
          d = turnRight(d);
        }
      }
      else if (input.action == Action.F) {
        if (d == Direction.NORTH) {
          y += input.arg;
        }
        else if (d == Direction.EAST) {
          x += input.arg;
        }
        else if (d == Direction.SOUTH) {
          y -= input.arg;
        }
        else {
          x -= input.arg;
        }
      }
    }
    return Math.abs(x) + Math.abs(y);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    long x_s = 0;
    long y_s = 0;
    long x_w = 10;
    long y_w = 1;
    for (final Input input : il.linesAsObjects(pc, Input::make)) {
      if (input.action == Action.N) {
        y_w += input.arg;
      }
      else if (input.action == Action.E) {
        x_w += input.arg;
      }
      else if (input.action == Action.S) {
        y_w -= input.arg;
      }
      else if (input.action == Action.W) {
        x_w -= input.arg;
      }
      else if ((input.action == Action.L) || (input.action == Action.R)) {
        for (int i = 0; i < getNumberOfRightTurns(input.action, input.arg); ++i) {
          final long old_x = x_w;
          x_w = y_w;
          y_w = -old_x;
        }
      }
      else if (input.action == Action.F) {
        x_s += input.arg * x_w;
        y_s += input.arg * y_w;
      }
    }
    return Math.abs(x_s) + Math.abs(y_s);
  }

  /** Get the number of right turns for the given turning direction and number of degrees. */
  private long getNumberOfRightTurns(final Action action, final long degrees) {
    long turns = (degrees / 90) % 4;
    if (action == Action.L) {
      turns = 4 - turns;
    }
    return turns;
  }

  /** Turn right once. */
  private Direction turnRight(final Direction d) {
    if (d == Direction.NORTH) {
      return Direction.EAST;
    }
    else if (d == Direction.EAST) {
      return Direction.SOUTH;
    }
    else if (d == Direction.SOUTH) {
      return Direction.WEST;
    }
    else {
      return Direction.NORTH;
    }
  }

  private static record Input(Action action, long arg) {

    static Input make(final String line) {
      return new Input(Action.valueOf(line.substring(0, 1)), Long.parseLong(line.substring(1)));
    }
  }

  private static enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;
  }

  private static enum Action {
    N,
    S,
    E,
    W,
    L,
    R,
    F;
  }

}
