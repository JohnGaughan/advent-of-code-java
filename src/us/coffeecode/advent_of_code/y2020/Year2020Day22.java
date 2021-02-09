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
package us.coffeecode.advent_of_code.y2020;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/22">Year 2020, day 22</a>. This puzzle requires us to play a simple card
 * game called &quot;Combat&quot; which is very similar to War. One helpful property of the game and the input data is
 * there are no ties: every card has a unique rank. Part one asks us to simulate a regular game, while part two adds
 * some new rules involve recursive sub-games in certain circumstances.
 * </p>
 * <p>
 * This is a fairly simple recursive algorithm, definitely simpler than some others in Advent of Code. Each sub-game is
 * completely separate from the parent games, only needing parent game state to set up the hands for the sub-game.
 * Compared to combinations and permutations, this is a lot simpler to implement.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public class Year2020Day22 {

  public long calculatePart1() {
    final List<Deque<Integer>> hands = getInput();
    final Deque<Integer> p1 = hands.get(0);
    final Deque<Integer> p2 = hands.get(1);
    while (!p1.isEmpty() && !p2.isEmpty()) {
      final Integer c1 = p1.remove();
      final Integer c2 = p2.remove();
      if (c1.compareTo(c2) > 0) {
        p1.add(c1);
        p1.add(c2);
      }
      else {
        p2.add(c2);
        p2.add(c1);
      }
    }
    return score(p1.isEmpty() ? p2 : p1);
  }

  public long calculatePart2() {
    List<Deque<Integer>> hands = getInput();
    return play(hands.get(0), hands.get(1)).score;
  }

  /** Play a game of recursive combat. */
  @SuppressWarnings("boxing")
  private RecursiveResult play(final Deque<Integer> p1, final Deque<Integer> p2) {
    Set<GameState> oldStates = new HashSet<>();
    while (!p1.isEmpty() && !p2.isEmpty()) {
      final GameState currentState = new GameState(p1, p2);
      if (oldStates.contains(currentState)) {
        return new RecursiveResult(true, score(p1));
      }
      oldStates.add(currentState);
      final int c1 = p1.remove().intValue();
      final int c2 = p2.remove().intValue();
      // See if we need to recurse
      if (p1.size() >= c1 && p2.size() >= c2) {
        // Recurse
        final Deque<Integer> p1new = new LinkedList<>();
        Iterator<Integer> iter = p1.iterator();
        for (int i = 0; i < c1; ++i) {
          p1new.add(iter.next());
        }
        final Deque<Integer> p2new = new LinkedList<>();
        iter = p2.iterator();
        for (int i = 0; i < c2; ++i) {
          p2new.add(iter.next());
        }
        RecursiveResult result = play(p1new, p2new);
        if (result.p1won) {
          p1.add(c1);
          p1.add(c2);
        }
        else {
          p2.add(c2);
          p2.add(c1);
        }
      }
      // Play as normal
      else if (c1 > c2) {
        p1.add(c1);
        p1.add(c2);
      }
      else {
        p2.add(c2);
        p2.add(c1);
      }
    }
    final boolean p1won = p2.isEmpty();
    return new RecursiveResult(p1won, score(p1won ? p1 : p2));
  }

  private int score(final Deque<Integer> hand) {
    int score = 0;
    int multiplier = 1;
    while (!hand.isEmpty()) {
      score += multiplier * hand.removeLast().intValue();
      ++multiplier;
    }
    return score;
  }

  /** Result of a recursive game - who won, and what was the score? */
  private static final class RecursiveResult {

    final boolean p1won;

    final int score;

    RecursiveResult(final boolean b, final int i) {
      p1won = b;
      score = i;
    }
  }

  /** Represents one historical state of the game. */
  private static final class GameState {

    private final Integer[] p1;

    private final Integer[] p2;

    private final int hash;

    private String toString;

    GameState(final Deque<Integer> h1, final Deque<Integer> h2) {
      p1 = h1.toArray(new Integer[h1.size()]);
      p2 = h2.toArray(new Integer[h2.size()]);
      hash = ((32454791 + Arrays.hashCode(p1)) * 23976257 + Arrays.hashCode(p2)) * 5933573;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof GameState)) {
        return false;
      }
      GameState o = (GameState) obj;
      return Arrays.equals(p1, o.p1) && Arrays.equals(p2, o.p2);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
      return hash;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      if (toString == null) {
        StringBuilder str = new StringBuilder(128);
        str.append(Arrays.toString(p1)).append('\n');
        str.append(Arrays.toString(p2));
        toString = str.toString();
      }
      return toString;
    }
  }

  /** Get the input data for this solution. */
  private List<Deque<Integer>> getInput() {
    try {
      return Utils.getLineGroups(Files.readAllLines(Utils.getInput(2020, 22))).stream().map(this::parse).collect(
        Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private Deque<Integer> parse(final List<String> input) {
    Deque<Integer> hand = new LinkedList<>();
    for (int i = 1; i < input.size(); ++i) {
      hand.add(Integer.valueOf(input.get(i)));
    }
    return hand;
  }
}
