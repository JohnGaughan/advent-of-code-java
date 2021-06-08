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
import java.util.List;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/6">Year 2016, day 6</a>. This problem asks us to perform frequency
 * analysis on letters across many strings, then find the most or least frequent character in each position. Concatenate
 * them all to get the result string.
 * </p>
 * <p>
 * This is a fairly simple exercise. First, stuff the data into an array representing frequency. The first ordinal is
 * the position in the string, and the second is the letter. The value in each cell is how many times it appears. Once
 * this is done, all we need to do is iterate and keep track of which letter is most - or least - frequent in that
 * position, then concatenate it onto a StringBuilder. Then simply return the string.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day06 {

  public String calculatePart1() {
    return calculate(true);
  }

  public String calculatePart2() {
    return calculate(false);
  }

  private String calculate(final boolean gt) {
    int[][] frequency = new int[8][26];
    for (String line : getInput()) {
      for (int i = 0; i < line.length(); ++i) {
        frequency[i][line.codePointAt(i) - 'a']++;
      }
    }
    StringBuilder message = new StringBuilder(frequency.length);
    for (int i = 0; i < frequency.length; ++i) {
      int candidate = -1;
      for (int ch = 0; ch < frequency[i].length; ++ch) {
        if (candidate < 0) {
          candidate = ch;
        }
        else if (gt && frequency[i][candidate] < frequency[i][ch]) {
          candidate = ch;
        }
        else if (!gt && frequency[i][candidate] > frequency[i][ch]) {
          candidate = ch;
        }
      }
      message.appendCodePoint(candidate + 'a');
    }
    return message.toString();
  }

  /** Get the input data for this solution. */
  private List<String> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 6));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
