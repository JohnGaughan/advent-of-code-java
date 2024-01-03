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

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 16, title = "Aunt Sue")
@Component
public final class Year2015Day16 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final List<Rule> rules = il.linesAsObjects(pc, Rule::new).stream().filter(r -> r.check1("children", 3) && r.check1("cats", 7)
      && r.check1("samoyeds", 2) && r.check1("pomeranians", 3) && r.check1("akitas", 0) && r.check1("vizslas", 0)
      && r.check1("goldfish", 5) && r.check1("trees", 3) && r.check1("cars", 2) && r.check1("perfumes", 1)).toList();
    if (rules.size() == 1) {
      return rules.getFirst().auntId;
    }
    return 0;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<Rule> rules = il.linesAsObjects(pc, Rule::new).stream().filter(r -> r.check2("children", 3) && r.check2("cats", 7)
      && r.check2("samoyeds", 2) && r.check2("pomeranians", 3) && r.check2("akitas", 0) && r.check2("vizslas", 0)
      && r.check2("goldfish", 5) && r.check2("trees", 3) && r.check2("cars", 2) && r.check2("perfumes", 1)).toList();
    if (rules.size() == 1) {
      return rules.getFirst().auntId;
    }
    return 0;
  }

  private static final class Rule {

    private static final Pattern SPLIT = Pattern.compile(" ");

    final long auntId;

    final Map<String, Integer> attributes = new HashMap<>();

    Rule(final String input) {
      final String[] tokens = SPLIT.split(input.replace(":", "").replace(",", ""));
      auntId = Long.parseLong(tokens[1]);
      for (int i = 3; i < tokens.length; i += 2) {
        // Just in case there is an odd number of tokens...
        attributes.put(tokens[i - 1], Integer.valueOf(tokens[i]));
      }
    }

    /** Convenience method for use in stream predicates. */
    boolean check1(final String key, final int value) {
      if (!attributes.containsKey(key)) {
        return true;
      }
      return value == attributes.get(key).intValue();
    }

    /** Convenience method for use in stream predicates. */
    boolean check2(final String key, final int value) {
      if (!attributes.containsKey(key)) {
        return true;
      }
      final int keyValue = attributes.get(key).intValue();
      if ("cats".equals(key) || "trees".equals(key)) {
        return value < keyValue;
      }
      else if ("pomeranians".equals(key) || "goldfish".equals(key)) {
        return value > keyValue;
      }
      return value == keyValue;
    }

  }

}
