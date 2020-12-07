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
package net.johngaughan.advent.y2020.day4;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * Day Four, Part One.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day4Part1
implements AdventProblem {

  private static final class Validator
  implements Predicate<Map<String, String>> {

    public static final Set<String> FIELD_NAMES = Collections.unmodifiableSet(
      Arrays.stream(new String[] { "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid" }).collect(
        Collectors.toSet()));

    /** {@inheritDoc} */
    @Override
    public boolean test(final Map<String, String> t) {
      for (String field : FIELD_NAMES) {
        if (!"cid".equals(field) && !t.containsKey(field)) {
          return false;
        }
      }
      return true;
    }

  }

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    return new Parser().parse(path).stream().filter(new Validator()).count();
  }

}
