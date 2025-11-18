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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 8)
@Component
public final class Year2015Day08 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Collection<String> lines = il.lines(pc);
    final long total = lines.stream()
                            .mapToLong(String::length)
                            .sum();
    final long parsed = lines.stream()
                             .mapToLong(this::countParsedCharacters)
                             .sum();
    return total - parsed;
  }

  /** Count the number of characters in the line, considering escape sequences. */
  private long countParsedCharacters(final String line) {
    long characters = 0;
    // Iterate over everything inside the apostrophes containing the string
    for (int i = 1; i < line.length() - 1; ++i) {
      ++characters;
      if (line.codePointAt(i) == '\\') {
        // If this character begins an escape, advance the loop pointer appropriately to skip characters.
        ++i;
        if (((i + 2) < (line.length() - 1)) && (line.codePointAt(i) == 'x')) {
          i += 2;
        }
      }
    }
    return characters;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Collection<String> lines = il.lines(pc);
    final long total = lines.stream()
                            .mapToLong(String::length)
                            .sum();
    final long reverseParsed = lines.stream()
                                    .mapToLong(this::countReverseParsedCharacters)
                                    .sum();
    return reverseParsed - total;
  }

  private long countReverseParsedCharacters(final String line) {
    return 2 + line.length() + line.codePoints()
                                   .filter(ch -> ((ch == '"') || (ch == '\\')))
                                   .count();
  }

}
