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

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 22, title = "Sporifica Virus")
@Component
public final class Year2017Day22 {

  private static final int GRID_SIZE = 1 + (208 << 1);

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  private long calculate(final PuzzleContext pc) {
    final boolean invert = pc.getBoolean("InvertInfectionState");
    final int iterations = pc.getInt("Iterations");
    final State[][] coordinates = getInput(pc);
    long infections = 0;
    int y = GRID_SIZE >> 1;
    int x = y;
    Direction dir = Direction.NORTH;
    for (int i = 0; i < iterations; ++i) {
      // 1. Turn based on infection state.
      dir = dir.turn(coordinates[y][x]);

      // 2. Invert the infection state of the current location.
      coordinates[y][x] = coordinates[y][x].modify(invert);
      if (coordinates[y][x] == State.INFECTED) {
        ++infections;
      }

      // 3. Move forward one location.
      x += dir.dx;
      y += dir.dy;
    }
    return infections;
  }

  /** Get the input data for this solution. */
  private State[][] getInput(final PuzzleContext pc) {
    // This assumes the input grid is a square, which is true.
    final List<String> lines = il.lines(pc);
    final State[][] grid = new State[GRID_SIZE][GRID_SIZE];
    final int center = GRID_SIZE >> 1;
    final int inputDistanceFromOrigin = lines.size() >> 1;
    for (int y = 0; y < grid.length; ++y) {
      String line = null;
      if (y >= center - inputDistanceFromOrigin && y <= center + inputDistanceFromOrigin) {
        line = lines.get(y - center + inputDistanceFromOrigin);
      }
      for (int x = 0; x < grid[y].length; ++x) {
        if ((line != null) && (x >= center - inputDistanceFromOrigin) && (x <= center + inputDistanceFromOrigin)) {
          grid[y][x] = line.codePointAt(x - center + inputDistanceFromOrigin) == '#' ? State.INFECTED : State.CLEAN;
        }
        else {
          grid[y][x] = State.CLEAN;
        }
      }
    }
    return grid;
  }

  private static enum State {

    CLEAN {

      @Override
      State modify(final boolean invert) {
        return invert ? WEAKENED : INFECTED;
      }
    },
    WEAKENED {

      @Override
      State modify(final boolean invert) {
        return INFECTED;
      }
    },
    INFECTED {

      @Override
      State modify(final boolean invert) {
        return invert ? FLAGGED : CLEAN;
      }
    },
    FLAGGED {

      @Override
      State modify(final boolean invert) {
        return CLEAN;
      }
    };

    abstract State modify(boolean invert);
  }

  private static enum Direction {

    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0);

    final int dx;

    final int dy;

    Direction(final int _dx, final int _dy) {
      dx = _dx;
      dy = _dy;
    }

    Direction turn(final State state) {
      return TURNS.get(this).get(state);
    }

  }

  private static final Map<Direction, Map<State, Direction>> TURNS;

  static {
    final Map<State, Direction> north = Map.of(State.CLEAN, Direction.WEST, State.WEAKENED, Direction.NORTH, State.INFECTED,
      Direction.EAST, State.FLAGGED, Direction.SOUTH);

    final Map<State, Direction> south = Map.of(State.CLEAN, Direction.EAST, State.WEAKENED, Direction.SOUTH, State.INFECTED,
      Direction.WEST, State.FLAGGED, Direction.NORTH);

    final Map<State, Direction> east = Map.of(State.CLEAN, Direction.NORTH, State.WEAKENED, Direction.EAST, State.INFECTED,
      Direction.SOUTH, State.FLAGGED, Direction.WEST);

    final Map<State, Direction> west = Map.of(State.CLEAN, Direction.SOUTH, State.WEAKENED, Direction.WEST, State.INFECTED,
      Direction.NORTH, State.FLAGGED, Direction.EAST);

    TURNS = Map.of(Direction.NORTH, north, Direction.SOUTH, south, Direction.EAST, east, Direction.WEST, west);
  }

}
