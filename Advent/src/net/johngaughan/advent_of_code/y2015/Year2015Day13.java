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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/13">Year 2015, day 13</a>. The problem involves people sitting around a
 * table who prefer to sit next to certain people: who they sit next to affects their happiness. We need to find the
 * seating arrangement that maximizes total happiness. Part one asks for the maximum happiness given the inputs. Part
 * two says to inject yourself into the seating arrangement and recalculate again, assuming zero change in happiness
 * between you and anyone else.
 * </p>
 * <p>
 * This is a simple job of finding all the permutations, then iterating around the table and calculating the happiness
 * value. For part two, load the data from the input file, then modify it by adding a neutral party before calculating
 * happiness.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public class Year2015Day13 {

  public int calculatePart1(final Path path) {
    return getMaxHappiness(parse(path));
  }

  public int calculatePart2(final Path path) {
    return getMaxHappiness(addMyself(parse(path)));
  }

  /** Get the maximum happiness value. */
  private int getMaxHappiness(final Map<String, Map<String, Integer>> rules) {
    int highest = 0;
    for (final List<String> ordering : Utils.permutations(rules.keySet())) {
      highest = Math.max(highest, happiness(rules, ordering));
    }
    return highest;
  }

  /** Get the total happiness for the given ordering. */
  private int happiness(final Map<String, Map<String, Integer>> rules, final List<String> ordering) {
    int happiness = 0;
    for (int i = 0; i < ordering.size(); ++i) {
      final String left = ordering.get(i == 0 ? ordering.size() - 1 : i - 1);
      final String right = ordering.get(i == ordering.size() - 1 ? 0 : i + 1);
      final Map<String, Integer> guestRules = rules.get(ordering.get(i));
      happiness += guestRules.get(left);
      happiness += guestRules.get(right);
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
      entry.getValue().put(ME, ZERO);
    }
    rules.put(ME, myRules);
    return rules;
  }

  /** Parse the file located at the provided path location. */
  private Map<String, Map<String, Integer>> parse(final Path path) {
    try {
      final Map<String, Map<String, Integer>> results = new HashMap<>();
      for (final Rule rule : Files.readAllLines(path).stream().map(Rule::new).collect(Collectors.toList())) {
        if (!results.containsKey(rule.person1)) {
          results.put(rule.person1, new HashMap<>());
        }
        results.get(rule.person1).put(rule.person2, rule.happiness);
      }
      return results;
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
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
