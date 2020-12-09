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
package net.johngaughan.advent_of_code.y2015;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/11">Year 2015, day 12</a>. This problem requires parsing a JSON string and
 * extracting numbers from it. Part one requires all numbers, part two omits numbers in or contained in objects that
 * have a "red" property.
 * </p>
 * <p>
 * This is a fairly straightforward problem that requires moving around a JSON model and returning numbers, which are
 * then summed in a string. The only thing I really do not like about this solution is the boolean that changes
 * behavior. However, not including it duplicates a lot of code.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day12 {

  public long calculatePart1(final Path path) {
    return extractNumbers(parse(path), false).stream().mapToLong(l -> l).sum();
  }

  public long calculatePart2(final Path path) {
    return extractNumbers(parse(path), true).stream().mapToLong(l -> l).sum();
  }

  /** Given a general object from a JSON string, extract all of its numbers. */
  private Collection<Long> extractNumbers(final Object obj, final boolean ignoreRed) {
    final Collection<Long> numbers = new ArrayList<>();
    if (obj instanceof Number) {
      numbers.add(((Number) obj).longValue());
    }
    else if (obj instanceof JSONArray) {
      numbers.addAll(extractNumbers((JSONArray) obj, ignoreRed));
    }
    else if (obj instanceof JSONObject) {
      numbers.addAll(extractNumbers((JSONObject) obj, ignoreRed));
    }
    return numbers;
  }

  /** Given an array from a JSON string, extract all of its numbers. */
  private Collection<Long> extractNumbers(final JSONArray array, final boolean ignoreRed) {
    final Collection<Long> numbers = new ArrayList<>();
    for (Iterator<?> iter = array.iterator(); iter.hasNext();) {
      final Object element = iter.next();
      numbers.addAll(extractNumbers(element, ignoreRed));
    }
    return numbers;
  }

  /** Given a JSON object from a JSON string, extract all of its numbers. */
  private Collection<Long> extractNumbers(final JSONObject obj, final boolean ignoreRed) {
    final Collection<Long> numbers = new ArrayList<>();
    boolean skip = false;
    if (ignoreRed) {
      for (String key : obj.keySet()) {
        Object value = obj.opt(key);
        if ((value instanceof String) && "red".equals(value)) {
          skip = true;
          break;
        }
      }
    }
    if (!skip) {
      for (String key : obj.keySet()) {
        numbers.addAll(extractNumbers(obj.get(key), ignoreRed));
      }
    }
    return numbers;
  }

  /** Parse the file located at the provided path location. */
  private JSONArray parse(final Path path) {
    try {
      JSONTokener tokener = new JSONTokener(Files.readString(path));
      return new JSONArray(tokener);
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
