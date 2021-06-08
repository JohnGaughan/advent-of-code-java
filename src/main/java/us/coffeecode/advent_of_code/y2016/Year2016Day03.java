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
import java.util.Arrays;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/3">Year 2016, day 3</a>. This problem asks us to evaluate triangles. Based
 * on the lengths of their sides, which ones are possible in Euclidean geometry?
 * </p>
 * <p>
 * The solution is fairly straightforward. Given a grid of numbers, read them into a 2D array. Next, for part two only,
 * reorganize the numbers since they are given in a different order. Then sort each row which represents the side
 * lengths of a triangle. Finally, check that the sum of the two short sides is greater than the length of the third
 * (longest) side.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day03 {

  public long calculatePart1() {
    return countPossibleTriangles(getInput());
  }

  public long calculatePart2() {
    final int[][] triangles = getInput();
    for (int i = 0; i < triangles.length; i += 3) {
      int temp = -1;
      /* @formatter:off
       * For each 3x3 section, swap pairs of A, B, and C:
       *
       * - A B
       * A - C
       * B C -
       *
       * This will put the input into the same order as with part one, so the same algorithm will work.
       *
       * @formatter:on
       */

      // Swap A
      temp = triangles[i][1];
      triangles[i][1] = triangles[i + 1][0];
      triangles[i + 1][0] = temp;

      // Swap B
      temp = triangles[i][2];
      triangles[i][2] = triangles[i + 2][0];
      triangles[i + 2][0] = temp;

      // Swap C
      temp = triangles[i + 1][2];
      triangles[i + 1][2] = triangles[i + 2][1];
      triangles[i + 2][1] = temp;
    }
    return countPossibleTriangles(triangles);
  }

  private long countPossibleTriangles(final int[][] triangles) {
    int possible = 0;
    for (final int[] triangle : triangles) {
      Arrays.sort(triangle);
      if (triangle[0] + triangle[1] > triangle[2]) {
        ++possible;
      }
    }
    return possible;
  }

  private static final Pattern SPLIT = Pattern.compile("\\s+");

  /** Get the input data for this solution. */
  private int[][] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 3)).stream().map(
        s -> Arrays.stream(SPLIT.split(s.trim())).mapToInt(Integer::valueOf).toArray()).toArray(int[][]::new);
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
