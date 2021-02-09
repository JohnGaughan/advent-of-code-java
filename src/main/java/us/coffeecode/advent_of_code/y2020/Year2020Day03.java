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

import java.nio.file.Files;
import java.util.List;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/3">Year 2020, day 3</a>. This problem involves a slope of a hill. Starting
 * in the upper left, how many trees would a person encounter based on the angle they take down the hill? The hill
 * repeats horizontally, but not vertically. Part one asks for the number of tree for a specific angle, while part two
 * asks for the product of several counts of trees for multiple angles.
 * </p>
 * <p>
 * Most of the logic is in the Slope class. It can count the trees encountered by giving it the rise and run angle
 * components. Other than that, not much to say about a fairly simple brute force calculation.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2020Day03 {

  public long calculatePart1() {
    final Slope slope = getInput();
    return slope.countTrees(1, 3);
  }

  public long calculatePart2() {
    final Slope slope = getInput();
    long answer = slope.countTrees(1, 1);
    answer *= slope.countTrees(1, 3);
    answer *= slope.countTrees(1, 5);
    answer *= slope.countTrees(1, 7);
    answer *= slope.countTrees(2, 1);
    return answer;
  }

  /** Get the input data for this solution. */
  private Slope getInput() {
    try {
      return new Slope(Files.readAllLines(Utils.getInput(2020, 3)));
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * This class represents a slope that can be traversed. A slope repeats in the horizontal direction, but not
   * vertically.
   */
  private static final class Slope {

    /**
     * This represents the slope, sort of like Tux Racer. If a particular array element is true, there is a tree at that
     * location: otherwise, it is clear.
     */
    private final boolean[][] slope;

    /** Constructs a <code>Slope</code>. */
    public Slope(final List<String> lines) {
      slope = new boolean[lines.size()][];
      for (int i = 0; i < slope.length; ++i) {
        final String line = lines.get(i);
        slope[i] = new boolean[line.length()];
        for (int j = 0; j < slope[i].length; ++j) {
          final char c = line.charAt(j);
          slope[i][j] = c == '#';
        }
      }
    }

    /** Count the number of trees encountered for the provided row and column changes. */
    public long countTrees(final long rowChange, final long colChange) {
      long trees = 0;
      int row = 0;
      int col = 0;
      while (row < slope.length) {
        if (isTree(row, col)) {
          ++trees;
        }
        row += rowChange;
        col += colChange;
      }
      return trees;
    }

    /** Get whether there is a tree at the given coordinate. */
    private boolean isTree(final int row, final int col) {
      final int colMod = col % slope[row].length;
      return slope[row][colMod];
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      final StringBuilder str = new StringBuilder();
      boolean first = true;
      for (final boolean[] line : slope) {
        if (first) {
          first = false;
        }
        else {
          str.append("\n");
        }
        for (final boolean b : line) {
          str.append(b ? "X" : ".");
        }
      }
      return str.toString();
    }

  }

}
