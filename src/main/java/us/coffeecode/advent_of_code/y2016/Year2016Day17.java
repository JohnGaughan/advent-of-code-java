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
package us.coffeecode.advent_of_code.y2016;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/17">Year 2016, day 17</a>. This problem asks us to search a maze to find a
 * path from one corner to another using special rules to calculate legal moves.
 * </p>
 * <p>
 * This is a breadth-first search. For part one, end searching as soon as we find a solution. It must be the shortest
 * path. Part two wants the longest path. In theory this could be effectively infinite, but in practice, many paths are
 * dead ends. Keep searching until exhausting all paths.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day17 {

  private static final int HEIGHT = 4;

  private static final int WIDTH = 4;

  public String calculatePart1() {
    return getPath(true, getInput());
  }

  public long calculatePart2() {
    return getPath(false, getInput()).length();
  }

  private String getPath(final boolean findShortest, final String salt) {
    Set<Path> paths = new HashSet<>();
    paths.add(new Path(""));
    Path currentSolution = null;
    outer: while (!paths.isEmpty()) {
      final Set<Path> nextIterationPaths = new HashSet<>();
      for (Path path : paths) {
        if (path.isSolution()) {
          currentSolution = path;
          // This is breadth-first, so quit right away if looking for the shortest path.
          if (findShortest) {
            break outer;
          }
          // Otherwise, mark this as a potential solution and stop searching this branch of the search tree.
          continue;
        }
        final String md5 = Utils.md5ToHex(salt + path.path);
        if (path.y > 0 && md5.codePointAt(0) > 'a') {
          nextIterationPaths.add(new Path(path.path + UP));
        }
        if (path.y < HEIGHT - 1 && md5.codePointAt(1) > 'a') {
          nextIterationPaths.add(new Path(path.path + DOWN));
        }
        if (path.x > 0 && md5.codePointAt(2) > 'a') {
          nextIterationPaths.add(new Path(path.path + LEFT));
        }
        if (path.x < WIDTH - 1 && md5.codePointAt(3) > 'a') {
          nextIterationPaths.add(new Path(path.path + RIGHT));
        }
      }
      paths = nextIterationPaths;
    }
    return currentSolution == null ? "" : currentSolution.path;
  }

  /** Get the input data for this solution. */
  private String getInput() {
    try {
      return Files.readString(Utils.getInput(2016, 17)).trim();
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Path {

    final int x;

    final int y;

    final String path;

    final int hashCode;

    Path(final String _path) {
      path = _path;
      int _x = 0;
      int _y = 0;
      for (int i = 0; i < path.length(); ++i) {
        final int ch = path.codePointAt(i);
        if (ch == UP) {
          --_y;
        }
        else if (ch == DOWN) {
          ++_y;
        }
        else if (ch == LEFT) {
          --_x;
        }
        else if (ch == RIGHT) {
          ++_x;
        }
      }
      x = _x;
      y = _y;
      hashCode = Objects.hash(path);
    }

    boolean isSolution() {
      return x == WIDTH - 1 && y == HEIGHT - 1;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof Path)) {
        return false;
      }
      // X and Y are deterministic based on path, ignore them.
      return ((Path) obj).path.equals(path);
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public String toString() {
      return path + "=(" + x + "," + y + ")";
    }

  }

  private static final char LEFT = 'L';

  private static final char RIGHT = 'R';

  private static final char UP = 'U';

  private static final char DOWN = 'D';

}
