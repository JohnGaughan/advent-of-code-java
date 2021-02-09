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
package us.coffeecode.advent_of_code.y2015;

import java.nio.file.Files;
import java.util.List;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/8">Year 2015, day 8</a>. This problem asks us to count the number of
 * characters in various strings based on some rules. Specifically, the strings are encoded in a way similar to how
 * compilers expect them in a program: surrounded by double quotes, with some backslashes escaping other characters.
 * Counting the strings as-is means counting their contents without the surrounding quotes. Part one asks us to figure
 * out how many characters are inside the quotes after parsing the escapes, and subtracting from the first calculation.
 * Part two asks us to count how many characters each string would take up after escaping them and surrounding with new
 * quotes, then subtracting the first number from this.
 * </p>
 * <p>
 * The solution is made simpler than it otherwise would be due to the input all being well-formed, so minimal validation
 * necessary; as well as the fact that only the string lengths are needed, not the entire strings. Simply counting the
 * characters that <i>would</i> be there makes this program shorter than if it needed full validation and construction
 * of strings.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2015Day08 {

  public int calculatePart1() {
    final List<String> lines = getInput();
    final int total = lines.stream().mapToInt(s -> s.length()).sum();
    final int parsed = lines.stream().mapToInt(s -> countParsedCharacters(s)).sum();
    return total - parsed;
  }

  /** Count the number of characters in the line, considering escape sequences. */
  private int countParsedCharacters(final String line) {
    int characters = 0;
    // Iterate over everything inside the apostrophes containing the string
    for (int i = 1; i < line.length() - 1; ++i) {
      ++characters;
      if (line.codePointAt(i) == '\\') {
        // If this character begins an escape, advance the loop pointer appropriately to skip characters.
        ++i;
        if (i + 2 < line.length() - 1 && line.codePointAt(i) == 'x') {
          i += 2;
        }
      }
    }
    return characters;
  }

  public int calculatePart2() {
    final List<String> lines = getInput();
    final int total = lines.stream().mapToInt(s -> s.length()).sum();
    final int reverseParsed = lines.stream().mapToInt(s -> countReverseParsedCharacters(s)).sum();
    return reverseParsed - total;
  }

  private int countReverseParsedCharacters(final String line) {
    int characters = 2;
    for (final int ch : line.toCharArray()) {
      if (ch == '"' || ch == '\\') {
        ++characters;
      }
      ++characters;
    }
    return characters;
  }

  /** Get the input data for this solution. */
  private List<String> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2015, 8));
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
