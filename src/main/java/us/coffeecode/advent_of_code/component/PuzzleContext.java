/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * The puzzle context contains information about a specific test being run. This includes all of its metadata needed to
 * identify the solution (year and day) along with the part and the input ID or file being used. It also contains
 * properties defined for the test, and this is where the logic for determining which property to use for a variable
 * identifier is located.
 */
public final class PuzzleContext {

  private final int year;

  private final int day;

  private final int part;

  private final String inputId;

  private final String baseInputId;

  private final String baseInputIdNameOnly;

  private final String answer;

  private final Map<String, String> parameters;

  private final String toString;

  private final int hashCode;

  public PuzzleContext(final int _year, final int _day, final int _part, final String _inputId, final String _answer, final Map<String, String> _parameters) {
    year = _year;
    day = _day;
    part = _part;
    inputId = _inputId;
    answer = _answer;
    parameters = _parameters;
    toString = "(" + year + "," + day + "," + part + "," + inputId + "=" + answer + ")";
    hashCode = toString.hashCode();
    final int idx = inputId.indexOf('.');
    if (idx < 0) {
      baseInputId = inputId;
    }
    else {
      baseInputId = inputId.substring(0, idx);
    }
    int end = baseInputId.length();
    for (int i = 0; i < baseInputId.length(); ++i) {
      if (Character.isDigit(baseInputId.codePointAt(i))) {
        end = i;
        break;
      }
    }
    baseInputIdNameOnly = baseInputId.substring(0, end);
  }

  public int getYear() {
    return year;
  }

  public int getDay() {
    return day;
  }

  public int getPart() {
    return part;
  }

  public String getInputId() {
    return inputId;
  }

  public String getAnswer() {
    return answer;
  }

  public boolean getBoolean(final String name) {
    return Boolean.parseBoolean(getString(name));
  }

  public int getInt(final String name) {
    try {
      return Integer.parseInt(getString(name));
    }
    catch (NumberFormatException ex) {
      throw new RuntimeException("Could not parse value identified by [" + name + "]", ex);
    }
  }

  public long getLong(final String name) {
    try {
      return Long.parseLong(getString(name));
    }
    catch (NumberFormatException ex) {
      throw new RuntimeException("Could not parse value identified by [" + name + "]", ex);
    }
  }

  public BigDecimal getBigDecimal(final String name) {
    try {
      return new BigDecimal(getString(name));
    }
    catch (NumberFormatException ex) {
      throw new RuntimeException("Could not parse value identified by [" + name + "]", ex);
    }
  }

  /**
   * Get a string parameter as defined in the puzzle parameters. If not specified, returns the default value. Property
   * keys are checked in a specific order, which allows more specific keys to override more general keys. Consider a
   * hypothetical parameter named "widget" and an input of "example01.txt" run in part one of a solution. Property keys
   * will be checked in this order until a match is found:
   * <ol>
   * <li><code>widget.example01.part1</code></li>
   * <li><code>widget.example.part1</code></li>
   * <li><code>widget.example01</code></li>
   * <li><code>widget.example</code></li>
   * <li><code>widget.part1</code></li>
   * <li><code>widget</code></li>
   * </ol>
   */
  public String getString(final String name) {
    // First, look for a key that matches completely.
    {
      final String key = name + "." + baseInputId + ".part" + part;
      if (parameters.containsKey(key)) {
        return parameters.get(key);
      }
    }

    // Second, look for a key that matches completely using the input ID name only.
    {
      final String key = name + "." + baseInputIdNameOnly + ".part" + part;
      if (parameters.containsKey(key)) {
        return parameters.get(key);
      }
    }

    // Third, look for a key that matches the input ID without part.
    {
      final String key = name + "." + baseInputId;
      if (parameters.containsKey(key)) {
        return parameters.get(key);
      }
    }

    // Fourth, look for a key that matches the input ID name only without part.
    {
      final String key = name + "." + baseInputIdNameOnly;
      if (parameters.containsKey(key)) {
        return parameters.get(key);
      }
    }

    // Fifth, look for a key that matches the part only.
    {
      final String key = name + ".part" + part;
      if (parameters.containsKey(key)) {
        return parameters.get(key);
      }
    }

    // Finally, look for a key that matches the parameter name only.
    if (parameters.containsKey(name)) {
      return parameters.get(name);
    }

    throw new IllegalArgumentException("Unable to locate parameter identified by [" + name + "]");
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return toString;
  }
}
