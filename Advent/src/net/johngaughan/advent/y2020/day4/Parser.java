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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.johngaughan.advent.y2020.Utils;

/**
 * <p>
 * File parser for day four.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
final class Parser {

  /** Pattern that splits a line into individual tokens. */
  private static final Pattern SPLIT_LINE = Pattern.compile(" ");

  /** Pattern that splits a token into its key and value parts. */
  private static final Pattern SPLIT_TOKEN = Pattern.compile(":");

  public Collection<Map<String, String>> parse(final Path t) {
    try {
      return parsePassports(Files.readAllLines(t));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Given a list of lines from the input file, parse them into passport data. */
  private Collection<Map<String, String>> parsePassports(final List<String> lines) {
    final List<List<String>> lineGroups = Utils.getLineGroups(lines);

    // 2. Create passport data based on the groupings of separated lines.
    final Collection<Map<String, String>> passports = new ArrayList<>(lineGroups.size());
    for (List<String> lineGroup : lineGroups) {
      passports.add(parsePassport(lineGroup));
    }
    return passports;
  }

  /** Parse a group of lines into passport data. */
  private Map<String, String> parsePassport(final List<String> lines) {
    final Map<String, String> fields = new HashMap<>();
    for (String line : lines) {
      for (String token : SPLIT_LINE.split(line.trim())) {
        final String[] parts = SPLIT_TOKEN.split(token.trim());
        fields.put(parts[0].trim(), parts[1].trim());
      }
    }
    return Collections.unmodifiableMap(fields);
  }

}
