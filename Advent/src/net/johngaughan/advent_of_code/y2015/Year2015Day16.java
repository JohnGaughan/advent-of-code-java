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

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/16">Year 2015, day 16</a>. This problem involves figuring out which rule
 * matches a set of criteria: notably, the criteria is incomplete. Part one looks for an exact match based on the
 * criteria present, while part two looks for a match based on data that can vary.
 * </p>
 * <p>
 * There is not much to say about this one. It is fairly straightforward: filter a stream using a predicate that asks
 * each rule if it matches that predicate. Verify the resultant list has one element, then return the ID of that
 * element.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day16 {

  public int calculatePart1(final Path path) {
    final List<Rule> rules =
      parse(path).stream().filter(r -> r.check1("children", 3) && r.check1("cats", 7) && r.check1("samoyeds", 2)
        && r.check1("pomeranians", 3) && r.check1("akitas", 0) && r.check1("vizslas", 0) && r.check1("goldfish", 5)
        && r.check1("trees", 3) && r.check1("cars", 2) && r.check1("perfumes", 1)).collect(Collectors.toList());
    if (rules.size() == 1) {
      return rules.get(0).auntId;
    }
    return Integer.MIN_VALUE;
  }

  public int calculatePart2(final Path path) {
    final List<Rule> rules =
      parse(path).stream().filter(r -> r.check2("children", 3) && r.check2("cats", 7) && r.check2("samoyeds", 2)
        && r.check2("pomeranians", 3) && r.check2("akitas", 0) && r.check2("vizslas", 0) && r.check2("goldfish", 5)
        && r.check2("trees", 3) && r.check2("cars", 2) && r.check2("perfumes", 1)).collect(Collectors.toList());
    if (rules.size() == 1) {
      return rules.get(0).auntId;
    }
    return Integer.MIN_VALUE;
  }

  /** Parse the file located at the provided path location. */
  private List<Rule> parse(final Path path) {
    try {
      return Files.readAllLines(path).stream().map(Rule::new).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Rule {

    private static final Pattern SPLIT = Pattern.compile(" ");

    final int auntId;

    final Map<String, Integer> attributes = new HashMap<>();

    Rule(final String input) {
      final String[] tokens = SPLIT.split(input.replace(":", "").replace(",", ""));
      auntId = Integer.parseInt(tokens[1]);
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
      return attributes.containsKey(key) && value == attributes.get(key);
    }

    /** Convenience method for use in stream predicates. */
    boolean check2(final String key, final int value) {
      if (!attributes.containsKey(key)) {
        return true;
      }
      final int keyValue = attributes.get(key);
      if ("cats".equals(key) || "trees".equals(key)) {
        return value < keyValue;
      }
      else if ("pomeranians".equals(key) || "goldfish".equals(key)) {
        return value > keyValue;
      }
      return value == keyValue;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return Integer.toString(auntId);
    }
  }
}
