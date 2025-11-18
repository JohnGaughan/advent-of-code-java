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
package us.coffeecode.advent_of_code.y2018;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2018, day = 17)
@Component
public final class Year2018Day17 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Tile[][] tiles = getInput(pc);
    simulate(tiles);
    return score(tiles, false);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Tile[][] tiles = getInput(pc);
    simulate(tiles);
    return score(tiles, true);
  }

  private long score(final Tile[][] tiles, final boolean partTwo) {
    long score = 0;
    // Find the row with the first tile of clay: this is where we start counting.
    int start_y = Integer.MAX_VALUE;
    out: for (int y = 0; y < tiles.length; ++y) {
      for (int x = 0; x < tiles[y].length; ++x) {
        if (tiles[y][x] == Tile.CLAY) {
          start_y = y;
          break out;
        }
      }
    }

    // Find the row with the last tile of clay: this is where we end counting.
    int end_y = Integer.MIN_VALUE;
    out: for (int y = tiles.length - 1; y > 0; --y) {
      for (int x = 0; x < tiles[y].length; ++x) {
        if (tiles[y][x] == Tile.CLAY) {
          // Add one for < comparison later.
          end_y = y + 1;
          break out;
        }
      }
    }

    // Count the water tiles.
    for (int y = start_y; y < end_y; ++y) {
      for (final Tile tile : tiles[y]) {
        if (tile == Tile.WATER_AT_REST) {
          ++score;
        }
        else if (!partTwo && (tile == Tile.WATER_IN_MOTION)) {
          ++score;
        }
      }
    }
    return score;
  }

  private void simulate(final Tile[][] tiles) {
    // Start by adding water below the spring.
    int spring_x = 0;
    while (tiles[0][spring_x] != Tile.SPRING) {
      ++spring_x;
    }
    tiles[1][spring_x] = Tile.WATER_IN_MOTION;

    // Create a queue of tiles to update and add the base case: the tile under the spring.
    final Deque<Point2D> queue = new LinkedList<>();
    queue.addFirst(new Point2D(spring_x, 1));

    // As long as there are tiles left to update, keep updating them.
    while (!queue.isEmpty()) {
      final Point2D current = queue.pollFirst();
      if (current.getY() >= tiles.length - 1) {
        // Ignore updates beyond the bottom of the grid.
        continue;
      }
      final Point2D below = new Point2D(current, 0, 1);

      if (current.get(tiles) == Tile.WATER_IN_MOTION) {

        // If this water in motion can flow further down, do so.
        if (below.get(tiles) == Tile.SAND) {
          below.set(tiles, Tile.WATER_IN_MOTION);
          // Come back and process the current tile again later, in case something below fills up.
          queue.addFirst(current);
          queue.addFirst(below);
        }

        // There is something below that can cause this layer to spread out.
        else if ((below.get(tiles) == Tile.CLAY) || (below.get(tiles) == Tile.WATER_AT_REST)) {

          // Will this layer overflow? If so, get the bounds of what to fill.
          int fillLeft = current.getX();
          int fillRight = current.getX();
          boolean overflowLeft = false;
          boolean overflowRight = false;
          for (int x = current.getX(); x > 0; --x) {
            // Go left until we hit clay to the side, or sand below.
            if (new Point2D(x - 1, current.getY()).get(tiles) == Tile.CLAY) {
              fillLeft = x;
              break;
            }
            else if (new Point2D(x, current.getY() + 1).get(tiles) == Tile.SAND) {
              fillLeft = x;
              overflowLeft = true;
              break;
            }
          }
          for (int x = current.getX(); x < tiles[current.getY()].length; ++x) {
            // Go right until we hit clay to the side, or sand below.
            if (new Point2D(x + 1, current.getY()).get(tiles) == Tile.CLAY) {
              fillRight = x;
              break;
            }
            else if (new Point2D(x, current.getY() + 1).get(tiles) == Tile.SAND) {
              fillRight = x;
              overflowRight = true;
              break;
            }
          }

          // If the current layer overflows, add the layer as water in motion.
          if (overflowLeft || overflowRight) {
            for (int x = fillLeft; x <= fillRight; ++x) {
              new Point2D(x, current.getY()).set(tiles, Tile.WATER_IN_MOTION);
            }
            if (overflowLeft) {
              queue.addFirst(new Point2D(fillLeft, current.getY()));
            }
            if (overflowRight) {
              queue.addFirst(new Point2D(fillRight, current.getY()));
            }
          }

          // Otherwise, add the layer as water at rest.
          else {
            for (int x = fillLeft; x <= fillRight; ++x) {
              new Point2D(x, current.getY()).set(tiles, Tile.WATER_AT_REST);
            }
          }
        }
      }
    }
  }

  @SuppressWarnings("unused")
  private String toString(final Tile[][] tiles, final int max_y) {
    final StringBuilder str = new StringBuilder(tiles.length * (tiles[0].length + 2));
    for (int y = 0; y < tiles.length && y < max_y; ++y) {
      for (final Tile tile : tiles[y]) {
        str.append(tile);
      }
      str.append('\n');
    }
    return str.toString();
  }

  /** Get the input data for this solution. */
  private Tile[][] getInput(final PuzzleContext pc) {
    final List<String> lines = il.lines(pc);

    // Parse lines into something more usable. First array is 0=x, 1=y. Inner array is either [value] or [min, max]
    final int[][][] clayLocations = new int[lines.size()][2][];
    for (int i = 0; i < clayLocations.length; ++i) {
      final String line = lines.get(i);
      boolean xFirst = line.charAt(0) == 'x';
      int n1 = Integer.parseInt(line.substring(2, line.indexOf(',')));
      int rangeStart = 1 + line.indexOf('=', line.indexOf(','));
      int rangeSeparator = line.indexOf("..");
      int n2 = Integer.parseInt(line.substring(rangeStart, rangeSeparator));
      int n3 = Integer.parseInt(line.substring(rangeSeparator + 2));
      if (xFirst) {
        clayLocations[i][0] = new int[1];
        clayLocations[i][1] = new int[2];
        clayLocations[i][0][0] = n1;
        clayLocations[i][1][0] = n2;
        clayLocations[i][1][1] = n3;
      }
      else {
        clayLocations[i][0] = new int[2];
        clayLocations[i][1] = new int[1];
        clayLocations[i][1][0] = n1;
        clayLocations[i][0][0] = n2;
        clayLocations[i][0][1] = n3;
      }
    }

    // Find the maximum x and y values. X starts at 500, the location of the spring.
    int xMin = 500;
    int xMax = 500;
    int yMax = 0;
    for (final int[][] locations : clayLocations) {
      // Start with the first element of each array
      xMin = Math.min(xMin, locations[0][0]);
      xMax = Math.max(xMax, locations[0][0]);
      yMax = Math.max(yMax, locations[1][0]);

      // If there is a second element, factor that in, too.
      if (locations[0].length > 1) {
        xMin = Math.min(xMin, locations[0][1]);
        xMax = Math.max(xMax, locations[0][1]);
      }
      else {
        yMax = Math.max(yMax, locations[1][1]);
      }
    }

    // Increment both maximums to account for off-by-one, and an additional unit of X for spill-over off the edges
    xMin -= 2;
    xMax += 2;
    ++yMax;

    // Allocate the tile array and populate with default values
    final Tile[][] tiles = new Tile[yMax][xMax - xMin];
    for (int y = 0; y < tiles.length; ++y) {
      for (int x = 0; x < tiles[y].length; ++x) {
        tiles[y][x] = Tile.SAND;
      }
    }

    // Populate the tile array with locations of clay.
    for (final int[][] locations : clayLocations) {
      if (locations[0].length == 1) {
        for (int y = locations[1][0]; y <= locations[1][1]; ++y) {
          tiles[y][locations[0][0] - xMin] = Tile.CLAY;
        }
      }
      else {
        for (int x = locations[0][0]; x <= locations[0][1]; ++x) {
          tiles[locations[1][0]][x - xMin] = Tile.CLAY;
        }
      }
    }

    // Add the spring
    tiles[0][500 - xMin] = Tile.SPRING;

    return tiles;
  }

  private static enum Tile {

    CLAY("#"),
    SAND("."),
    SPRING("+"),
    WATER_IN_MOTION("|"),
    WATER_AT_REST("~");

    final String str;

    Tile(final String _str) {
      str = _str;
    }

    @Override
    public String toString() {
      return str;
    }

  }

}
