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
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.johngaughan.advent.y2020.AdventProblem;

/**
 * <p>
 * <Day Four, Part Two.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Day4Part2
implements AdventProblem {

  private static final class Validator
  implements Predicate<Map<String, String>> {

    private static final Pattern YEAR = Pattern.compile("\\d{4}");

    private static final Pattern HEIGHT = Pattern.compile("\\d+(cm|in)");

    private static final Pattern HAIR_COLOR = Pattern.compile("#[0-9a-f]{6}");

    private static final Pattern EYE_COLOR = Pattern.compile("(amb|blu|brn|gry|grn|hzl|oth)");

    private static final Pattern PASSPORT_ID = Pattern.compile("\\d{9}");

    /** {@inheritDoc} */
    @Override
    public boolean test(final Map<String, String> t) {
      if (!validateYear(t.get("byr"), 1920, 2002)) {
        return false;
      }
      if (!validateYear(t.get("iyr"), 2010, 2020)) {
        return false;
      }
      if (!validateYear(t.get("eyr"), 2020, 2030)) {
        return false;
      }
      if (!validateHeight(t.get("hgt"))) {
        return false;
      }
      if (!validateRegex(t.get("hcl"), HAIR_COLOR)) {
        return false;
      }
      if (!validateRegex(t.get("ecl"), EYE_COLOR)) {
        return false;
      }
      if (!validateRegex(t.get("pid"), PASSPORT_ID)) {
        return false;
      }
      return true;
    }

    /** Validates one of the year fields. */
    boolean validateYear(final String value, final int min, final int max) {
      if ((value == null) || !YEAR.matcher(value).matches()) {
        return false;
      }
      final int numValue = Integer.parseInt(value);
      return (min <= numValue) && (numValue <= max);
    }

    /** Validate the height field. */
    boolean validateHeight(final String value) {
      if ((value == null) || !HEIGHT.matcher(value).matches()) {
        return false;
      }
      final int num = Integer.parseInt(value.substring(0, value.length() - 2));
      if ("cm".equalsIgnoreCase(value.substring(value.length() - 2))) {
        return (150 <= num) && (num <= 193);
      }
      return (59 <= num) && (num <= 76);
    }

    /** Validates a field that only needs a regex test. */
    boolean validateRegex(final String value, final Pattern regex) {
      if (value == null) {
        return false;
      }
      return regex.matcher(value).matches();
    }

  }

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    return new Parser().parse(path).stream().filter(new Validator()).count();
  }

}
