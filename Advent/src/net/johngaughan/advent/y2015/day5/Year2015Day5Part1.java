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
package net.johngaughan.advent.y2015.day5;

import java.nio.file.Path;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * Year 2015, Day 5, Part 1.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day5Part1
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    long nice = 0;
    for (String line : new Parser().parse(path)) {
      if (isNice(line)) {
        ++nice;
      }
    }
    return nice;
  }

  private boolean isNice(final String str) {
    // 1. three vowels
    // 2. at least one double letter
    // 3. does not have ab, cd, pq, or xy
    int vowels = 0;
    boolean repeated = false;
    for (int i = 0; i < str.length(); ++i) {
      char c = str.charAt(i);
      if ((c == 'a') || (c == 'e') || (c == 'i') || (c == 'o') || (c == 'u')) {
        ++vowels;
      }
      if (i > 0) {
        char previous = str.charAt(i - 1);
        if (previous == c) {
          repeated = true;
        }
        if (((previous == 'a') && (c == 'b')) || ((previous == 'c') && (c == 'd')) || ((previous == 'p') && (c == 'q'))
          || ((previous == 'x') && (c == 'y'))) {
          return false;
        }
      }
    }
    return (vowels >= 3) && repeated;
  }

}
