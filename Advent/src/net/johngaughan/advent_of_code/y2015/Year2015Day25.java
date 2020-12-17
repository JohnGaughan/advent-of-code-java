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
package net.johngaughan.advent_of_code.y2015;

import java.nio.file.Files;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/25">Year 2015, day 25</a>. This is a one-part puzzle that requires
 * performing some calculations a large number of times. The input is coordinates on a grid, where each cell has a
 * unique integer that can be determined by its row and column. Once you have the integer, run the calculations that
 * number of times to get the final answer.
 * </p>
 * <p>
 * The first task is determining the number of iterations. There are patterns in the numbers, such as triangular
 * numbers. However, the important sequence is in the first column: the
 * <a href="https://en.wikipedia.org/wiki/Lazy_caterer%27s_sequence">Lazy caterer's sequence</a>. Correcting for an
 * off-by-one because the program assumes f(1, 1) = 1 instead of zero, we can directly calculate any number in the first
 * column. If we go past the desired row by an amount equal to the column number, we get the diagonal containing the
 * cell we need. Since the numbers simply count up, we can add the column to that calculated value to get the number in
 * that cell. Again, we need to account for an off-by-one because the first "real" cell is given, not calculated. In the
 * code below, the calculation to get that specific grid element is simplified a bit.
 * </p>
 * <p>
 * Once this is done, apply <a href="https://en.wikipedia.org/wiki/Modular_exponentiation">modular exponentiation</a> to
 * get the result. This completes in 21 iterations instead of 1,073,157 (or over 30 million without optimizing away the
 * repetitions) of a brute force implementation. It is far, far more efficient. Note that {@link java.math.BigInteger}
 * has a method to compute this, but it is slower than using primitives. Since the numbers all fit in <code>long</code>,
 * this is the better approach.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day25 {

  private static final long FIRST_VALUE = 20_151_125L;

  private static final long MULTIPLIER = 252_533L;

  private static final long MODULUS = 33_554_393L;

  private static final long PERIOD = MODULUS >> 1;

  public long calculatePart1() {
    final Coordinate crd = getInput();
    final int r = crd.row;
    final int c = crd.col;
    // Calculate the number of iterations required. This is simplified from a more complex equation.
    final long iterations = (r * r + 2 * r * c + c * c - 3 * r - c) / 2;

    // Use modular exponentiation to calculate it.
    long result = 1;
    long base = MULTIPLIER;
    long exponent = iterations % PERIOD;
    while (exponent > 0) {
      if ((exponent & 1) == 1) {
        result = result * base % MODULUS;
      }
      exponent >>= 1;
      base = base * base % MODULUS;
    }
    result = FIRST_VALUE * result % MODULUS;
    return result;
  }

  /** Get the input data for this solution. */
  private Coordinate getInput() {
    try {
      return new Coordinate(Files.readString(Utils.getInput(2015, 25)).trim());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Coordinate {

    private static final String ROW = "row ";

    private static final String COL = "column ";

    final int row;

    final int col;

    Coordinate(final String input) {
      int a = input.indexOf(ROW) + ROW.length();
      int b = input.indexOf(',', a);
      row = Integer.parseInt(input.substring(a, b));

      a = input.indexOf(COL) + COL.length();
      b = input.indexOf('.', a);
      col = Integer.parseInt(input.substring(a, b));
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "[row=" + row + ",col=" + col + "]";
    }
  }

}
