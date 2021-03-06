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
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/4">Year 2020, day 4</a>. This problem concerns itself with passports.
 * Passports have eight fields, and north pole credentials have all of those except one. Part one asks us to validate
 * the passport data in the input file by simply checking that it exists, while part two has more strict validation on
 * each field. In either case, "cid" is optional.
 * </p>
 * <p>
 * Part one was trivial: simply validate that the required fields are present, removing those passports that have
 * missing fields. Part two does the same, then filters a second time using a more complex predicate class that digs
 * into each field a bit more. The only way I could find to make this more concise affected readability too much, so
 * enjoy Java's lack of brevity.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2020Day04 {

  public long calculatePart1() {
    return getInput().stream().filter(m -> m.keySet().containsAll(REQUIRED_FIELDS)).count();
  }

  public long calculatePart2() {
    return getInput().stream().filter(m -> m.keySet().containsAll(REQUIRED_FIELDS)).filter(
      new ValidatorPart2()).count();
  }

  /** Predicate that validates data for part 2. */
  private static final class ValidatorPart2
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
      if (!HAIR_COLOR.matcher(t.get("hcl")).matches()) {
        return false;
      }
      if (!EYE_COLOR.matcher(t.get("ecl")).matches()) {
        return false;
      }
      if (!PASSPORT_ID.matcher(t.get("pid")).matches()) {
        return false;
      }
      return true;
    }

    /** Validates one of the year fields. */
    boolean validateYear(final String value, final int min, final int max) {
      if (!YEAR.matcher(value).matches()) {
        return false;
      }
      final int numValue = Integer.parseInt(value);
      return min <= numValue && numValue <= max;
    }

    /** Validate the height field. */
    boolean validateHeight(final String value) {
      if (!HEIGHT.matcher(value).matches()) {
        return false;
      }
      final int num = Integer.parseInt(value.substring(0, value.length() - 2));
      if ("cm".equalsIgnoreCase(value.substring(value.length() - 2))) {
        return 150 <= num && num <= 193;
      }
      return 59 <= num && num <= 76;
    }

  }

  /** Field names that are required to be in the input. */
  private static final Set<String> REQUIRED_FIELDS =
    Arrays.stream(new String[] { "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" }).collect(Collectors.toSet());

  /** Pattern that splits a line into individual tokens. */
  private static final Pattern SPLIT_LINE = Pattern.compile(" ");

  /** Pattern that splits a token into its key and value parts. */
  private static final Pattern SPLIT_TOKEN = Pattern.compile(":");

  /** Get the input data for this solution. */
  private Collection<Map<String, String>> getInput() {
    try {
      return Utils.getLineGroups(Files.readAllLines(Utils.getInput(2020, 4))).stream().map(
        line -> Arrays.stream(SPLIT_LINE.split(String.join(" ", line))).map(s -> SPLIT_TOKEN.split(s)).collect(
          Collectors.toMap(s -> s[0], s -> s[1]))).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
