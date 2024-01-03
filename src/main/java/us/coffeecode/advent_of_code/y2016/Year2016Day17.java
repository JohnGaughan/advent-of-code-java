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

import java.util.HashSet;
import java.util.HexFormat;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MD5;

@AdventOfCodeSolution(year = 2016, day = 17, title = "Two Steps Forward")
@Component
public final class Year2016Day17 {

  private static final int HEIGHT = 4;

  private static final int WIDTH = 4;

  @Autowired
  private MD5 md5;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    return getPath(true, il.fileAsString(pc));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return getPath(false, il.fileAsString(pc)).length();
  }

  private String getPath(final boolean findShortest, final String salt) {
    Set<Path> paths = new HashSet<>();
    paths.add(Path.make(""));
    Path currentSolution = null;
    outer: while (!paths.isEmpty()) {
      final Set<Path> nextIterationPaths = new HashSet<>();
      for (final Path path : paths) {
        if (path.isSolution()) {
          currentSolution = path;
          // This is breadth-first, so quit right away if looking for the shortest path.
          if (findShortest) {
            break outer;
          }
          // Otherwise, mark this as a potential solution and stop searching this branch of the search tree.
          continue;
        }
        final String hash = HexFormat.of().formatHex(md5.md5(salt + path.path));
        if ((path.y > 0) && (hash.codePointAt(0) > 'a')) {
          nextIterationPaths.add(Path.make(path.path + UP));
        }
        if ((path.y < HEIGHT - 1) && (hash.codePointAt(1) > 'a')) {
          nextIterationPaths.add(Path.make(path.path + DOWN));
        }
        if ((path.x > 0) && (hash.codePointAt(2) > 'a')) {
          nextIterationPaths.add(Path.make(path.path + LEFT));
        }
        if ((path.x < WIDTH - 1) && (hash.codePointAt(3) > 'a')) {
          nextIterationPaths.add(Path.make(path.path + RIGHT));
        }
      }
      paths = nextIterationPaths;
    }
    return currentSolution == null ? "" : currentSolution.path;
  }

  private static final record Path(int x, int y, String path) {

    static Path make(final String _path) {
      int _x = 0;
      int _y = 0;
      for (int i = 0; i < _path.length(); ++i) {
        final int ch = _path.codePointAt(i);
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
      return new Path(_x, _y, _path);
    }

    boolean isSolution() {
      return (x == WIDTH - 1) && (y == HEIGHT - 1);
    }

  }

  private static final char LEFT = 'L';

  private static final char RIGHT = 'R';

  private static final char UP = 'U';

  private static final char DOWN = 'D';

}
