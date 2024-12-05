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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2023, day = 2, title = "Cube Conundrum")
@Component
public class Year2023Day02 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final List<Game> games = il.linesAsObjects(pc, this::parse)
                               .stream()
                               .filter(g -> g.isPossible())
                               .toList();
    return games.stream()
                .mapToLong(g -> g.id)
                .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return il.linesAsObjects(pc, this::parse)
             .stream()
             .mapToLong(g -> g.getPower())
             .sum();
  }

  /** Separate the game number from the hands. */
  private static final Pattern SPLIT_GAME_FROM_HANDS = Pattern.compile(": ");

  /** Separate the hands from each other. */
  private static final Pattern SPLIT_HANDS = Pattern.compile("; ");

  /** Separate the contents of each hand. */
  private static final Pattern SPLIT_COLORS = Pattern.compile(", ");

  /** Separate individual tokens without punctuation. */
  private static final Pattern SPLIT_TOKENS = Pattern.compile(" ");

  private static final String BLUE = "blue";

  private static final String GREEN = "green";

  private static final String RED = "red";

  /** Parse a single line from an input file into a Game record. */
  private Game parse(final String line) {
    final String[] split1 = SPLIT_GAME_FROM_HANDS.split(line);
    final long id = Long.parseLong(SPLIT_TOKENS.split(split1[0])[1]);

    final List<Hand> hands = new ArrayList<>();
    for (final String rawHand : SPLIT_HANDS.split(split1[1])) {
      int blue = 0;
      int green = 0;
      int red = 0;
      for (final String rawColor : SPLIT_COLORS.split(rawHand)) {
        final String[] tokens = SPLIT_TOKENS.split(rawColor);
        if (BLUE.equals(tokens[1])) {
          blue = Integer.parseInt(tokens[0]);
        }
        else if (GREEN.equals(tokens[1])) {
          green = Integer.parseInt(tokens[0]);
        }
        else if (RED.equals(tokens[1])) {
          red = Integer.parseInt(tokens[0]);
        }
        else {
          throw new IllegalArgumentException("Unknown color [" + tokens[1] + "] in line [" + line + "]");
        }
      }
      hands.add(new Hand(red, green, blue));
    }

    return new Game(id, hands);
  }

  private record Hand(int red, int green, int blue) {

    /** Get whether this hand is possible for the requirements in part one. */
    boolean isPossible() {
      return (red <= 12) && (green <= 13) && (blue <= 14);
    }
  }

  private record Game(long id, List<Hand> hands) {

    /** Get whether this game is possible for the requirements in part one. */
    boolean isPossible() {
      return hands.stream()
                  .allMatch(h -> h.isPossible());
    }

    /** Get the power of this game using the requirements in part two. */
    long getPower() {
      long blue = hands.stream()
                       .mapToLong(h -> h.blue)
                       .max()
                       .getAsLong();
      long green = hands.stream()
                        .mapToLong(h -> h.green)
                        .max()
                        .getAsLong();
      long red = hands.stream()
                      .mapToLong(h -> h.red)
                      .max()
                      .getAsLong();
      return blue * green * red;
    }
  }
}
