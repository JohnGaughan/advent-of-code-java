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

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 12, title = "JSAbacusFramework.io")
@Component
public final class Year2015Day12 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return extractNumbers(getInput(pc), false).stream().mapToLong(Integer::longValue).sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return extractNumbers(getInput(pc), true).stream().mapToLong(Integer::longValue).sum();
  }

  /** Given a general object from a JSON string, extract all of its numbers. */
  private Collection<Integer> extractNumbers(final Object obj, final boolean ignoreRed) {
    final Collection<Integer> numbers = new ArrayList<>();
    if (obj instanceof Integer i) {
      numbers.add(i);
    }
    else if (obj instanceof JSONArray a) {
      numbers.addAll(extractNumbers(a, ignoreRed));
    }
    else if (obj instanceof JSONObject o) {
      numbers.addAll(extractNumbers(o, ignoreRed));
    }
    return numbers;
  }

  /** Given an array from a JSON string, extract all of its numbers. */
  private Collection<Integer> extractNumbers(final JSONArray array, final boolean ignoreRed) {
    final Collection<Integer> numbers = new ArrayList<>();
    for (final Object element : array) {
      numbers.addAll(extractNumbers(element, ignoreRed));
    }
    return numbers;
  }

  /** Given a JSON object from a JSON string, extract all of its numbers. */
  private Collection<Integer> extractNumbers(final JSONObject obj, final boolean ignoreRed) {
    final Collection<Integer> numbers = new ArrayList<>();
    boolean skip = false;
    if (ignoreRed) {
      for (final String key : obj.keySet()) {
        final Object value = obj.opt(key);
        if (value instanceof String && "red".equals(value)) {
          skip = true;
          break;
        }
      }
    }
    if (!skip) {
      for (final String key : obj.keySet()) {
        numbers.addAll(extractNumbers(obj.get(key), ignoreRed));
      }
    }
    return numbers;
  }

  /** Get the input data for this solution. */
  private Object getInput(final PuzzleContext pc) {
    return new JSONTokener(il.fileAsString(pc)).nextValue();
  }

}
