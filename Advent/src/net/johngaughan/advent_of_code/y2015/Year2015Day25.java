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
import java.nio.file.Path;

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
 * that cell. Again, we need to account for an off-by-one because the first "real" cell is given, not calculated.
 * </p>
 * <p>
 * Once this is done, we simply do the math over and over to get the result. This is a general hashing algorithm that
 * performs modulo arithmetic: that means it cannot produce any value greater than the modulo, nor smaller than zero.
 * Since the multiplicand and modulo are both prime, it has a long sequence before it repeats. In this specific case,
 * the length of the sequence is half of the modulo, rounding down. This shaves off the vast majority of calculations,
 * which are not required because at one point, the calculations repeat. While the algorith completes in a few tens of
 * milliseconds in either case, I chose to optimize this because I did the math so I may as well use it.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day25 {

  private static final long MULTIPLIER = 252_533L;

  private static final long DIVISOR = 33_554_393L;

  private static final long PERIOD = DIVISOR / 2;

  public long calculatePart1(final Path path) {
    final Coordinate c = parse(path);
    // Calculate the number of iterations required. Note that "n+2" in the sequence simplifies with the -2 from the row
    // and column being one-indexed.
    final long iterations = (((c.row + c.col - 2) * (c.row + c.col - 2) + c.row + c.col) / 2 + c.col - 2) % PERIOD;

    // Hash around a million times.
    long output = 20_151_125L;
    for (int i = 0; i < iterations; ++i) {
      output *= MULTIPLIER;
      output %= DIVISOR;
    }
    return output;
  }

  /** Parse the file located at the provided path location. */
  private Coordinate parse(final Path path) {
    try {
      return new Coordinate(Files.readString(path).trim());
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
