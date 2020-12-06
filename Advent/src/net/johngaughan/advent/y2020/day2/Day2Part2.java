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
package net.johngaughan.advent.y2020.day2;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.johngaughan.advent.y2020.AdventProblem;

/**
 * <p>
 * Day Two, Part Two.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Day2Part2
implements AdventProblem {

  private static final class PolicyTester
  implements Predicate<String> {

    /** This pattern separates the policy and password on an input line. */
    static final Pattern SPLIT = Pattern.compile(": ");

    /** {@inheritDoc} */
    @Override
    public boolean test(final String t) {
      // Separate the policy from the password
      final String[] pieces = SPLIT.split(t);

      // Get the character to test and the positions to look at.
      final int split1 = pieces[0].indexOf('-');
      final int split2 = pieces[0].indexOf(' ', split1);
      final int position1 = Integer.parseInt(pieces[0].substring(0, split1));
      final int position2 = Integer.parseInt(pieces[0].substring(split1 + 1, split2));
      final char character = pieces[0].charAt(split2 + 1);

      // Count how many times the character appears in the required positions.
      int positions = 0;
      if (pieces[1].charAt(position1 - 1) == character) {
        ++positions;
      }
      if (pieces[1].charAt(position2 - 1) == character) {
        ++positions;
      }
      return positions == 1;
    }

  }

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    return new Parser().parse(path).stream().filter(new PolicyTester()).count();
  }

}
