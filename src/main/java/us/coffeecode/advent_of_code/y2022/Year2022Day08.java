/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 8, title = "Treetop Tree House")
@Component
public class Year2022Day08 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[][] trees = il.linesAs2dIntArrayFromDigits(pc);
    long visible = 0;
    for (final int y : IntStream.range(0, trees[0].length)
                                .toArray()) {
      for (final int x : IntStream.range(0, trees[y].length)
                                  .toArray()) {
        if (IntStream.range(0, y)
                     .filter(y0 -> trees[y][x] <= trees[y0][x])
                     .count() == 0
          || IntStream.range(y + 1, trees.length)
                      .filter(y0 -> trees[y][x] <= trees[y0][x])
                      .count() == 0
          || IntStream.range(0, x)
                      .filter(x0 -> trees[y][x] <= trees[y][x0])
                      .count() == 0
          || IntStream.range(x + 1, trees[y].length)
                      .filter(x0 -> trees[y][x] <= trees[y][x0])
                      .count() == 0) {
          ++visible;
        }
      }
    }
    return visible;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[][] trees = il.linesAs2dIntArrayFromDigits(pc);
    long score = Long.MIN_VALUE;
    for (int y = 1; y < trees.length - 1; ++y) {
      for (int x = 1; x < trees[y].length - 1; ++x) {
        score = Math.max(score, scenicScore(trees, x, y));
      }
    }
    return score;
  }

  private long scenicScore(final int[][] trees, final int x, final int y) {
    final long north = scoreNorth(trees, x, y);
    final long south = scoreSouth(trees, x, y);
    final long east = scoreEast(trees, x, y);
    final long west = scoreWest(trees, x, y);
    return north * south * east * west;
  }

  private long scoreNorth(final int[][] trees, final int x, final int y) {
    long score = 0;
    for (int y0 = y - 1; y0 >= 0; --y0) {
      ++score;
      if (trees[y0][x] >= trees[y][x]) {
        break;
      }
    }
    return score;
  }

  private long scoreSouth(final int[][] trees, final int x, final int y) {
    long score = 0;
    for (int y0 = y + 1; y0 < trees.length; ++y0) {
      ++score;
      if (trees[y0][x] >= trees[y][x]) {
        break;
      }
    }
    return score;
  }

  private long scoreWest(final int[][] trees, final int x, final int y) {
    long score = 0;
    for (int x0 = x - 1; x0 >= 0; --x0) {
      ++score;
      if (trees[y][x0] >= trees[y][x]) {
        break;
      }
    }
    return score;
  }

  private long scoreEast(final int[][] trees, final int x, final int y) {
    long score = 0;
    for (int x0 = x + 1; x0 < trees[y].length; ++x0) {
      ++score;
      if (trees[y][x0] >= trees[y][x]) {
        break;
      }
    }
    return score;
  }

}
