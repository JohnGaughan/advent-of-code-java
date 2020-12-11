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
import java.nio.file.Path;
import java.util.Arrays;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/11">Year 2015, day 11</a>. This problem is another variation on Conway's
 * Game of Life that pops up in this challenge regularly. This time, there are grid elements that are ignored. Part one
 * is an ordinary variation, while part two considers a neighbor to be a seat in any of the eight directions that is
 * visible across floor elements.
 * </p>
 * <p>
 * Nothing really to say about this one, other than it was easy but tedious to type out and ensure all those array
 * subscripts were not fat-fingered.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day11 {

  public int calculatePart1(final Path path) {
    Status[][] newGrid = parse(path);
    Status[][] oldGrid = new Status[newGrid.length][newGrid[0].length];
    while (!Arrays.deepEquals(oldGrid, newGrid)) {
      Status[][] temp = newGrid;
      newGrid = oldGrid;
      oldGrid = temp;
      for (int i = 0; i < oldGrid.length; ++i) {
        final boolean rowAbove = i > 0;
        final boolean rowBelow = i < (oldGrid.length - 1);
        for (int j = 0; j < oldGrid[i].length; ++j) {
          newGrid[i][j] = oldGrid[i][j];
          // Skip locations that cannot possibly be updated.
          if (oldGrid[i][j] == Status.NO_SEAT) {
            continue;
          }
          final boolean colLeft = j > 0;
          final boolean colRight = j < (oldGrid[i].length - 1);
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

  public int calculatePart2(final Path path) {
    Status[][] newGrid = parse(path);
    Status[][] oldGrid = new Status[newGrid.length][newGrid[0].length];
    while (!Arrays.deepEquals(oldGrid, newGrid)) {
      Status[][] temp = newGrid;
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
  private int count(final Status[][] grid, final Status match) {
    int count = 0;
    for (Status[] row : grid) {
      for (Status element : row) {
        if (element == match) {
          ++count;
        }
      }
    }
    return count;
  }

  /** Parse the file located at the provided path location. */
  private Status[][] parse(final Path path) {
    try {
      return Files.readAllLines(path).stream().map(
        s -> s.codePoints().mapToObj(Status::valueOf).toArray(Status[]::new)).toArray(Status[][]::new);
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static enum Status {

    OCCUPIED('#'),
    EMPTY('L'),
    NO_SEAT('.');

    static Status valueOf(final int codePoint) {
      for (Status status : values()) {
        if (status.c == codePoint) {
          return status;
        }
      }
      throw new IllegalArgumentException("Invalid code point [" + codePoint + "]");
    }

    private final int c;

    private Status(final int ch) {
      c = ch;
    }
  }

}
