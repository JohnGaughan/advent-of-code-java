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

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 11, title = "Seating System")
@Component
public final class Year2020Day11 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    Status[][] newGrid = getInput(pc);
    Status[][] oldGrid = new Status[newGrid.length][newGrid[0].length];
    while (!Arrays.deepEquals(oldGrid, newGrid)) {
      final Status[][] temp = newGrid;
      newGrid = oldGrid;
      oldGrid = temp;
      for (int i = 0; i < oldGrid.length; ++i) {
        final boolean rowAbove = i > 0;
        final boolean rowBelow = i < oldGrid.length - 1;
        for (int j = 0; j < oldGrid[i].length; ++j) {
          newGrid[i][j] = oldGrid[i][j];
          // Skip locations that cannot possibly be updated.
          if (oldGrid[i][j] == Status.NO_SEAT) {
            continue;
          }
          final boolean colLeft = j > 0;
          final boolean colRight = j < oldGrid[i].length - 1;
          int neighbors = 0;
          // Above
          if (rowAbove && colLeft && (oldGrid[i - 1][j - 1] == Status.OCCUPIED)) {
            ++neighbors;
          }
          if (rowAbove && (oldGrid[i - 1][j] == Status.OCCUPIED)) {
            ++neighbors;
          }
          if (rowAbove && colRight && (oldGrid[i - 1][j + 1] == Status.OCCUPIED)) {
            ++neighbors;
          }

          // Even
          if (colLeft && (oldGrid[i][j - 1] == Status.OCCUPIED)) {
            ++neighbors;
          }
          if (colRight && (oldGrid[i][j + 1] == Status.OCCUPIED)) {
            ++neighbors;
          }

          // Below
          if (rowBelow && colLeft && (oldGrid[i + 1][j - 1] == Status.OCCUPIED)) {
            ++neighbors;
          }
          if (rowBelow && (oldGrid[i + 1][j] == Status.OCCUPIED)) {
            ++neighbors;
          }
          if (rowBelow && colRight && (oldGrid[i + 1][j + 1] == Status.OCCUPIED)) {
            ++neighbors;
          }

          // Empty and no occupied neighbors -> occupied
          if ((oldGrid[i][j] == Status.EMPTY) && (neighbors == 0)) {
            newGrid[i][j] = Status.OCCUPIED;
          }
          // Occupied and 4+ occupied neighbors -> empty
          else if ((oldGrid[i][j] == Status.OCCUPIED) && (neighbors > 3)) {
            newGrid[i][j] = Status.EMPTY;
          }
        }
      }
    }
    return count(newGrid, Status.OCCUPIED);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    Status[][] newGrid = getInput(pc);
    Status[][] oldGrid = new Status[newGrid.length][newGrid[0].length];
    while (!Arrays.deepEquals(oldGrid, newGrid)) {
      final Status[][] temp = newGrid;
      newGrid = oldGrid;
      oldGrid = temp;
      for (int i = 0; i < oldGrid.length; ++i) {
        for (int j = 0; j < oldGrid[i].length; ++j) {
          newGrid[i][j] = oldGrid[i][j];
          // Skip locations that cannot possibly be updated.
          if (oldGrid[i][j] == Status.NO_SEAT) {
            continue;
          }
          int neighbors = 0;

          // Up left
          for (int i2 = i - 1, j2 = j - 1; (i2 >= 0) && (j2 >= 0); --i2, --j2) {
            if (oldGrid[i2][j2] == Status.OCCUPIED) {
              ++neighbors;
              break;
            }
            else if (oldGrid[i2][j2] == Status.EMPTY) {
              break;
            }
          }

          // Up
          for (int i2 = i - 1; i2 >= 0; --i2) {
            if (oldGrid[i2][j] == Status.OCCUPIED) {
              ++neighbors;
              break;
            }
            else if (oldGrid[i2][j] == Status.EMPTY) {
              break;
            }
          }

          // Up right
          for (int i2 = i - 1, j2 = j + 1; (i2 >= 0) && (j2 < oldGrid[i2].length); --i2, ++j2) {
            if (oldGrid[i2][j2] == Status.OCCUPIED) {
              ++neighbors;
              break;
            }
            else if (oldGrid[i2][j2] == Status.EMPTY) {
              break;
            }
          }

          // Left
          for (int j2 = j - 1; j2 >= 0; --j2) {
            if (oldGrid[i][j2] == Status.OCCUPIED) {
              ++neighbors;
              break;
            }
            else if (oldGrid[i][j2] == Status.EMPTY) {
              break;
            }
          }

          // Right
          for (int j2 = j + 1; j2 < oldGrid[i].length; ++j2) {
            if (oldGrid[i][j2] == Status.OCCUPIED) {
              ++neighbors;
              break;
            }
            else if (oldGrid[i][j2] == Status.EMPTY) {
              break;
            }
          }

          // Down left
          for (int i2 = i + 1, j2 = j - 1; (i2 < oldGrid.length) && (j2 >= 0); ++i2, --j2) {
            if (oldGrid[i2][j2] == Status.OCCUPIED) {
              ++neighbors;
              break;
            }
            else if (oldGrid[i2][j2] == Status.EMPTY) {
              break;
            }
          }

          // Down
          for (int i2 = i + 1; i2 < oldGrid.length; ++i2) {
            if (oldGrid[i2][j] == Status.OCCUPIED) {
              ++neighbors;
              break;
            }
            else if (oldGrid[i2][j] == Status.EMPTY) {
              break;
            }
          }

          // Down right
          for (int i2 = i + 1, j2 = j + 1; (i2 < oldGrid.length) && (j2 < oldGrid[i2].length); ++i2, ++j2) {
            if (oldGrid[i2][j2] == Status.OCCUPIED) {
              ++neighbors;
              break;
            }
            else if (oldGrid[i2][j2] == Status.EMPTY) {
              break;
            }
          }

          // Empty and no occupied neighbors -> occupied
          if ((oldGrid[i][j] == Status.EMPTY) && (neighbors == 0)) {
            newGrid[i][j] = Status.OCCUPIED;
          }
          // Occupied and 5+ occupied neighbors -> empty
          else if ((oldGrid[i][j] == Status.OCCUPIED) && (neighbors > 4)) {
            newGrid[i][j] = Status.EMPTY;
          }
        }
      }
    }
    return count(newGrid, Status.OCCUPIED);
  }

  /** Count the number of items in the grid that match the given status. */
  private long count(final Status[][] grid, final Status match) {
    return Arrays.stream(grid)
                 .mapToLong(a -> Arrays.stream(a)
                                       .filter(s -> s == match)
                                       .count())
                 .sum();
  }

  /** Get the input data for this solution. */
  private Status[][] getInput(final PuzzleContext pc) {
    return il.linesAsObjectsArray(pc, s -> s.codePoints()
                                            .mapToObj(Status::valueOf)
                                            .toArray(Status[]::new),
      Status[][]::new);
  }

  private static enum Status {

    OCCUPIED('#'),
    EMPTY('L'),
    NO_SEAT('.');

    static Status valueOf(final int codePoint) {
      return Arrays.stream(values())
                   .filter(s -> s.c == codePoint)
                   .findFirst()
                   .get();
    }

    private final int c;

    private Status(final int ch) {
      c = ch;
    }
  }

}
