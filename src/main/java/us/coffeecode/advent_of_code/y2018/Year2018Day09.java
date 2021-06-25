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
package us.coffeecode.advent_of_code.y2018;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2018/day/9">Year 2018, day 9</a>. This problem asks us to simulate a marble game
 * and find a score. Elves take turns inserting marbles into a circle following certain rules, and we need to know the
 * highest score when the game is complete. Parts one and two differ only in the number of marbles.
 * </p>
 * <p>
 * My first implementation used an array sized for the number of marbles, as a type of optimized circular buffer. The
 * problem was that part two absolutely destroys data structures that are not efficient. I switched to a doubly and
 * circular linked list instead. This ended up taking less memory and several orders of magnitude less time. Using a
 * custom linked list implementation was overall better than reusing Java's LinkedList class because I can embed a
 * primitive value directly in the class, removing boxing and unboxing; I can make it linked in a circle; and access is
 * consistent since there are no ends to wrap around.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2018Day09 {

  public long calculatePart1() {
    return calculate(false);
  }

  public long calculatePart2() {
    return calculate(true);
  }

  private long calculate(final boolean partTwo) {
    final int[] input = getInput();

    // The initial state is after the first turn, placing marble 0.
    int currentPlayer = 0;
    final int marbles = (partTwo ? 100 : 1) * (input[1] + 1);
    final long[] scores = new long[input[0]];

    Marble current = new Marble(0);
    current.next = current.previous = current;

    // i is the next marble to place.
    for (int i = 1; i < marbles; ++i) {
      ++currentPlayer;
      if (currentPlayer == scores.length) {
        currentPlayer = 0;
      }

      if (i % 23 == 0) {
        current = current.previous.previous.previous.previous.previous.previous;
        scores[currentPlayer] += i + current.previous.value;
        current.previous.previous.next = current;
        current.previous = current.previous.previous;
      }
      else {
        current = current.next;
        Marble inserted = new Marble(i);
        inserted.next = current.next;
        inserted.previous = current;
        inserted.previous.next = inserted;
        inserted.next.previous = inserted;
        current = inserted;
      }
    }

    return Arrays.stream(scores).max().getAsLong();
  }

  private static final Pattern DIGITS = Pattern.compile("\\d+");

  /** Get the input data for this solution. */
  private int[] getInput() {
    try {
      final Matcher match = DIGITS.matcher(Files.readString(Utils.getInput(2018, 9)).trim());
      match.find();
      final int players = Integer.parseInt(match.group());
      match.find();
      final int maxMarble = Integer.parseInt(match.group());
      return new int[] { players, maxMarble };
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Marble {

    final int value;

    Marble next;

    Marble previous;

    Marble(final int _value) {
      value = _value;
    }

  }

}
