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
package us.coffeecode.advent_of_code.y2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SequencedCollection;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2023, day = 4, title = "Scratchcards")
@Component
public class Year2023Day04 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return il.linesAsObjects(pc, this::parse).stream().mapToLong(Card::score).sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final SequencedCollection<Card> cards = il.linesAsObjects(pc, this::parse, TreeSet::new);
    // Store the value of each card.
    final Map<Integer, Long> scores = new HashMap<>(cards.size() << 1);
    // Work backwards so that card values are already memoized before being referenced.
    for (final Card card : cards.reversed()) {
      final int id = card.id;
      final int m = card.matches;
      final long value = 1 + IntStream.range(id + 1, id + 1 + m).boxed().mapToLong(k -> scores.get(k).longValue()).sum();
      scores.put(Integer.valueOf(id), Long.valueOf(value));
    }
    return scores.values().stream().mapToLong(Long::longValue).sum();
  }

  /** Match either a colon or pipe, splitting a line into exactly three pieces. */
  private static final Pattern SPLIT_FIRST = Pattern.compile("[:|]");

  /** Match any number of spaces. */
  private static final Pattern SPLIT_TOKENS = Pattern.compile("\\s+");

  /** Parse one line from the input file into one Card record. */
  private Card parse(final String line) {
    final String[] tokens = SPLIT_FIRST.split(line);
    final int id = Integer.parseInt(SPLIT_TOKENS.split(tokens[0])[1]);
    final Set<Integer> numbers = toNumbers(tokens[1]);
    numbers.retainAll(toNumbers(tokens[2]));
    return new Card(id, numbers.size());
  }

  /** Convert a space-delimited list of numbers in string format into a set of integers. */
  private Set<Integer> toNumbers(final String string) {
    return Arrays.stream(SPLIT_TOKENS.split(string.trim())).map(Integer::valueOf).collect(Collectors.toSet());
  }

  private record Card(int id, int matches)
  implements Comparable<Card> {

    /** Calculate the score of the card per the requirements in part one. */
    int score() {
      return (1 << matches) >> 1;
    }

    @Override
    public int compareTo(final Card o) {
      return Integer.compare(id, o.id);
    }
  }
}
