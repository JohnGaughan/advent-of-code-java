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
package net.johngaughan.advent.y2020.day7;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p>
 * File parser for year 2020, day 7.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
final class Parser {

  /** Matches the delimiter between the bag and the bags it contains. */
  private static final Pattern LINE_SPLIT = Pattern.compile(" bags contain ");

  /** Matches the delimiter between bag types. */
  private static final Pattern LIST_SPLIT = Pattern.compile(", ");

  public Map<String, Map<String, Integer>> parse(final Path path) {
    try {
      return parse(Files.readAllLines(path));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private Map<String, Map<String, Integer>> parse(final List<String> lines) {
    final Map<String, Map<String, Integer>> rules = new HashMap<>();
    for (final String line : lines) {
      final String[] lineParts = LINE_SPLIT.split(line);
      // Strip off the trailing period.
      final Map<String, Integer> value = parse(lineParts[1].substring(0, lineParts[1].length() - 1));
      rules.put(lineParts[0], value);
    }
    return rules;
  }

  private Map<String, Integer> parse(final String line) {
    final Map<String, Integer> rules = new HashMap<>();

    // Only populate if there are children: otherwise keep it empty, to avoid null checks when using it.
    if (!"no other bags".equals(line)) {
      for (String element : LIST_SPLIT.split(line)) {
        final int space = element.indexOf(' ');

        // Number is everything up to the first space
        final Integer value = Integer.parseInt(element.substring(0, space));

        // Color is everything after the first space up until "bag" or "bags"
        final int bag = element.indexOf(" bag");
        final String key = element.substring(space + 1, bag);

        rules.put(key, value);
      }
    }
    return rules;
  }

}
