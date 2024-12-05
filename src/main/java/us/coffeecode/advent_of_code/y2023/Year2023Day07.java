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
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2023, day = 7, title = "Camel Cards")
@Component
public class Year2023Day07 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  public long calculate(final PuzzleContext pc) {
    final boolean wildcardsEnabled = pc.getBoolean("WildcardsEnabled");
    final List<Hand> hands = il.linesAsObjectsMutable(pc, s -> parse(s, wildcardsEnabled), SPLIT);
    Collections.sort(hands, (a, b) -> compare(a, b, wildcardsEnabled));
    return IntStream.range(0, hands.size())
                    .mapToLong(i -> (i + 1) * hands.get(i).bid)
                    .sum();
  }

  /** Compare two hands for order, allowing for wildcards to be turned on or off. */
  private int compare(final Hand h1, final Hand h2, final boolean wildcardsEnabled) {
    int compare = h1.type.compareTo(h2.type);
    if (compare != 0) {
      return compare;
    }
    for (int i = 0; i < h1.cards.length; ++i) {
      compare = Integer.compare(convert(h1.cards[i], wildcardsEnabled), convert(h2.cards[i], wildcardsEnabled));
      if (compare != 0) {
        return compare;
      }
    }
    return 0;
  }

  /**
   * Convert a card represented as a code point into an integer such that different cards can be integer sorted in rank
   * order.
   */
  private int convert(final int card, final boolean wildcardsEnabled) {
    if (Character.isDigit(card)) {
      return card - '0';
    }
    else if (card == 'T') {
      return 10;
    }
    else if (card == 'Q') {
      return 12;
    }
    else if (card == 'K') {
      return 13;
    }
    else if (card == 'A') {
      return 14;
    }
    // 'J' is the only remaining card.
    if (wildcardsEnabled) {
      return 1;
    }
    return 11;
  }

  /** Parse a line from the input file into a hand. Allows the option of wildcards, which can alter a hand's type. */
  private Hand parse(final String[] tokens, final boolean wildcardsEnabled) {
    final int[] cards = tokens[0].codePoints()
                                 .toArray();
    return new Hand(cards, Type.valueOf(cards, wildcardsEnabled), Long.parseLong(tokens[1]));
  }

  private record Hand(int[] cards, Type type, long bid) {}

  /**
   * The type of a hand determines its major rank against other hands. For example: full house beats two pair,
   * regardless of each hand's high card.
   */
  private static enum Type {

    // Order matters: comparison is done by the enum's ordinal, which implicitly increases with later instances.
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND;

    /** Get the type that matches the given cards and whether wildcards are enabled. */
    static Type valueOf(final int[] cards, final boolean wildcardsEnabled) {
      // Wildcards are not enabled and/or there are no wildcards present. Use part one's logic.
      if (!wildcardsEnabled || IntStream.of(cards)
                                        .noneMatch(c -> c == 'J')) {
        final int[] sorted = IntStream.of(cards)
                                      .sorted()
                                      .toArray();
        final long uniques = IntStream.of(sorted)
                                      .distinct()
                                      .count();
        if (uniques == 1) {
          // Only one unique card, so this is the only option.
          return FIVE_OF_A_KIND;
        }
        else if (uniques == 2) {
          // If there are two unique card then there is one point where the card changes. Full house and four of a kind
          // have no overlap with regards to where that change point is.
          if ((sorted[0] == sorted[1]) && (sorted[3] == sorted[4])) {
            return FULL_HOUSE;
          }
          return FOUR_OF_A_KIND;
        }
        else if (uniques == 3) {
          // Three of a kind can fit in three locations within the array.
          if ((sorted[0] == sorted[2]) || (sorted[1] == sorted[3]) || (sorted[2] == sorted[4])) {
            return THREE_OF_A_KIND;
          }
          return TWO_PAIR;
        }
        else if (uniques == 4) {
          // If there is only one duplicate, then the only option is one pair.
          return ONE_PAIR;
        }
        return HIGH_CARD;
      }
      // Wildcards are enabled and there is a wildcard present.
      else {
        final long wild = IntStream.of(cards)
                                   .filter(c -> c == 'J')
                                   .count();
        final int[] sortedNoWild = IntStream.of(cards)
                                            .filter(c -> c != 'J')
                                            .sorted()
                                            .toArray();
        final long uniques = IntStream.of(sortedNoWild)
                                      .distinct()
                                      .count();
        if (wild == 1) {
          if (uniques == 1) {
            return FIVE_OF_A_KIND;
          }
          else if (uniques == 2) {
            if (sortedNoWild[1] == sortedNoWild[2]) {
              // 3-1 split of the four non-wildcards.
              return FOUR_OF_A_KIND;
            }
            // 2-2 split of the four non-wildcards.
            return FULL_HOUSE;
          }
          else if (uniques == 3) {
            return THREE_OF_A_KIND;
          }
          return ONE_PAIR;
        }
        else if (wild == 2) {
          if (uniques == 1) {
            return FIVE_OF_A_KIND;
          }
          else if (uniques == 2) {
            return FOUR_OF_A_KIND;
          }
          return THREE_OF_A_KIND;
        }
        else if (wild == 3) {
          if (uniques == 1) {
            return FIVE_OF_A_KIND;
          }
          return FOUR_OF_A_KIND;
        }
        else if ((wild == 4) || (wild == 5)) {
          return FIVE_OF_A_KIND;
        }
        throw new IllegalArgumentException(Arrays.toString(cards));
      }
    }
  }

  /** Splits tokens in an input line. */
  private static final Pattern SPLIT = Pattern.compile(" ");
}
