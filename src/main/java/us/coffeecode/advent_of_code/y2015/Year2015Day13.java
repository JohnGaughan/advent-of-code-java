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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Collections2;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 13)
@Component
public class Year2015Day13 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return getMaxHappiness(getInput(pc));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return getMaxHappiness(addMyself(getInput(pc)));
  }

  /** Get the maximum happiness value. */
  private long getMaxHappiness(final Map<String, Map<String, Integer>> rules) {
    long highest = 0;
    for (final List<String> ordering : Collections2.permutations(rules.keySet())) {
      highest = Math.max(highest, happiness(rules, ordering));
    }
    return highest;
  }

  /** Get the total happiness for the given ordering. */
  private long happiness(final Map<String, Map<String, Integer>> rules, final List<String> ordering) {
    long happiness = 0;
    for (int i = 0; i < ordering.size(); ++i) {
      final String left = ordering.get((i == 0) ? (ordering.size() - 1) : (i - 1));
      final String right = ordering.get((i == (ordering.size() - 1)) ? 0 : (i + 1));
      final Map<String, Integer> guestRules = rules.get(ordering.get(i));
      happiness += guestRules.get(left)
                             .intValue();
      happiness += guestRules.get(right)
                             .intValue();
    }
    return happiness;
  }

  private static final String ME = "Me";

  private static final Integer ZERO = Integer.valueOf(0);

  /** Add myself to the seating rules. */
  private Map<String, Map<String, Integer>> addMyself(final Map<String, Map<String, Integer>> rules) {
    final Map<String, Integer> myRules = new HashMap<>();
    for (final Map.Entry<String, Map<String, Integer>> entry : rules.entrySet()) {
      myRules.put(entry.getKey(), ZERO);
      entry.getValue()
           .put(ME, ZERO);
    }
    rules.put(ME, myRules);
    return rules;
  }

  /** Get the input data for this solution. */
  private Map<String, Map<String, Integer>> getInput(final PuzzleContext pc) {
    final Map<String, Map<String, Integer>> results = new HashMap<>();
    for (final Rule rule : il.linesAsObjects(pc, Rule::new)) {
      if (!results.containsKey(rule.person1)) {
        results.put(rule.person1, new HashMap<>());
      }
      results.get(rule.person1)
             .put(rule.person2, Integer.valueOf(rule.happiness));
    }
    return results;
  }

  /** Represents one rule in the input file. */
  private static final class Rule {

    private static final Pattern SPLIT = Pattern.compile(" ");

    final String person1;

    final String person2;

    final int happiness;

    Rule(final String line) {
      // chomp off the trailing period
      final String[] tokens = SPLIT.split(line.substring(0, line.length() - 1));
      person1 = tokens[0];
      person2 = tokens[10];

      int h = Integer.parseInt(tokens[3]);
      if ("lose".equals(tokens[2])) {
        h = -h;
      }
      happiness = h;
    }
  }

}
