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
package net.johngaughan.advent_of_code.y2020;

import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/12">Year 2020, day 12</a>. This problem requires us to move a boat around
 * on a grid using navigation instructions. Part one has us track the boat's location and heading, where the heading is
 * aligned to 90 degree angles. It moves around a few hundred times, then we return the Manhattan distance of the boat
 * compared to origin. Part two changes how we interpret the instructions by tracking a waypoint near the boat. Most of
 * the instructions now move the waypoint or rotate it around the boat. Moving the boat moves it toward the waypoint a
 * number of times equal to the distance argument.
 * </p>
 * <p>
 * The solution could be more object-oriented, but it would cause the size of the program to balloon quite severely.
 * Instead, just handle the various decisions in a loop. Simple and effective.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day12 {

  public int calculatePart1() {
    int x = 0;
    int y = 0;
    Direction d = Direction.EAST;
    for (final Input input : getInput()) {
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
      else if (input.action == Action.L || input.action == Action.R) {
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

  public int calculatePart2() {
    int x_s = 0;
    int y_s = 0;
    int x_w = 10;
    int y_w = 1;
    for (final Input input : getInput()) {
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
      else if (input.action == Action.L || input.action == Action.R) {
        for (int i = 0; i < getNumberOfRightTurns(input.action, input.arg); ++i) {
          final int old_x = x_w;
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
  private int getNumberOfRightTurns(final Action action, final int degrees) {
    int turns = degrees / 90 % 4;
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

  /** Get the input data for this solution. */
  private List<Input> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2020, 12)).stream().map(Input::new).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Input {

    final Action action;

    final int arg;

    Input(final String line) {
      action = Action.valueOf(line.substring(0, 1));
      arg = Integer.parseInt(line.substring(1));
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
