/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 3, title = "Mull It Over")
@Component
public class Year2024Day03 {

  private static final Pattern MATCH = Pattern.compile("mul\\(\\d+,\\d+\\)");

  private static final Pattern MATCH_DO = Pattern.compile("do\\(\\)");

  private static final Pattern MATCH_DONT = Pattern.compile("don't\\(\\)");

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Matcher matches = MATCH.matcher(il.lines(pc).stream().reduce(String::concat).get());
    long result = 0;
    while (matches.find()) {
      result += eval(matches.group());
    }
    return result;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final String line = il.lines(pc).stream().reduce(String::concat).get();
    final NavigableMap<Integer, Boolean> enabled = getEnabled(line);
    final Matcher matches = MATCH.matcher(line);
    long result = 0;
    while (matches.find()) {
      if (enabled.floorEntry(Integer.valueOf(matches.start())).getValue().booleanValue()) {
        result += eval(matches.group());
      }
    }
    return result;
  }

  /** Evaluate a single multiply instruction. */
  private long eval(final String s) {
    final int mid = s.indexOf(',');
    return Long.parseLong(s.substring(s.indexOf('(') + 1, mid)) * Long.parseLong(s.substring(mid + 1, s.indexOf(')')));
  }

  /** Get the indices where the enabled flag changes, and what its value is. */
  private NavigableMap<Integer, Boolean> getEnabled(final String line) {
    final NavigableMap<Integer, Boolean> enabled = new TreeMap<>();
    enabled.put(Integer.valueOf(0), Boolean.TRUE);
    final Matcher matchEnabled = MATCH_DO.matcher(line);
    while (matchEnabled.find()) {
      enabled.put(Integer.valueOf(matchEnabled.start()), Boolean.TRUE);
    }
    final Matcher matchDisaabled = MATCH_DONT.matcher(line);
    while (matchDisaabled.find()) {
      enabled.put(Integer.valueOf(matchDisaabled.start()), Boolean.FALSE);
    }
    return enabled;
  }
}
