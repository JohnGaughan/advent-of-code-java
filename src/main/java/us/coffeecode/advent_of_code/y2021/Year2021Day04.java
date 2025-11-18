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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SequencedCollection;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 4)
@Component
public final class Year2021Day04 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = il.groupsAsObject(pc, this::parse);
    final Collection<Card> cards = input.cards;
    for (final int draw : input.draw) {
      cards.stream()
           .forEach(card -> card.mark(draw));
      // Check for a winner.
      final SequencedCollection<Card> winners = cards.stream()
                                                     .filter(Card::isWinner)
                                                     .toList();
      if (!winners.isEmpty()) {
        final long score = winners.getFirst()
                                  .score();
        return draw * score;
      }
    }
    return 0;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input input = il.groupsAsObject(pc, this::parse);
    final Collection<Card> cards = input.cards;
    for (final int draw : input.draw) {
      cards.stream()
           .forEach(card -> card.mark(draw));
      // Check for a winner.
      final SequencedCollection<Card> winners = cards.stream()
                                                     .filter(Card::isWinner)
                                                     .toList();
      if (!winners.isEmpty()) {
        // Final remaining card won.
        if (cards.size() == 1) {
          final long score = winners.getFirst()
                                    .score();
          return draw * score;
        }
        // Cull the herd so in future rounds we ignore cards that already won.
        cards.removeAll(winners);
      }
    }
    return 0;
  }

  private static final Pattern LIST_SPLIT = Pattern.compile(",");

  private Input parse(final List<List<String>> groups) {
    final int[] draw = Arrays.stream(LIST_SPLIT.split(groups.getFirst()
                                                            .getFirst()))
                             .mapToInt(Integer::parseInt)
                             .toArray();
    final Collection<Card> cards = new ArrayList<>(groups.size() - 1);
    for (int i = 1; i < groups.size(); ++i) {
      cards.add(new Card(groups.get(i)));
    }
    return new Input(draw, cards);
  }

  private static final class Card {

    private static final Pattern BOARD_SPLIT = Pattern.compile("\\s+");

    private final int[][] array;

    // This class deliberately uses identity semantics for equality and hashing.

    Card(final List<String> lines) {
      array = new int[lines.size()][];
      for (int y = 0; y < array.length; ++y) {
        final String line = lines.get(y)
                                 .trim();
        array[y] = Arrays.stream(BOARD_SPLIT.split(line))
                         .mapToInt(Integer::parseInt)
                         .toArray();
      }
    }

    void mark(final int draw) {
      for (int y = 0; y < array.length; ++y) {
        for (int x = 0; x < array[y].length; ++x) {
          if (array[y][x] == draw) {
            array[y][x] = -1;
          }
        }
      }
    }

    long score() {
      return Arrays.stream(array)
                   .mapToInt(row -> Arrays.stream(row)
                                          .filter(i -> i > 0)
                                          .sum())
                   .sum();
    }

    boolean isWinner() {
      row: for (final int[] row : array) {
        boolean winner = true;
        for (int i : row) {
          if (i >= 0) {
            winner = false;
            continue row;
          }
        }
        if (winner) {
          return true;
        }
      }
      column: for (int x = 0; x < array[0].length; ++x) {
        boolean winner = true;
        for (int y = 0; y < array.length; ++y) {
          if (array[y][x] >= 0) {
            winner = false;
            continue column;
          }
        }
        if (winner) {
          return true;
        }
      }
      return false;
    }

  }

  private static record Input(int[] draw, Collection<Card> cards) {}
}
