/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2021;

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

@AdventOfCodeSolution(year = 2021, day = 14, title = "Extended Polymerization")
@Component
public final class Year2021Day14 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, 10);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, 40);
  }

  private long calculate(final PuzzleContext pc, final int iterations) {
    final Input input = getInput(pc);

    // Track the count of each individual character.
    final Map<Integer, Long> singleCount = new HashMap<>();
    for (int i = 0; i < input.template.length(); ++i) {
      final Integer key = Integer.valueOf(input.template.codePointAt(i));
      singleCount.merge(key, ONE, (old, val) -> Long.valueOf(old.longValue() + 1));
    }

    // Track the count of each pair of characters. Try to avoid resizing hash maps.
    final int pairSize = (input.rules.size() << 2) / 3;
    Map<String, Long> pairCount = new HashMap<>(pairSize);
    for (int i = 0; i < input.template.length() - 1; ++i) {
      final String key = input.template.substring(i, i + 2);
      pairCount.merge(key, ONE, (old, val) -> Long.valueOf(old.longValue() + 1));
    }

    // Iterate!
    for (int i = 0; i < iterations; ++i) {

      // Cannot increment frequencies in place. Earlier substitutions will affect later ones.
      final Map<String, Long> nextPairCount = new HashMap<>(pairSize);

      // Iterate over each pair and add its data to the new maps.
      for (final var entry : pairCount.entrySet()) {
        final String key = entry.getKey();
        final Long dfault = entry.getValue();
        final long value = dfault.longValue();

        // Increment the code point count.
        final Integer newCodePoint = input.rules.get(key);
        singleCount.merge(newCodePoint, dfault, (old, val) -> Long.valueOf(old.longValue() + value));

        // Add the new pairs.
        final String leftPair = str(key.codePointAt(0), newCodePoint.intValue());
        nextPairCount.merge(leftPair, dfault, (old, val) -> Long.valueOf(old.longValue() + value));

        final String rightPair = str(newCodePoint.intValue(), key.codePointAt(1));
        nextPairCount.merge(rightPair, dfault, (old, val) -> Long.valueOf(old.longValue() + value));
      }

      // Override the previous pair frequency counts: inserting new elements broke the old pairs.
      pairCount = nextPairCount;
    }

    // Get the two extreme frequencies and calculate their difference.
    final long most = singleCount.values().stream().mapToLong(Long::longValue).max().getAsLong();
    final long least = singleCount.values().stream().mapToLong(Long::longValue).min().getAsLong();
    return most - least;
  }

  private String str(final int a, final int b) {
    return new String(new int[] { a, b }, 0, 2);
  }

  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);
    final String template = groups.getFirst().getFirst();
    final Map<String, Integer> rules = new HashMap<>(groups.get(1).size());
    for (final String line : groups.get(1)) {
      final String[] tokens = SPLIT.split(line);
      rules.put(tokens[0], Integer.valueOf(tokens[1].codePointAt(0)));
    }
    return new Input(template, rules);
  }

  private static final Long ONE = Long.valueOf(1);

  private static final Pattern SPLIT = Pattern.compile(" -> ");

  private record Input(String template, Map<String, Integer> rules) {}
}
