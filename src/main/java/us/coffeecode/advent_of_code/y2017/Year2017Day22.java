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

import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/22">Year 2017, day 22</a>. This problem asks us to model a grid where each
 * cell can have one of four states. Rules define state transitions, and we must iterate a given number of times and
 * report how many of a specific state transition occur. The two parts differ in the number of iterations and the
 * complexity of the state transitions.
 * </p>
 * <p>
 * The implementation is fairly simple. Define enumerations for the states and directions. Construct a grid to traverse
 * around, and implement state transitions in those enumerations. For each iteration, perform the steps described in the
 * problem statement. At first, I opted for a Map of coordinates which were simple two-int objects. This makes it
 * trivial to operate on grids of arbitrary size, as the problem says the grid is infinite. However, this introduced a
 * ton of overhead. I used observation to see that the virus only ever travels 208 units away from origin in any
 * direction, so I constructed a grid capable of holding any coordinate the virus will visit and got rid of the
 * coordinate class. This sped up the algorithm by four times due to avoiding a ton of operations involving maps, and
 * using primitives instead. The initialization logic is slightly more complex, but it is an acceptable tradeoff.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day22 {

  private static final int GRID_SIZE = 1 + 208 * 2;

  public long calculatePart1() {
    return calculate(10_000, false);
  }

  public long calculatePart2() {
    return calculate(10_000_000, true);
  }

  private long calculate(final int iterations, final boolean partTwo) {
    final State[][] coordinates = getInput();
    long infections = 0;
    int y = GRID_SIZE / 2;
    int x = y;
    Direction dir = Direction.NORTH;
    for (int i = 0; i < iterations; ++i) {
      // 1. Turn based on infection state.
      dir = dir.turn(coordinates[y][x]);

      // 2. Invert the infection state of the current location.
      coordinates[y][x] = coordinates[y][x].modify(partTwo);
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
  private State[][] getInput() {
    try {
      // This assumes the input grid is a square, which is true.
      final List<String> lines = Files.readAllLines(Utils.getInput(2017, 22));
      final State[][] grid = new State[GRID_SIZE][GRID_SIZE];
      final int center = GRID_SIZE / 2;
      final int inputDistanceFromOrigin = lines.size() / 2;
      for (int y = 0; y < grid.length; ++y) {
        String line = null;
        if (y >= center - inputDistanceFromOrigin && y <= center + inputDistanceFromOrigin) {
          line = lines.get(y - center + inputDistanceFromOrigin);
        }
        for (int x = 0; x < grid[y].length; ++x) {
          if (line != null && x >= center - inputDistanceFromOrigin && x <= center + inputDistanceFromOrigin) {
            grid[y][x] = line.codePointAt(x - center + inputDistanceFromOrigin) == '#' ? State.INFECTED : State.CLEAN;
          }
          else {
            grid[y][x] = State.CLEAN;
          }
        }
      }
      return grid;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static enum State {

    CLEAN {

      @Override
      State modify(final boolean partTwo) {
        return partTwo ? WEAKENED : INFECTED;
      }
    },
    WEAKENED {

      @Override
      State modify(final boolean partTwo) {
        return INFECTED;
      }
    },
    INFECTED {

      @Override
      State modify(final boolean partTwo) {
        return partTwo ? FLAGGED : CLEAN;
      }
    },
    FLAGGED {

      @Override
      State modify(final boolean partTwo) {
        return CLEAN;
      }
    };

    abstract State modify(boolean partTwo);
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

  private static final Map<Direction, Map<State, Direction>> TURNS = new HashMap<>();

  static {
    final Map<State, Direction> north = new HashMap<>();
    north.put(State.CLEAN, Direction.WEST);
    north.put(State.WEAKENED, Direction.NORTH);
    north.put(State.INFECTED, Direction.EAST);
    north.put(State.FLAGGED, Direction.SOUTH);

    final Map<State, Direction> south = new HashMap<>();
    south.put(State.CLEAN, Direction.EAST);
    south.put(State.WEAKENED, Direction.SOUTH);
    south.put(State.INFECTED, Direction.WEST);
    south.put(State.FLAGGED, Direction.NORTH);

    final Map<State, Direction> east = new HashMap<>();
    east.put(State.CLEAN, Direction.NORTH);
    east.put(State.WEAKENED, Direction.EAST);
    east.put(State.INFECTED, Direction.SOUTH);
    east.put(State.FLAGGED, Direction.WEST);

    final Map<State, Direction> west = new HashMap<>();
    west.put(State.CLEAN, Direction.SOUTH);
    west.put(State.WEAKENED, Direction.WEST);
    west.put(State.INFECTED, Direction.NORTH);
    west.put(State.FLAGGED, Direction.EAST);

    TURNS.put(Direction.NORTH, north);
    TURNS.put(Direction.SOUTH, south);
    TURNS.put(Direction.EAST, east);
    TURNS.put(Direction.WEST, west);
  }

}
