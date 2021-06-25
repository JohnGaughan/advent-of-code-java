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

import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2018/day/3">Year 2018, day 3</a>. This puzzle relates to overlapping rectangles.
 * For part one, we need to see how many coordinates have multiple rectangles that contain them. For part two, we need
 * to find the single rectangle that does not overlap with any others.
 * </p>
 * <p>
 * There are a few approaches to this problem. For part one, the simplest is to run the simulation and count the
 * squares. The only optimization I added here is to have a single group of loops, and to count collisions as they occur
 * instead of looping over the entire grid after marking each claim.
 * </p>
 * <p>
 * For part two, I use the standard "do rectangles overlap" boolean expression inside of nested loops. Unfortunately it
 * is not possible to constrict the inner loop or to remove rectangles as they collide, as this produces incorrect
 * results due to messing up transitive properties. If rectangles A and B overlap, and B and C overlap, we cannot prune
 * B because after checking A, we still need it when checking C. Every method I tried to remove rectangles from the
 * search space broke it. This still runs in less than 1ms, so the naive approach is fine here.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2018Day03 {

  public long calculatePart1() {
    final int size = 1_000;
    final int[][] fabric = new int[size][size];
    long result = 0;
    for (final Claim claim : getInput()) {
      for (int y = claim.y1; y < claim.y2; ++y) {
        for (int x = claim.x1; x < claim.x2; ++x) {
          ++fabric[y][x];
          if (fabric[y][x] == 2) {
            ++result;
          }
        }
      }
    }
    return result;
  }

  public long calculatePart2() {
    final Claim[] claims = getInput();
    for (int i = 0; i < claims.length; ++i) {
      boolean overlaps = false;
      for (int j = 0; j < claims.length; ++j) {
        if (i == j) {
          continue;
        }
        if (claims[i].x2 > claims[j].x1 && claims[i].x1 < claims[j].x2 && claims[i].y2 > claims[j].y1
          && claims[i].y1 < claims[j].y2) {
          overlaps = true;
          break;
        }
      }
      if (!overlaps) {
        return claims[i].id;
      }
    }
    return 0;
  }

  /** Get the input data for this solution. */
  private Claim[] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2018, 3)).stream().map(Claim::new).toArray(Claim[]::new);
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Claim {

    private static final Pattern DIGITS = Pattern.compile("\\d+");

    final int id;

    final int x1;

    final int y1;

    final int x2;

    final int y2;

    Claim(final String input) {
      Matcher match = DIGITS.matcher(input);
      match.find();
      id = Integer.parseInt(match.group());
      match.find();
      x1 = Integer.parseInt(match.group());
      match.find();
      y1 = Integer.parseInt(match.group());
      match.find();
      x2 = x1 + Integer.parseInt(match.group());
      match.find();
      y2 = y1 + Integer.parseInt(match.group());
    }

  }

}
